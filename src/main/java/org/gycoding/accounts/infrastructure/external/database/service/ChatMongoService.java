package org.gycoding.accounts.infrastructure.external.database.service;

import org.gycoding.accounts.domain.entities.EntityChat;
import org.gycoding.accounts.domain.entities.Message;
import org.gycoding.accounts.infrastructure.external.database.repository.ChatMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatMongoService {
    @Autowired
    private ChatMongoRepository chatMongoRepository;

    private EntityChat getChat(UUID chatId) {
        return chatMongoRepository.findByChatId(chatId.toString());
    }

    public EntityChat create(EntityChat chat) {
        return chatMongoRepository.save(chat);
    }

    public Message sendMessage(UUID chatId, Message message) {
        var chat = this.getChat(chatId);
        chat.messages().add(message);

        chatMongoRepository.save(chat);

        return message;
    }

    public List<Message> listMessages(UUID chatId) throws Exception {
        try {
            return this.getChat(chatId).messages();
        } catch (NullPointerException e) {
            throw e;
        }
    }
}
