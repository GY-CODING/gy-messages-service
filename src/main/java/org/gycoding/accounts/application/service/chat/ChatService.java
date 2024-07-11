package org.gycoding.accounts.application.service.chat;

import org.gycoding.accounts.application.service.auth.AuthService;
import org.gycoding.accounts.domain.entities.EntityChat;
import org.gycoding.accounts.domain.entities.Message;
import org.gycoding.accounts.domain.enums.MessageStates;
import org.gycoding.accounts.domain.enums.ServerStatus;
import org.gycoding.accounts.domain.exceptions.ChatAPIException;
import org.gycoding.accounts.infrastructure.dto.ChatRQDTO;
import org.gycoding.accounts.infrastructure.external.database.service.ChatMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService implements ChatRepository {
    @Autowired
    private ChatMongoService chatMongoService = null;
    @Autowired
    private AuthService authService;

    @Override
    public EntityChat create(ChatRQDTO chatRQDTO, String userId) throws ChatAPIException {
        try {
            var chat = EntityChat.builder()
                    .chatId(UUID.randomUUID().toString())
                    .name(chatRQDTO.name())
                    .creator(userId)
                    .owner(userId)
                    .members(List.of())
                    .messages(List.of())
                    .build();

            return chatMongoService.create(chat);
        } catch(Exception e) {
            throw new ChatAPIException(ServerStatus.CHAT_EXISTS);
        }
    }

    @Override
    public void delete(UUID chatId, String userId) throws ChatAPIException {
        if(authService.isAdmin(userId)) {
            try {
                chatMongoService.delete(chatId);
            } catch(Exception e) {
                throw new ChatAPIException(ServerStatus.CHAT_NOT_FOUND);
            }
        } else {
            throw new ChatAPIException(ServerStatus.USER_NOT_ADMIN);
        }
    }

    @Override
    public Message sendMessage(UUID chatId, String content, String userId) throws ChatAPIException {
        try {
            var message = Message.builder()
                    .author(userId)
                    .content(content)
                    .date(OffsetDateTime.now().format(Message.DATE_FORMAT))
                    .state(MessageStates.SENT)
                    .build();

            return chatMongoService.sendMessage(chatId, message);
        } catch(Exception e) {
            throw new ChatAPIException(ServerStatus.MESSAGE_NOT_SENT);
        }
    }

    @Override
    public List<Message> listMessages(UUID chatId, String userId) throws ChatAPIException {
        try {
            return chatMongoService.listMessages(chatId);
        } catch(Exception e) {
            throw new ChatAPIException(ServerStatus.CHAT_NOT_FOUND);
        }
    }
}
