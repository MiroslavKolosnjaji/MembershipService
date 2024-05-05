package com.example.membershipservice.service;

import com.example.membershipservice.dto.MembershipDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * @author Miroslav Kološnjaji
 */
public interface MembershipService {
    Mono<MembershipDTO> addMembership(MembershipDTO membershipDTO);
    Mono<MembershipDTO> updateMembership(MembershipDTO membershipDTO);
    Mono<MembershipDTO> getMembership(Long gymId, Long memberId, LocalDateTime created);
    Flux<MembershipDTO> getAllMemberships();
    Mono<Void> deleteMembership(Long gymId, Long memberId, LocalDateTime dateFrom);
}
