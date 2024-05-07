package com.example.membershipservice.web.handler;

import com.example.membershipservice.dto.MembershipDTO;
import com.example.membershipservice.model.Membership;
import com.example.membershipservice.model.MembershipType;
import com.example.membershipservice.service.impl.MembershipServiceImpl;
import com.example.membershipservice.web.router.MembershipRouterConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MembershipEndPointTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    MembershipServiceImpl membershipService;


    @Test
    void testCreateMembership() {

        webTestClient.post()
                .uri(MembershipRouterConfig.MEMBERSHIP_PATH)
                .body(Mono.just(getTestMembership(null, MembershipType.MONTHLY)), Membership.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("/api/membership/7");
    }

    @Test
    void testCreateMembershipBadRequest() {

        var membership = getTestMembership(null, MembershipType.MONTHLY);
        membership.setMembershipType(null);

        webTestClient.post()
                .uri(MembershipRouterConfig.MEMBERSHIP_PATH)
                .body(Mono.just(membership), Membership.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    void testUpdateMembership() {

        var membership = getTestMembership(1L, MembershipType.MONTHLY);
        membership.setMembershipType(MembershipType.ANNUAL);
        membership.setDateTo(membershipService.getDateTo(membership.getDateFrom(), membership.getMembershipType()));
        membership.setPrice(membership.getMembershipType().getPrice());

        webTestClient.put()
                .uri(MembershipRouterConfig.MEMBERSHIP_PATH_ID, membership.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(membership), Membership.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateMembershipBadRequest() {

        var membership = getTestMembership(1L, MembershipType.MONTHLY);
        membership.setPrice(null);

        webTestClient.put()
                .uri(MembershipRouterConfig.MEMBERSHIP_PATH_ID, membership.getId())
                .body(Mono.just(membership), Membership.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateMemberNotFound() {
        var membership = getTestMembership(12L, MembershipType.MONTHLY);

        webTestClient.put()
                .uri(MembershipRouterConfig.MEMBERSHIP_PATH_ID, membership.getId())
                .body(Mono.just(membership), Membership.class)
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    @Order(1)
    void testGetMembershipById() {
        webTestClient.get()
                .uri(MembershipRouterConfig.MEMBERSHIP_PATH_ID, 1, 1, LocalDate.now())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody(MembershipDTO.class);
    }

    @Test
    void testGetBembershipByIdNotFound() {
        webTestClient.get()
                .uri(MembershipRouterConfig.MEMBERSHIP_PATH_ID, 1, 1, LocalDate.now().plusMonths(1))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(2)
    void testGetAllMemberships() {
        webTestClient.get()
                .uri(MembershipRouterConfig.MEMBERSHIP_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()", hasSize(greaterThan(1)));
    }

    @Test
    @Order(999)
    void testDeleteMembership() {
        var membership = getTestMembership(1L, MembershipType.MONTHLY);

        webTestClient.delete()
                .uri(MembershipRouterConfig.MEMBERSHIP_PATH_ID, membership.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteMembershipNotFound() {
        webTestClient.delete()
                .uri(MembershipRouterConfig.MEMBERSHIP_PATH_ID, 1, 1, LocalDate.now().plusMonths(1))
                .exchange()
                .expectStatus().isNotFound();
    }

    private Membership getTestMembership(Long membershipId, MembershipType membershipType) {
        return Membership.builder()
                .id(membershipId)
                .gymId(1L)
                .memberId(1L)
                .membershipType(membershipType)
                .dateFrom(LocalDate.now())
                .dateTo(membershipService.getDateTo(LocalDate.now(), membershipType))
                .price(membershipType.getPrice())
                .build();
    }
}