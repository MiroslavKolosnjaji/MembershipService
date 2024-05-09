package com.example.membershipservice.web.handler;

import com.example.membershipservice.dto.MembershipDTO;
import com.example.membershipservice.exception.MembershipNotFoundException;
import com.example.membershipservice.exception.MissingMembershipDataException;
import com.example.membershipservice.model.Membership;
import com.example.membershipservice.service.MembershipService;
import com.example.membershipservice.web.router.MembershipRouterConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;


/**
 * @author Miroslav Kološnjaji
 */
@Component
@RequiredArgsConstructor
public class MembershipHandler {

    private static final Logger logger = LoggerFactory.getLogger(MembershipHandler.class);

    public static final String MEMBERSHIP_ID = "membershipId";
    private final MembershipService membershipService;
    private final Validator validator;


    public Mono<ServerResponse> createMembership(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MembershipDTO.class)
                .doOnSuccess(this::validate)
                .flatMap(membershipService::save)
                .flatMap(savedMembership -> ServerResponse.created(UriComponentsBuilder.fromPath(MembershipRouterConfig.MEMBERSHIP_PATH_ID)
                                .buildAndExpand(savedMembership.getId()).toUri())
                        .build())
                .onErrorResume(MissingMembershipDataException.class, ex -> {
                    logger.error("Missing membership data error occurred: {}", ex.getMessage());
                    return ServerResponse.notFound().build();
                });
    }

    public Mono<ServerResponse> updateMembership(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(MembershipDTO.class)
                .doOnSuccess(this::validate)
                .flatMap(membershipService::update)
                .flatMap(membershipDTO -> ServerResponse.noContent().build())
                .onErrorResume(MembershipNotFoundException.class, MembershipHandler::apply);
    }

    public Mono<ServerResponse> getMembershipById(ServerRequest serverRequest) {

        return membershipService.getById(Long.valueOf(serverRequest.pathVariable(MEMBERSHIP_ID)))
                .flatMap(membershipDTO -> ServerResponse.ok().bodyValue(membershipDTO))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(MembershipNotFoundException.class, MembershipHandler::apply);
    }

    public Mono<ServerResponse> getAllMemberships(ServerRequest serverRequest) {
        return ServerResponse.ok().body(membershipService.getAllMemberships(), Membership.class);
    }

    public Mono<ServerResponse> deleteMembership(ServerRequest serverRequest) {

        return membershipService.getById(Long.valueOf(serverRequest.pathVariable(MEMBERSHIP_ID)))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(membershipDTO -> membershipService.deleteById(membershipDTO.getId()))
                .then(ServerResponse.noContent().build());

    }

    private static Mono<? extends ServerResponse> apply(MembershipNotFoundException ex) {
        logger.error("Membership not found error occurred: {}", ex.getMessage());
        return ServerResponse.notFound().build();
    }

    private void validate(MembershipDTO membershipDTO) {
        Errors errors = new BeanPropertyBindingResult(membershipDTO, "membershipDTO");
        validator.validate(membershipDTO, errors);

        if (errors.hasErrors())
            throw new ServerWebInputException(errors.toString());
    }
}
