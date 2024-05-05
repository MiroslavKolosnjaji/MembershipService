package com.example.membershipservice.web.handler;

import com.example.membershipservice.dto.MembershipDTO;
import com.example.membershipservice.exception.MembershipNotFoundException;
import com.example.membershipservice.model.Membership;
import com.example.membershipservice.service.MembershipService;
import com.example.membershipservice.web.router.MembershipRouterConfig;
import lombok.RequiredArgsConstructor;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Miroslav Kološnjaji
 */
@Component
@RequiredArgsConstructor
public class MembershipHandler {

    private final MembershipService membershipService;
    private final Validator validator;


    public Mono<ServerResponse> createMembership(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MembershipDTO.class)
                .doOnSuccess(this::validate)
                .flatMap(membershipService::addMembership)
                .flatMap(savedMembership -> ServerResponse.created(UriComponentsBuilder.fromPath(MembershipRouterConfig.MEMBERSHIP_PATH_ID)
                                .buildAndExpand(savedMembership.getGymId(), savedMembership.getMemberId(), savedMembership.getDateFrom()).toUri())
                        .build());
    }

    public Mono<ServerResponse> updateMembership(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MembershipDTO.class)
                .doOnNext(this::validate)
                .flatMap(membershipService::updateMembership)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(membershipDTO -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> getMembershipById(ServerRequest serverRequest) {
        Map<String, String> idMap = getIDs(serverRequest);

        return ServerResponse.ok()
                .body(membershipService.getMembership(Long.valueOf(idMap.get("gymId")), Long.valueOf(idMap.get("memberId")), LocalDateTime.parse(idMap.get("dateFrom")))
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))), Membership.class);
    }

    public Mono<ServerResponse> getAllMemberships(ServerRequest serverRequest) {
        return ServerResponse.ok().body(membershipService.getAllMemberships(), Membership.class);
    }

    public Mono<ServerResponse> deleteMembership(ServerRequest serverRequest) {
        Map<String, String> idMap = getIDs(serverRequest);

        return membershipService.getMembership(Long.valueOf(idMap.get("gymId")), Long.valueOf(idMap.get("memberId")),LocalDateTime.parse(idMap.get("dateFrom")))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(membershipDTO -> membershipService.deleteMembership(membershipDTO.getGymId(), membershipDTO.getMemberId(), membershipDTO.getDateFrom()))
                .then(ServerResponse.noContent().build());
    }

    private Map<String, String> getIDs(ServerRequest serverRequest) {

        Map<String, String> pathVariables = new HashMap<>(serverRequest.pathVariables());

        pathVariables.put("gymId", serverRequest.pathVariable("gymId"));
        pathVariables.put("memberId", serverRequest.pathVariable("memberId"));
        pathVariables.put("dateFrom", serverRequest.pathVariable("dateFrom"));

        return pathVariables;
    }

    private void validate(MembershipDTO membershipDTO) {
        Errors errors = new BeanPropertyBindingResult(membershipDTO, "membershipDTO");
        validator.validate(membershipDTO, errors);

        if (errors.hasErrors())
            throw new ServerWebInputException(errors.toString());
    }
}
