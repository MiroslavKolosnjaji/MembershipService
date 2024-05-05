package com.example.membershipservice.model;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author Miroslav Kološnjaji
 */
@Getter
public enum MembershipType {
    MONTHLY(BigDecimal.valueOf(25.00)),
    ANNUAL(BigDecimal.valueOf(500.00)),
    TRIAL(BigDecimal.valueOf(0)),
    STUDENT(BigDecimal.valueOf(18.00)),
    CORPORATE(BigDecimal.valueOf(55.00)),
    DAILY(BigDecimal.valueOf(3)),
    BIWEEKLY(BigDecimal.valueOf(15.00));

    private final BigDecimal price;

    MembershipType(BigDecimal price) {
        this.price = price;
    }

}
