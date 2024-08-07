package org.gycoding.accounts.application.service.chat;

import org.gycoding.accounts.domain.entities.EntityChat;
import org.gycoding.accounts.domain.entities.Member;
import org.gycoding.accounts.domain.entities.Message;
import org.gycoding.accounts.infrastructure.dto.ChatRQDTO;
import org.gycoding.exceptions.model.APIException;

import java.util.List;
import java.util.UUID;

public interface ChatRepository {
    EntityChat create(ChatRQDTO chatRQDTO, String jwt) throws APIException;
    void delete(UUID chatId, String userId) throws APIException;
    void leave(UUID chatId, String userId) throws APIException;

    Message sendMessage(UUID chatId, String content, String userId) throws APIException;

    EntityChat getChat(UUID chatId, String userId) throws APIException;

    void addMember(UUID chatId, String jwt) throws APIException;
    List<Message> listMessages(UUID chatId, String userId) throws APIException;
    List<Member> listMembers(UUID chatId, String userId) throws APIException;
    List<EntityChat> listChats(String userId) throws APIException;
}
