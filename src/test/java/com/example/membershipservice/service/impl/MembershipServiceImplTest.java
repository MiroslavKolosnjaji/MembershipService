package com.example.membershipservice.service.impl;

import com.example.membershipservice.dto.MembershipDTO;
import com.example.membershipservice.exception.MissingMembershipDataException;
import com.example.membershipservice.mapper.MembershipMapper;
import com.example.membershipservice.model.Membership;
import com.example.membershipservice.model.MembershipType;
import com.example.membershipservice.repository.MembershipRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class MembershipServiceImplTest {

    @Mock
    MembershipRepository membershipRepository;

    @Mock
    MembershipMapper membershipMapper;

    @InjectMocks
    MembershipServiceImpl membershipService;

    @Test
    @Order(2)
    void testSave() {
        MembershipDTO membershipDTO = new MembershipDTO();
        membershipDTO.setId(1L);
        membershipDTO.setGymId(1L);
        membershipDTO.setMemberId(1L);
        membershipDTO.setMembershipType(MembershipType.MONTHLY);
        membershipDTO.setDateFrom(LocalDate.now());

        Membership membership = new Membership();
        membership.setId(1L);
        membership.setGymId(1L);
        membership.setMemberId(1L);
        membership.setMembershipType(MembershipType.MONTHLY);
        membership.setDateFrom(LocalDate.now());

        given(membershipMapper.membershipToMembershipDTO(membership)).willReturn(membershipDTO);
        given(membershipMapper.membershipDTOToMembership(membershipDTO)).willReturn(membership);
        given(membershipRepository.save(membership)).willReturn(Mono.just(membership));


        Mono<MembershipDTO> result = membershipService.save(membershipDTO);


        StepVerifier.create(result)
                .expectNext(membershipDTO)
                .verifyComplete();


        assertAll("Assert data",
                () -> assertEquals(LocalDate.now().plusMonths(1), membershipDTO.getDateTo(), "Expiration date is not valid"),
                () -> assertEquals(MembershipType.MONTHLY, membershipDTO.getMembershipType(), "MembershipType is not correct"),
                () -> assertEquals(MembershipType.MONTHLY.getPrice(), membershipDTO.getPrice(), "Price is not valid"));

        then(membershipRepository).should().save(any(Membership.class));
    }

    @Test
    void testSaveMembershipWithNoDateFromAndNoMembershipType() {
        MembershipDTO membershipDTO = new MembershipDTO();


        StepVerifier.create(membershipService.save(membershipDTO))
                        .expectError(MissingMembershipDataException.class)
                                .verify();

        then(membershipRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @Order(3)
    void testUpdate() {

        MembershipDTO membershipDTO = new MembershipDTO();
        membershipDTO.setId(1L);
        membershipDTO.setGymId(1L);
        membershipDTO.setMemberId(1L);
        membershipDTO.setMembershipType(MembershipType.ANNUAL);
        membershipDTO.setDateFrom(LocalDate.now());

        Membership membership = new Membership();
        membership.setId(1L);
        membership.setGymId(1L);
        membership.setMemberId(1L);
        membership.setMembershipType(MembershipType.ANNUAL);
        membership.setDateFrom(LocalDate.now());
        membership.setDateTo(LocalDate.now().plusYears(1));
        membership.setPrice(MembershipType.ANNUAL.getPrice());


        given(membershipMapper.membershipToMembershipDTO(membership)).willReturn(membershipDTO);
        given(membershipRepository.findById(anyLong())).willReturn(Mono.just(membership));
        given(membershipRepository.save(any(Membership.class))).willReturn(Mono.just(membership));

        System.out.println("MEMBERSHIP: " + membership);
        System.out.println("MEMBERSHIPDTO BEFORE UPDATE: " + membershipDTO);


        Mono<MembershipDTO> result = membershipService.update(membershipDTO);

        System.out.println("MEMBERSHIP DTO AFTER UPDATE: " + membershipDTO);

        StepVerifier.create(result)
                .assertNext(updatedDTO -> {
                    assertNotNull(updatedDTO);

                    assertAll("Assert data",
                            () -> assertEquals(LocalDate.now().plusYears(1), updatedDTO.getDateTo(), "Expiration date is not valid"),
                            () -> assertEquals(MembershipType.ANNUAL, updatedDTO.getMembershipType(), "MembershipType is not correct"),
                            () -> assertEquals(MembershipType.ANNUAL.getPrice(), updatedDTO.getPrice(), "Price is not valid"));
                })
                .verifyComplete();

        then(membershipMapper).should().membershipToMembershipDTO(any(Membership.class));
        then(membershipRepository).should().findById(anyLong());
        then(membershipRepository).should().save(any(Membership.class));

    }

    @Test
    @Order(1)
    void testGetById() {

        Membership membership = new Membership();
        membership.setId(1L);
        given(membershipRepository.findById(anyLong())).willReturn(Mono.just(membership));


        Mono<MembershipDTO> result = membershipService.getById(membership.getId());


        assertThat(result).isNotNull();
        assertNotEquals(Mono.empty(), result);
        then(membershipRepository).should().findById(anyLong());

    }

    @Test
    void testGetAllMemberships() {

        Membership membership1 = new Membership();
        membership1.setId(1L);

        Membership membership2 = new Membership();
        membership2.setId(2L);

        List<Membership> memberships = Arrays.asList(membership1, membership2);

        given(membershipMapper.membershipToMembershipDTO(membership1)).willReturn(new MembershipDTO());
        given(membershipMapper.membershipToMembershipDTO(membership2)).willReturn(new MembershipDTO());
        given(membershipRepository.findAll()).willReturn(Flux.fromIterable(memberships));


        Flux<MembershipDTO> result = membershipService.getAllMemberships();


        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();

        then(membershipRepository).should().findAll();
    }

    @Test
    void testDeleteById() {
        MembershipDTO inputDTO = new MembershipDTO();
        inputDTO.setId(1L);

        Mono<Void> result = membershipService.deleteById(inputDTO.getId());

        then(membershipRepository).should().deleteById(anyLong());

    }

    @Test
    void testGetDate() {
        LocalDate TODAY = LocalDate.now();

        LocalDate DAILY = membershipService.getDateTo(TODAY, MembershipType.DAILY);
        LocalDate MONTHLY = membershipService.getDateTo(TODAY, MembershipType.MONTHLY);
        LocalDate ANNUAL = membershipService.getDateTo(TODAY, MembershipType.ANNUAL);
        LocalDate BIWEEKLY = membershipService.getDateTo(TODAY, MembershipType.BIWEEKLY);
        LocalDate STUDENT = membershipService.getDateTo(TODAY, MembershipType.STUDENT);
        LocalDate CORPORATE = membershipService.getDateTo(TODAY, MembershipType.CORPORATE);


        assertAll("Test getDate mechanism",
                () -> assertNotNull(membershipService.getDateTo(LocalDate.now(), MembershipType.MONTHLY)),
                () -> assertEquals(TODAY.atStartOfDay().plusDays(1).toLocalDate(), DAILY, "DAILY not working"),
                () -> assertEquals(TODAY.atStartOfDay().plusMonths(1).toLocalDate(), MONTHLY, "MONTHLY not working"),
                () -> assertEquals(TODAY.atStartOfDay().plusYears(1).toLocalDate(), ANNUAL, "ANNUAL not working"),
                () -> assertEquals(TODAY.atStartOfDay().plusWeeks(2).toLocalDate(), BIWEEKLY, "BIWEEKLY not working"),
                () -> assertEquals(TODAY.atStartOfDay().plusMonths(1).toLocalDate(), STUDENT, "STUDENT not working"),
                () -> assertEquals(TODAY.atStartOfDay().plusMonths(3).toLocalDate(), CORPORATE, "CORPORATE not working"));

    }
}