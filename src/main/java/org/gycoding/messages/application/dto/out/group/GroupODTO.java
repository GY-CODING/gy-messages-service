package org.gycoding.messages.application.dto.out.group;

import lombok.Builder;
import org.gycoding.messages.application.dto.out.MessageODTO;

import java.util.List;
import java.util.UUID;

@Builder
public record GroupODTO(
        UUID chatId,
        String name,
        String creator,
        String owner,
        List<MemberODTO> members,
        List<MessageODTO> messages
) { }