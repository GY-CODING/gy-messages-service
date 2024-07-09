package org.gycoding.accounts.infrastructure.external.database.repository;

import org.gycoding.accounts.domain.entities.EntityChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatMongoRepository extends MongoRepository<EntityChat, String>{
    EntityChat findByChatId(String chatId);
}
