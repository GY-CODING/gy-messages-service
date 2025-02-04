package org.gycoding.messages.domain.repository;

import org.gycoding.messages.domain.model.group.GroupMO;
import org.gycoding.messages.domain.model.group.MemberMO;
import org.gycoding.messages.domain.model.MessageMO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository {
    Optional<GroupMO> save(GroupMO group);
    Optional<GroupMO> get(UUID chatId);
    void delete(UUID id);

    MessageMO sendMessage(UUID chatId, MessageMO message);
    List<MessageMO> listMessages(UUID chatId);

    void addMember(UUID chatId, MemberMO member);
    void removeMember(UUID chatId, MemberMO member);
    List<MemberMO> listMembers(UUID chatId);
}
