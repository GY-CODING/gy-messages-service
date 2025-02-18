package org.gycoding.messages.infrastructure.external.gyaccounts;

import kong.unirest.HttpResponse;
import org.gycoding.messages.domain.repository.GYAccountsFacade;
import org.gycoding.messages.infrastructure.external.unirest.UnirestFacade;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class GYAccountsFacadeImpl implements GYAccountsFacade {
    private @Value("${gy.accounts.url}") String URL;
    private @Value("${allowed.apiKey}") String API_KEY;

    @Override
    public List<UUID> listChats(String userId) {
        final var headers = new HashMap<String, String>();

        headers.put("x-user-id", userId);
        headers.put("x-api-key", API_KEY);

        UnirestFacade.get(
                URL + "/messages/chats",
                headers
        );

        HttpResponse<String> response = null;
        var parser = new JSONParser();
        var chats = new ArrayList<UUID>();

        for (Object obj : (JSONArray) parser.parse(response.getBody())) {
            JSONObject jsonObject   = (JSONObject) obj;

            String chatId           = (String) jsonObject.get("chatId");

            chats.add(UUID.fromString(chatId));
        }

        return chats;
    }

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
