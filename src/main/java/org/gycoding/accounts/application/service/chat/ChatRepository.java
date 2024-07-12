package org.gycoding.accounts.application.service.chat;

import org.gycoding.accounts.domain.entities.EntityChat;
import org.gycoding.accounts.domain.entities.Member;
import org.gycoding.accounts.domain.entities.Message;
import org.gycoding.accounts.domain.exceptions.ChatAPIException;
import org.gycoding.accounts.infrastructure.dto.ChatRQDTO;

import java.util.List;
import java.util.UUID;

public interface ChatRepository {
    EntityChat create(ChatRQDTO chatRQDTO, String jwt) throws ChatAPIException;
    void delete(UUID chatId, String userId) throws ChatAPIException;
    void leave(UUID chatId, String userId) throws ChatAPIException;

    Message sendMessage(UUID chatId, String content, String userId) throws ChatAPIException;

    EntityChat getChat(UUID chatId, String userId) throws ChatAPIException;

    void addMember(UUID chatId, String jwt) throws ChatAPIException;
    List<Message> listMessages(UUID chatId, String userId) throws ChatAPIException;
    List<Member> listMembers(UUID chatId, String userId) throws ChatAPIException;
    List<EntityChat> listChats(String userId) throws ChatAPIException;
}
