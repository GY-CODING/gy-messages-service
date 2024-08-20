package org.gycoding.messages.infrastructure.external.database.service;

import org.gycoding.messages.domain.entities.EntityChat;
import org.gycoding.messages.domain.entities.Member;
import org.gycoding.messages.domain.entities.Message;
import org.gycoding.messages.infrastructure.dto.GYAccountsChatDTO;
import org.gycoding.messages.infrastructure.external.database.repository.ChatMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatMongoService {
    @Autowired
    private ChatMongoRepository chatMongoRepository;

    public EntityChat create(EntityChat chat) {
        return chatMongoRepository.save(chat);
    }

    public void delete(UUID chatId) {
        chatMongoRepository.delete(this.getChat(chatId));
    }

    public Message sendMessage(UUID chatId, Message message) {
        var chat = this.getChat(chatId);
        chat.messages().add(message);

        chatMongoRepository.save(chat);

        return message;
    }

    public EntityChat getChat(UUID chatId) {
        return chatMongoRepository.findByChatId(chatId.toString());
    }

    public List<EntityChat> listChats(List<GYAccountsChatDTO> chatsFromMetadata) {
        List<EntityChat> chats = new ArrayList<>();

        for (String chatId : chatsFromMetadata.stream().map(GYAccountsChatDTO::chatId).toList()) {
            chats.add(this.getChat(UUID.fromString(chatId)));
        }

        return chats;
    }

    public List<Message> listMessages(UUID chatId) throws Exception {
        try {
            return this.getChat(chatId).messages();
        } catch (NullPointerException e) {
            throw e;
        }
    }

    public void addMember(UUID chatId, Member member) {
        var chat = this.getChat(chatId);
        chat.members().add(member);

        chatMongoRepository.save(chat);
    }

    public void removeMember(UUID chatId, Member member) {
        var chat = this.getChat(chatId);
        chat.members().remove(member);

        chatMongoRepository.save(chat);
    }

    public List<Member> listMembers(UUID chatId) throws NullPointerException {
        return this.getChat(chatId).members();
    }
}
