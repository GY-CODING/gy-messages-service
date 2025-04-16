package org.gycoding.messages.infrastructure.external.gyaccounts;

import kong.unirest.HttpResponse;
import org.gycoding.messages.domain.repository.GYAccountsFacade;
import org.gycoding.messages.infrastructure.external.unirest.UnirestFacade;
import org.gycoding.messages.shared.utils.logger.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class GYAccountsFacadeImpl implements GYAccountsFacade {
    private @Value("${gy.accounts.url}") String url;
    private @Value("${allowed.apiKey}") String apiKey;

    @Override
    public List<UUID> listChats(String userId) {
        final var headers = new HashMap<String, String>();

        headers.put("x-user-id", userId);
        headers.put("x-api-key", apiKey);

        HttpResponse<String> response = UnirestFacade.get(
                url + "/messages/chats",
                headers
        );
        var parser = new JSONParser();
        var chats = new ArrayList<UUID>();

        try {
            for (Object obj : (JSONArray) parser.parse(response.getBody())) {
                JSONObject jsonObject   = (JSONObject) obj;

                String chatId           = (String) jsonObject.get("chatId");

                chats.add(UUID.fromString(chatId));
            }
        } catch (ParseException e) {
            Logger.error("An error has occured while parsing chat list from metadata response: " + e.getMessage(), e);
        }

        return chats;
    }

    @Override
    public void addChat(String userId, UUID chatId, Boolean isAdmin) {
        final var headers = new HashMap<String, String>();

        headers.put("x-user-id", userId);
        headers.put("x-api-key", apiKey);
        headers.put("Content-Type", "application/json");

        UnirestFacade.patch(
                url + "/messages/chats/add",
                headers,
                String.format("{\"chatId\": \"%s\", \"isAdmin\": %s}", chatId.toString(), isAdmin.toString())
        );
    }

    @Override
    public void removeChat(String userId, UUID chatId) {
        final var headers = new HashMap<String, String>();

        headers.put("x-user-id", userId);
        headers.put("x-api-key", apiKey);

        UnirestFacade.delete(
                url + String.format("/messages/chats?chatId=%s", chatId.toString()),
                headers
        );
    }

    @Override
    public String getUsername(String userId) {
        final var headers = new HashMap<String, String>();

        headers.put("x-user-id", userId);
        headers.put("x-api-key", apiKey);

        HttpResponse<String> response = UnirestFacade.get(
                url + "/user/username",
                headers
        );

        return response.getBody();
    }
}
