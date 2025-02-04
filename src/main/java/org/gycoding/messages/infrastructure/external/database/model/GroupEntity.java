package org.gycoding.messages.infrastructure.external.database.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
@Document(collection = "Chat")
public record GroupEntity(
        @Id
        String mongoId,
        String chatId,
        String name,
        String creator,
        String owner,
        List<MemberEntity> members,
        List<MessageEntity> messages
) { }