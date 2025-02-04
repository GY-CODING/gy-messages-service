package org.gycoding.messages.infrastructure.external.database.repository.impl;

import lombok.AllArgsConstructor;
import org.gycoding.messages.domain.model.group.GroupMO;
import org.gycoding.messages.domain.model.group.MemberMO;
import org.gycoding.messages.domain.model.MessageMO;
import org.gycoding.messages.domain.repository.GroupRepository;
import org.gycoding.messages.infrastructure.external.database.repository.GroupMongoRepository;
import org.gycoding.messages.infrastructure.external.database.mapper.GroupDatabaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {
    private final GroupMongoRepository repository;

    private final GroupDatabaseMapper mapper;

    @Override
    public Optional<GroupMO> save(GroupMO chat) {
        return Optional.of(mapper.toMO(repository.save(mapper.toEntity(chat))));
    }

    @Override
    public Optional<GroupMO> get(UUID chatId) {
        return Optional.of(mapper.toMO(repository.findByChatId(chatId.toString()).get()));
    }

    @Override
    public void delete(UUID chatId) {
        repository.delete(repository.findByChatId(chatId.toString()).get());
    }

    @Override
    public MessageMO sendMessage(UUID chatId, MessageMO messageMO) {
        var chat = repository.findByChatId(chatId.toString()).get();
        chat.messages().add(mapper.toEntity(messageMO));

        repository.save(chat);

        return messageMO;
    }

    @Override
    public List<MessageMO> listMessages(UUID chatId) {
        return mapper.toMO(repository.findByChatId(chatId.toString()).get()).messages();
    }

    @Override
    public void addMember(UUID chatId, MemberMO memberMO) {
        var chat = repository.findByChatId(chatId.toString()).get();
        chat.members().add(mapper.toEntity(memberMO));

        repository.save(chat);
    }

    @Override
    public void removeMember(UUID chatId, MemberMO memberMO) {
        var chat = repository.findByChatId(chatId.toString()).get();
        chat.members().remove(mapper.toEntity(memberMO));

        if(chat.members().isEmpty()) {
            repository.delete(chat);
            return;
        }

        repository.save(chat);
    }

    @Override
    public List<MemberMO> listMembers(UUID chatId) throws NullPointerException {
        return mapper.toMO(repository.findByChatId(chatId.toString()).get()).members();
    }
}
