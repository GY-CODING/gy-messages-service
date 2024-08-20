package org.gycoding.messages.infrastructure.external.database.repository;

import org.gycoding.messages.domain.entities.EntityChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMongoRepository extends MongoRepository<EntityChat, String>{
    EntityChat findByChatId(String chatId);
}
