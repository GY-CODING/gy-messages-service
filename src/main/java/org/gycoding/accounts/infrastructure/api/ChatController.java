package org.gycoding.accounts.infrastructure.api;

import org.gycoding.accounts.application.service.auth.AuthService;
import org.gycoding.accounts.application.service.chat.ChatService;
import org.gycoding.accounts.application.service.websocket.NotificationService;
import org.gycoding.accounts.domain.exceptions.ChatAPIException;
import org.gycoding.accounts.infrastructure.dto.ChatRQDTO;
import org.gycoding.accounts.infrastructure.dto.MessageRQDTO;
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
            @RequestHeader String jwt
    ) throws ChatAPIException {
        return ResponseEntity.ok(chatService.create(chatRQDTO, jwt).toString());
    }

    @DeleteMapping("/{chatId}/delete")
    public ResponseEntity<?> delete(
            @PathVariable UUID chatId,
            @RequestHeader String jwt
    ) throws ChatAPIException {
        chatService.delete(chatId, jwt);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{chatId}/leave")
    public ResponseEntity<?> leave(
            @PathVariable UUID chatId,
            @RequestHeader String jwt
    ) throws ChatAPIException {
        chatService.leave(chatId, jwt);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{chatId}/send")
    public ResponseEntity<?> sendMessage(
            @PathVariable UUID chatId,
            @RequestBody MessageRQDTO messageRQDTO,
            @RequestHeader String jwt
    ) throws ChatAPIException {
        return ResponseEntity.ok(chatService.sendMessage(chatId, messageRQDTO.message(), jwt));
    }

    @GetMapping("/{chatId}/get")
    public ResponseEntity<?> getChat(
            @PathVariable UUID chatId,
            @RequestHeader String jwt
    ) throws ChatAPIException {
        return ResponseEntity.ok(chatService.getChat(chatId, authService.decode(jwt)).toString());
    }

    @GetMapping("/{chatId}/members/list")
    public ResponseEntity<?> listMembers(
            @PathVariable UUID chatId,
            @RequestHeader String jwt
    ) throws ChatAPIException {
        return ResponseEntity.ok(chatService.listMembers(chatId, jwt).toString());
    }

    @PostMapping("/{chatId}/members/add")
    public ResponseEntity<?> addMember(
            @PathVariable UUID chatId,
            @RequestHeader String jwt
    ) throws ChatAPIException {
        chatService.addMember(chatId, jwt);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{chatId}/messages/list")
    public ResponseEntity<?> listMessages(
            @PathVariable UUID chatId,
            @RequestHeader String jwt
    ) throws ChatAPIException {
        return ResponseEntity.ok(chatService.listMessages(chatId, jwt).toString());
    }

    @GetMapping("/chats/list")
    public ResponseEntity<?> listChats(
            @RequestHeader String jwt
    ) throws ChatAPIException {
        return ResponseEntity.ok(chatService.listChats(jwt).toString());
    }
}