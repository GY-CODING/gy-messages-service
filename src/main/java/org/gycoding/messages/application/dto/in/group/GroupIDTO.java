package org.gycoding.messages.application.dto.in.group;

import lombok.Builder;
import org.gycoding.messages.application.dto.in.MessageIDTO;
import org.gycoding.messages.application.dto.out.MessageODTO;
import org.gycoding.messages.application.dto.out.group.MemberODTO;

import java.util.List;
import java.util.UUID;

@Builder
public record GroupIDTO(
        UUID chatId,
        String name,
        String creator,
        String owner,
        List<MemberIDTO> members,
        List<MessageIDTO> messages
) { }