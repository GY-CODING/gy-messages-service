package org.gycoding.accounts.domain.entities;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
@Document(collection = "Chat")
public record EntityChat(
        @Id
        String mongoId,
        String chatId,
        String name,
        String creator,
        String owner,
        List<?> members,
        List<Message> messages
) {
    @Override
    public String toString() {
        return "{" +
                "\"chatId\": " + chatId +
                ", \"name\": \"" + name + "\"" +
                ", \"creator\": \"" + creator + "\"" +
                ", \"owner\": \"" + owner + "\"" +
                ", \"members\": " + members +
                ", \"messages\": " + messages +
                "}";
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "chatId", chatId,
                "name", name,
                "creator", creator,
                "owner", owner,
                "members", members,
                "messages", messages
        );
    }
}
