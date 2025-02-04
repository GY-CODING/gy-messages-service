package org.gycoding.messages.infrastructure.external.database.mapper;

import org.gycoding.messages.domain.model.MessageMO;
import org.gycoding.messages.infrastructure.external.database.model.MessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageDatabaseMapper {
    MessageMO toMO(MessageEntity message);

    MessageEntity toEntity(MessageMO message);
}
