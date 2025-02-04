package org.gycoding.messages.application.service.group;

import org.gycoding.messages.application.dto.in.group.GroupIDTO;
import org.gycoding.messages.application.dto.out.MessageODTO;
import org.gycoding.messages.application.dto.out.group.GroupODTO;
import org.gycoding.messages.application.dto.out.group.MemberODTO;
import org.gycoding.messages.infrastructure.api.dto.in.GroupRQDTO;
import org.gycoding.exceptions.model.APIException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupService {
    GroupODTO get(String userId, UUID chatId) throws APIException;
    GroupODTO create(String userId, GroupIDTO group) throws APIException;
    void delete(String userId, UUID chatId) throws APIException;

    void addMember(String userId, UUID chatId) throws APIException;
    List<MemberODTO> listMembers(String userId, UUID chatId) throws APIException;
    void leave(String userId, UUID chatId) throws APIException;

    MessageODTO sendMessage(String userId, UUID chatId, String content) throws APIException;
    List<MessageODTO> listMessages(String userId, UUID chatId) throws APIException;
}
