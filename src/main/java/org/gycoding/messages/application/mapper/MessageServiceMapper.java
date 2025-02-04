package org.gycoding.messages.application.mapper;

import org.gycoding.messages.application.dto.in.MessageIDTO;
import org.gycoding.messages.application.dto.out.MessageODTO;
import org.gycoding.messages.application.dto.out.group.GroupODTO;
import org.gycoding.messages.domain.model.MessageMO;
import org.gycoding.messages.domain.model.group.GroupMO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageServiceMapper {
    MessageODTO toODTO(MessageMO message);

    MessageMO toMO(MessageIDTO message);
}
