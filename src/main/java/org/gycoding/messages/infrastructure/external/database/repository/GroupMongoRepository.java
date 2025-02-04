package org.gycoding.messages.infrastructure.external.database.repository;

import org.gycoding.messages.infrastructure.external.database.model.GroupEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupMongoRepository extends MongoRepository<GroupEntity, String>{
    Optional<GroupEntity> findByChatId(String chatId);
}
