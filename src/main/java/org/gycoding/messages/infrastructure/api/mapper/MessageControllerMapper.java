package org.gycoding.messages.infrastructure.api.mapper;

import org.gycoding.messages.application.dto.in.MessageIDTO;
import org.gycoding.messages.application.dto.out.MessageODTO;
import org.gycoding.messages.infrastructure.api.dto.in.MessageRQDTO;
import org.gycoding.messages.infrastructure.api.dto.out.MessageRSDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageControllerMapper {
    MessageRSDTO toRSDTO(MessageODTO message);

    MessageIDTO toIDTO(MessageRQDTO message);
}