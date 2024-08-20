package org.gycoding.messages.infrastructure.external.gyaccounts;

import org.gycoding.messages.domain.exceptions.ChatAPIError;
import org.gycoding.messages.infrastructure.dto.GYAccountsChatDTO;
import org.gycoding.messages.infrastructure.external.unirest.UnirestFacade;
import org.gycoding.exceptions.model.APIException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class GYAccountsFacadeImpl implements GYAccountsFacade {
    private @Value("${gycoding.accounts.url}") String URL;

    @Override
    public void addChat(String token, String chatId, Boolean isAdmin) {
        final var headers = new HashMap<String, String>();

        headers.put("token", "Bearer " + token);
        headers.put("Content-Type", "application/json");

        var chat = GYAccountsChatDTO.builder()
                .chatId(chatId)
                .admin(isAdmin)
                .build();

        UnirestFacade.put(URL + "/auth/messages/chat/add", headers, chat.toString());
    }

    @Override
    public void removeChat(String token, UUID chatId) {
        final var headers = new HashMap<String, String>();

        headers.put("token", "Bearer " + token);
        headers.put("Content-Type", "application/json");

        UnirestFacade.delete(URL + "/auth/messages/chat/remove", headers, String.format("{\"chatId\": \"%s\"}", chatId.toString()));
    }

    @Override
    public List<GYAccountsChatDTO> listChats(String token) throws APIException {
        final var headers = new HashMap<String, String>();

        headers.put("token", "Bearer " + token);
        headers.put("Content-Type", "application/json");

        var response = UnirestFacade.get(URL + "/auth/messages/chat/list", headers);
        var parser = new JSONParser();
        var chats = new ArrayList<GYAccountsChatDTO>();

        try {
            for (Object obj : (JSONArray) parser.parse(response.getBody())) {
                JSONObject jsonObject   = (JSONObject) obj;

                String chatId           = (String) jsonObject.get("chatId");
                boolean name            = (boolean) jsonObject.get("isAdmin");

                var chat = GYAccountsChatDTO.builder()
                        .chatId(chatId)
                        .admin(name)
                        .build();

                chats.add(chat);
            }

            return chats;
        } catch (ParseException e) {
            throw new APIException(
                    ChatAPIError.JSON_COULD_NOT_BE_PARSED.getCode(),
                    ChatAPIError.JSON_COULD_NOT_BE_PARSED.getMessage(),
                    ChatAPIError.JSON_COULD_NOT_BE_PARSED.getStatus()
            );
        }
    }
}
