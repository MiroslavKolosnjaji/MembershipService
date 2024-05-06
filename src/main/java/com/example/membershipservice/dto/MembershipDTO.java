package com.example.membershipservice.dto;

import com.example.membershipservice.model.MembershipType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipDTO {

    private Long id;

    @NotNull
    private Long gymId;

    @NotNull
    private Long memberId;

    @NotNull
    private LocalDate dateFrom;

    @NotNull
    private LocalDate dateTo;

    @NotNull
    private MembershipType membershipType;

    @NotNull
    private BigDecimal price;
}
