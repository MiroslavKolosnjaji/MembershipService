package com.example.membershipservice.dto;

import com.example.membershipservice.model.MembershipType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipDTO {

    private Long gymId;
    private Long memberId;
    private LocalDateTime dateFrom;

    @NotNull
    private LocalDateTime dateTo;

    @NotNull
    private MembershipType membershipType;

    @NotNull
    private BigDecimal price;
}
