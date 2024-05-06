package com.example.membershipservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Membership {

    @Id
    private Long id;
    private Long gymId;
    private Long memberId;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private MembershipType membershipType;
    private BigDecimal price;
}
