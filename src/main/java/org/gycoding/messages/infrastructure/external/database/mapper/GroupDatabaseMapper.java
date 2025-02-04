package org.gycoding.messages.infrastructure.external.database.mapper;

import org.gycoding.messages.domain.model.group.GroupMO;
import org.gycoding.messages.domain.model.group.MemberMO;
import org.gycoding.messages.infrastructure.external.database.model.GroupEntity;
import org.gycoding.messages.infrastructure.external.database.model.MemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupDatabaseMapper extends MessageDatabaseMapper {
    @Mapping(target = "chatId", expression = "java(UUID.fromString(group.chatId()))")
    GroupMO toMO(GroupEntity group);

    GroupEntity toEntity(GroupMO group);

    MemberMO toMO(MemberEntity member);

    MemberEntity toEntity(MemberMO member);
}
