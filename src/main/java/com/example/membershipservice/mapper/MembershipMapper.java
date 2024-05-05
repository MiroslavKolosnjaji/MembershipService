package com.example.membershipservice.mapper;

import com.example.membershipservice.dto.MembershipDTO;
import com.example.membershipservice.model.Membership;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface MembershipMapper {

    MembershipDTO membershipToMembershipDTO(Membership membership);
    Membership membershipDTOToMembership(MembershipDTO membershipDTO);

}
