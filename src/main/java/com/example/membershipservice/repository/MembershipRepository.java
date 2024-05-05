package com.example.membershipservice.repository;

import com.example.membershipservice.model.Membership;
import com.example.membershipservice.model.MembershipId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Miroslav Kološnjaji
 */
public interface MembershipRepository extends ReactiveCrudRepository<Membership, MembershipId> {

    Mono<Membership> findByGymIdAndMemberIdAndDateFrom(Long gymId, Long membershipId, LocalDate dateFrom);
    Mono<Void> deleteByGymIdAndMemberIdAndDateFrom(Long gymId, Long membershipId, LocalDate dateFrom);
}
