package org.gycoding.messages.application.mapper;

import org.gycoding.messages.application.dto.in.group.GroupIDTO;
import org.gycoding.messages.application.dto.out.group.GroupODTO;
import org.gycoding.messages.application.dto.out.group.MemberODTO;
import org.gycoding.messages.domain.model.group.GroupMO;
import org.gycoding.messages.domain.model.group.MemberMO;
import org.gycoding.messages.domain.repository.GYAccountsFacade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {MessageServiceMapper.class, GYAccountsFacade.class})
public interface GroupServiceMapper extends MessageServiceMapper {
    GroupODTO toODTO(GroupMO group);

    @Mapping(expression = "java(mapMembers(group, gyAccountsFacade))", target = "members")
    GroupMO toMO(GroupIDTO group, GYAccountsFacade gyAccountsFacade);

    MemberODTO toODTO(MemberMO member);

    default List<MemberMO> mapMembers(GroupIDTO group, GYAccountsFacade gyAccountsFacade) {
        return group.members().stream()
                .map(member -> {
                    return MemberMO.builder()
                            .userId(member.userId())
                            .username(gyAccountsFacade.getUsername(member.userId()))
                            .isAdmin(member.isAdmin())
                            .build();
                })
                .toList();
    }
}
