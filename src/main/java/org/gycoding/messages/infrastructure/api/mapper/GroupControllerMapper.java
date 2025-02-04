package org.gycoding.messages.infrastructure.api.mapper;

import org.gycoding.messages.application.dto.in.group.GroupIDTO;
import org.gycoding.messages.application.dto.in.group.MemberIDTO;
import org.gycoding.messages.application.dto.out.group.GroupODTO;
import org.gycoding.messages.application.dto.out.group.MemberODTO;
import org.gycoding.messages.infrastructure.api.dto.in.GroupRQDTO;
import org.gycoding.messages.infrastructure.api.dto.out.group.GroupRSDTO;
import org.gycoding.messages.infrastructure.api.dto.out.group.MemberRSDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {UUID.class, MemberIDTO.class})
public interface GroupControllerMapper extends MessageControllerMapper {
    GroupRSDTO toRSDTO(GroupODTO group);

    @Mapping(expression = "java(UUID.randomUUID())", target = "chatId")
    @Mapping(source = "group.name", target = "name")
    @Mapping(source = "userId", target = "owner")
    @Mapping(source = "userId", target = "creator")
    @Mapping(expression = "java(List.of(MemberIDTO.builder().userId(userId).isAdmin(Boolean.TRUE).build()))", target = "members")
    @Mapping(expression = "java(List.of())", target = "messages")
    GroupIDTO toIDTO(GroupRQDTO group, String userId);

    MemberRSDTO toRSDTO(MemberODTO member);
}