package com.example.membershipservice.bootstrap;

import com.example.membershipservice.dto.MembershipDTO;
import com.example.membershipservice.model.MembershipType;
import com.example.membershipservice.repository.MembershipRepository;
import com.example.membershipservice.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author Miroslav Kološnjaji
 */
@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner {

    private final MembershipService membershipService;
    private final MembershipRepository membershipRepository;

    @Override
    public void run(String... args) throws Exception {
        membershipRepository.deleteAll().doOnSuccess(success -> loadMembershipData()).subscribe();
    }

    private void loadMembershipData(){
        membershipRepository.count().subscribe(count -> {
            LocalDate now = LocalDate.now();

            MembershipDTO membership1 = MembershipDTO.builder().gymId(1L).memberId(1L).dateFrom(now).membershipType(MembershipType.MONTHLY).build();
            MembershipDTO membership2 = MembershipDTO.builder().gymId(1L).memberId(2L).dateFrom(now).membershipType(MembershipType.ANNUAL).build();
            MembershipDTO membership3 = MembershipDTO.builder().gymId(1L).memberId(3L).dateFrom(now).membershipType(MembershipType.BIWEEKLY).build();
            MembershipDTO membership4 = MembershipDTO.builder().gymId(1L).memberId(4L).dateFrom(now).membershipType(MembershipType.DAILY).build();
            MembershipDTO membership5 = MembershipDTO.builder().gymId(1L).memberId(5L).dateFrom(now).membershipType(MembershipType.STUDENT).build();
            MembershipDTO membership6 = MembershipDTO.builder().gymId(1L).memberId(6L).dateFrom(now).membershipType(MembershipType.CORPORATE).build();



            membershipService.save(membership1).subscribe();
            membershipService.save(membership2).subscribe();
            membershipService.save(membership3).subscribe();
            membershipService.save(membership4).subscribe();
            membershipService.save(membership5).subscribe();
            membershipService.save(membership6).subscribe();
        });
    }

}
