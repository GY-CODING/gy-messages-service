package org.gycoding.messages.infrastructure.api;

import org.gycoding.messages.application.service.auth.AuthService;
import org.gycoding.messages.application.service.chat.ChatService;
import org.gycoding.messages.infrastructure.dto.ChatRQDTO;
import org.gycoding.messages.infrastructure.dto.MessageRQDTO;
import org.gycoding.exceptions.model.APIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class ChatController {
    @Autowired
    private ChatService chatService = null;

    @Autowired
    private AuthService authService = null;

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody ChatRQDTO chatRQDTO,
            @RequestHeader String token
    ) throws APIException {
        return ResponseEntity.ok(chatService.create(chatRQDTO, token).toString());
    }

    @DeleteMapping("/{chatId}/delete")
    public ResponseEntity<?> delete(
            @PathVariable UUID chatId,
            @RequestHeader String token
    ) throws APIException {
        chatService.delete(chatId, token);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{chatId}/leave")
    public ResponseEntity<?> leave(
            @PathVariable UUID chatId,
            @RequestHeader String token
    ) throws APIException {
        chatService.leave(chatId, token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{chatId}/send")
    public ResponseEntity<?> sendMessage(
            @PathVariable UUID chatId,
            @RequestBody MessageRQDTO messageRQDTO,
            @RequestHeader String token
    ) throws APIException {
        return ResponseEntity.ok(chatService.sendMessage(chatId, messageRQDTO.message(), token));
    }

    @GetMapping("/{chatId}/get")
    public ResponseEntity<?> getChat(
            @PathVariable UUID chatId,
            @RequestHeader String token
    ) throws APIException {
        return ResponseEntity.ok(chatService.getChat(chatId, authService.decode(token)).toString());
    }

    @GetMapping("/{chatId}/members/list")
    public ResponseEntity<?> listMembers(
            @PathVariable UUID chatId,
            @RequestHeader String token
    ) throws APIException {
        return ResponseEntity.ok(chatService.listMembers(chatId, token).toString());
    }

    @PostMapping("/{chatId}/members/add")
    public ResponseEntity<?> addMember(
            @PathVariable UUID chatId,
            @RequestHeader String token
    ) throws APIException {
        chatService.addMember(chatId, token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{chatId}/messages/list")
    public ResponseEntity<?> listMessages(
            @PathVariable UUID chatId,
            @RequestHeader String token
    ) throws APIException {
        return ResponseEntity.ok(chatService.listMessages(chatId, token).toString());
    }

    @GetMapping("/chats/list")
    public ResponseEntity<?> listChats(
            @RequestHeader String token
    ) throws APIException {
        return ResponseEntity.ok(chatService.listChats(token).toString());
    }
}