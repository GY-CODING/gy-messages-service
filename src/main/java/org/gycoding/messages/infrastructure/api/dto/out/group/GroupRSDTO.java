package org.gycoding.messages.infrastructure.api.dto.out.group;

import lombok.Builder;
import org.gycoding.messages.application.dto.out.MessageODTO;
import org.gycoding.messages.application.dto.out.group.MemberODTO;
import org.gycoding.messages.infrastructure.api.dto.out.MessageRSDTO;

import java.util.List;
import java.util.UUID;

@Builder
public record GroupRSDTO(
        UUID chatId,
        String name,
        String creator,
        String owner,
        List<MemberRSDTO> members,
        List<MessageRSDTO> messages
) { }