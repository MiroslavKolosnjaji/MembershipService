package com.example.membershipservice.service;

import com.example.membershipservice.dto.MembershipDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Miroslav Kološnjaji
 */
public interface MembershipService {
    Mono<MembershipDTO> save(MembershipDTO membershipDTO);
    Mono<MembershipDTO> update(MembershipDTO membershipDTO);
    Mono<MembershipDTO> getById(Long id);
    Flux<MembershipDTO> getAllMemberships();
    Mono<Void> deleteById(Long id);
}
