package com.example.membershipservice.repository;

import com.example.membershipservice.model.Membership;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


/**
 * @author Miroslav Kološnjaji
 */
public interface MembershipRepository extends ReactiveCrudRepository<Membership, Long> {
}
