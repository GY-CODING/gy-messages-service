package org.gycoding.messages.domain.model.group;

import lombok.Builder;
import org.gycoding.messages.domain.model.MessageMO;

import java.util.List;
import java.util.UUID;

@Builder
public record GroupMO(
        UUID chatId,
        String name,
        String creator,
        String owner,
        List<MemberMO> members,
        List<MessageMO> messages
) { }