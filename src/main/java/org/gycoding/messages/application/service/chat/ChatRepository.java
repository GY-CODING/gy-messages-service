package org.gycoding.messages.application.service.chat;

import org.gycoding.messages.domain.entities.EntityChat;
import org.gycoding.messages.domain.entities.Member;
import org.gycoding.messages.domain.entities.Message;
import org.gycoding.messages.infrastructure.dto.ChatRQDTO;
import org.gycoding.exceptions.model.APIException;

import java.util.List;
import java.util.UUID;

public interface ChatRepository {
    EntityChat create(ChatRQDTO chatRQDTO, String token) throws APIException;
    void delete(UUID chatId, String token) throws APIException;
    void leave(UUID chatId, String token) throws APIException;

    Message sendMessage(UUID chatId, String content, String token) throws APIException;

    EntityChat getChat(UUID chatId, String userId) throws APIException;

    void addMember(UUID chatId, String token) throws APIException;
    List<Message> listMessages(UUID chatId, String token) throws APIException;
    List<Member> listMembers(UUID chatId, String token) throws APIException;
    List<EntityChat> listChats(String token) throws APIException;
}
