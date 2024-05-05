package com.example.membershipservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipId implements Serializable {

    @Id
    @Column("gym_id")
    private Long gymId;

    @Id
    @Column("member_id")
    private Long memberId;

    @Id
    @Column("date_from")
    private LocalDateTime dateFrom;
}
