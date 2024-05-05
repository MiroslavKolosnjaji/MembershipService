package com.example.membershipservice.service.impl;

import com.example.membershipservice.dto.MembershipDTO;
import com.example.membershipservice.exception.MembershipNotFoundException;
import com.example.membershipservice.mapper.MembershipMapper;
import com.example.membershipservice.model.MembershipType;
import com.example.membershipservice.repository.MembershipRepository;
import com.example.membershipservice.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;
    private final MembershipMapper membershipMapper;


    @Override
    public Mono<MembershipDTO> addMembership(MembershipDTO membershipDTO) {

        membershipDTO.setDateTo(getDateTo(membershipDTO.getDateFrom(), membershipDTO.getMembershipType()));
        membershipDTO.setPrice(membershipDTO.getMembershipType().getPrice());

        return membershipRepository.save(membershipMapper.membershipDTOToMembership(membershipDTO))
                .map(membershipMapper::membershipToMembershipDTO);
    }

    @Override
    public Mono<MembershipDTO> updateMembership(MembershipDTO membershipDTO) {

        return membershipRepository.findByGymIdAndMemberIdAndDateFrom(membershipDTO.getGymId()
                        , membershipDTO.getMemberId(), membershipDTO.getDateFrom())
                .switchIfEmpty(Mono.error(new MembershipNotFoundException("Membership not found!")))
                .map(foundMembership -> {
                    foundMembership.setGymId(membershipDTO.getGymId());
                    foundMembership.setMemberId(membershipDTO.getMemberId());
                    foundMembership.setDateFrom(membershipDTO.getDateFrom());
                    foundMembership.setMembershipType(membershipDTO.getMembershipType());
                    foundMembership.setDateTo(getDateTo(membershipDTO.getDateFrom(), membershipDTO.getMembershipType()));
                    foundMembership.setPrice(membershipDTO.getMembershipType().getPrice());
                    return foundMembership;
                }).flatMap(membershipRepository::save)
                .map(membershipMapper::membershipToMembershipDTO);
    }

    @Override
    public Mono<MembershipDTO> getMembership(Long gymId, Long memberId, LocalDateTime created) {
        return membershipRepository.findByGymIdAndMemberIdAndDateFrom(gymId, memberId, created)
                .switchIfEmpty(Mono.error(new MembershipNotFoundException("Membership not found!")))
                .map(membershipMapper::membershipToMembershipDTO);
    }

    @Override
    public Flux<MembershipDTO> getAllMemberships() {
        return membershipRepository.findAll().map(membershipMapper::membershipToMembershipDTO);
    }

    @Override
    public Mono<Void> deleteMembership(Long gymId, Long memberId, LocalDateTime dateFrom) {
        return membershipRepository.deleteByGymIdAndMemberIdAndDateFrom(gymId, memberId, dateFrom);
    }

    public LocalDateTime getDateTo(LocalDateTime dateFrom, MembershipType membershipType) {
        return switch (membershipType) {
            case TRIAL -> dateFrom.plusDays(3);
            case DAILY -> dateFrom.plusDays(1);
            case BIWEEKLY -> dateFrom.plusWeeks(2);
            case MONTHLY, STUDENT -> dateFrom.plusMonths(1);
            case ANNUAL -> dateFrom.plusYears(1);
            case CORPORATE -> dateFrom.plusMonths(3);
        };
    }
}