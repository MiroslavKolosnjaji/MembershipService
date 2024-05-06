package com.example.membershipservice.service.impl;

import com.example.membershipservice.dto.MembershipDTO;
import com.example.membershipservice.exception.MembershipNotFoundException;
import com.example.membershipservice.mapper.MembershipMapper;
import com.example.membershipservice.model.MembershipType;
import com.example.membershipservice.repository.MembershipRepository;
import com.example.membershipservice.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
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
    public Mono<MembershipDTO> save(MembershipDTO membershipDTO) {

        membershipDTO.setDateTo(getDateTo(membershipDTO.getDateFrom(), membershipDTO.getMembershipType()));
        membershipDTO.setPrice(membershipDTO.getMembershipType().getPrice());

        return membershipRepository.save(membershipMapper.membershipDTOToMembership(membershipDTO))
                .map(membershipMapper::membershipToMembershipDTO);
    }

    @Override
    public Mono<MembershipDTO> update(MembershipDTO membershipDTO) {

        return membershipRepository.findById(membershipDTO.getId())
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

    public Mono<MembershipDTO> getById(Long id) {

        return membershipRepository.findById(id)
                .switchIfEmpty(Mono.error(new MembershipNotFoundException("Membership not found!")))
                .map(membershipMapper::membershipToMembershipDTO)
                .flatMap(Mono::just)
                .onErrorResume(MembershipNotFoundException.class, ex -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));

    }

    @Override
    public Flux<MembershipDTO> getAllMemberships() {
        return membershipRepository.findAll().map(membershipMapper::membershipToMembershipDTO);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return membershipRepository.deleteById(id);
    }

    public LocalDate getDateTo(LocalDate dateFrom, MembershipType membershipType) {
        LocalDateTime dateFromWithTime = dateFrom.atStartOfDay();

        LocalDateTime dateTo = switch (membershipType) {
            case TRIAL -> dateFromWithTime.plusDays(3);
            case DAILY -> dateFromWithTime.plusDays(1);
            case BIWEEKLY -> dateFromWithTime.plusWeeks(2);
            case MONTHLY, STUDENT -> dateFromWithTime.plusMonths(1);
            case ANNUAL -> dateFromWithTime.plusYears(1);
            case CORPORATE -> dateFromWithTime.plusMonths(3);
        };

        return dateTo.toLocalDate();
    }
}
