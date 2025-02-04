package org.gycoding.messages.application.mapper;

import org.gycoding.messages.application.dto.in.group.GroupIDTO;
import org.gycoding.messages.application.dto.out.group.GroupODTO;
import org.gycoding.messages.application.dto.out.group.MemberODTO;
import org.gycoding.messages.domain.model.group.GroupMO;
import org.gycoding.messages.domain.model.group.MemberMO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {MessageServiceMapper.class})
public interface GroupServiceMapper extends MessageServiceMapper {
    GroupODTO toODTO(GroupMO group);

    GroupMO toMO(GroupIDTO group);

    MemberODTO toODTO(MemberMO member);
}
