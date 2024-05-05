package com.example.membershipservice.web.router;

import com.example.membershipservice.web.handler.MembershipHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author Miroslav Kološnjaji
 */
@Configuration
@RequiredArgsConstructor
public class MembershipRouterConfig {

    public static final String MEMBERSHIP_PATH = "/api/membership";
    public static final String MEMBERSHIP_PATH_ID = MEMBERSHIP_PATH + "/{gymId}/{memberId}/{dateFrom}";

    private final MembershipHandler membershipHandler;

    @Bean
    public RouterFunction<ServerResponse> membershipRouter() {
        return route()
                .POST(MEMBERSHIP_PATH, membershipHandler::createMembership)
                .PUT(MEMBERSHIP_PATH_ID, membershipHandler::updateMembership)
                .GET(MEMBERSHIP_PATH_ID, membershipHandler::getMembershipById)
                .GET(MEMBERSHIP_PATH, membershipHandler::getAllMemberships)
                .DELETE(MEMBERSHIP_PATH_ID, membershipHandler::deleteMembership)
                .build();
    }

}
