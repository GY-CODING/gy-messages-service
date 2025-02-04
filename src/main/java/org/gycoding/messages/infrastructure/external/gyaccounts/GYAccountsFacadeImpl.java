package org.gycoding.messages.infrastructure.external.gyaccounts;

import org.gycoding.messages.domain.repository.GYAccountsFacade;
import org.gycoding.messages.infrastructure.external.unirest.UnirestFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class GYAccountsFacadeImpl implements GYAccountsFacade {
    private @Value("${gy.accounts.url}") String URL;
    private @Value("${allowed.apiKey}") String API_KEY;

    @Override
    public void addChat(String userId, UUID chatId, Boolean isAdmin) {
        final var headers = new HashMap<String, String>();

        headers.put("x-user-id", userId);
        headers.put("x-api-key", API_KEY);
        headers.put("Content-Type", "application/json");

        UnirestFacade.patch(
                URL + "/messages/chats/add",
                headers,
                String.format("{\"chatId\": \"%s\", \"isAdmin\": %s}", chatId.toString(), isAdmin.toString())
        );
    }

    @Override
    public void removeChat(String userId, UUID chatId) {
        final var headers = new HashMap<String, String>();

        headers.put("x-user-id", userId);
        headers.put("x-api-key", API_KEY);

        UnirestFacade.delete(
                URL + String.format("/messages/chats?chatId=%s", chatId.toString()),
                headers
        );
    }
}
