package com.example.membershipservice.repository;

import com.example.membershipservice.model.Membership;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;



/**
 * @author Miroslav Kološnjaji
 */
public interface MembershipRepository extends ReactiveCrudRepository<Membership, Long> {
}
