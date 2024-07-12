package org.gycoding.accounts.infrastructure.external.gyaccounts;

import org.gycoding.accounts.domain.enums.ServerStatus;
import org.gycoding.accounts.domain.exceptions.ChatAPIException;
import org.gycoding.accounts.infrastructure.dto.GYAccountsChatDTO;
import org.gycoding.accounts.infrastructure.external.unirest.UnirestFacade;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GYAccountsFacadeImpl implements GYAccountsFacade {
    private final static String URL = "https://gy-accounts-gy-coding.koyeb.app";

    @Override
    public void addChat(String jwt, String chatId, Boolean isAdmin) {
        var chat = GYAccountsChatDTO.builder()
                .chatId(chatId)
                .admin(isAdmin)
                .build();

        System.out.println(chat.toString());

        UnirestFacade.put(URL + "/auth/messages/chat/add", jwt, chat.toString());
    }

    @Override
    public void removeChat(String userId, UUID chatId) {
        UnirestFacade.delete(URL + "/auth/messages/chat/remove", userId, String.format("{\"chatId\": \"%s\"}", chatId.toString()));
    }

    @Override
    public List<GYAccountsChatDTO> listChats(String jwt) throws ChatAPIException {
        var response = UnirestFacade.get(URL + "/auth/messages/chat/list", jwt);
        var parser = new JSONParser();
        var chats = new ArrayList<GYAccountsChatDTO>();

        try {
            for (Object obj : (JSONArray) parser.parse(response.getBody())) {
                JSONObject jsonObject   = (JSONObject) obj;

                String chatId           = (String) jsonObject.get("chatId");
                Boolean name            = (boolean) jsonObject.get("isAdmin");

                var chat = GYAccountsChatDTO.builder()
                        .chatId(chatId)
                        .admin(name)
                        .build();

                chats.add(chat);
            }

            return chats;
        } catch (ParseException e) {
            throw new ChatAPIException(ServerStatus.JSON_COULD_NOT_BE_PARSED);
        }
    }
}
