package com.example.membershipservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Membership {

    @Column("gym_id")
    private Long gymId;

    @Column("member_id")
    private Long memberId;

    @Column("date_from")
    @CreatedDate
    private LocalDate dateFrom;

    private LocalDate dateTo;
    private MembershipType membershipType;
    private BigDecimal price;
}
