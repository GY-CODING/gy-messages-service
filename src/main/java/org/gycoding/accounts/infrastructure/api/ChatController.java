package org.gycoding.accounts.infrastructure.api;

import org.gycoding.accounts.application.service.auth.AuthService;
import org.gycoding.accounts.application.service.chat.ChatService;
import org.gycoding.accounts.domain.exceptions.ChatAPIException;
import org.gycoding.accounts.infrastructure.dto.ChatRQDTO;
import org.gycoding.accounts.infrastructure.dto.MessageRQDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
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
        return ResponseEntity.ok(chatService.create(chatRQDTO, authService.decode(jwt)).toString());
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(
            @RequestBody UUID chatId,
            @RequestHeader String jwt
    ) throws ChatAPIException {
        chatService.delete(chatId, authService.decode(jwt));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{chatId}/send")
    public ResponseEntity<?> sendMessage(
            @PathVariable UUID chatId,
            @RequestBody MessageRQDTO messageRQDTO,
            @RequestHeader String jwt
    ) throws ChatAPIException {
        return ResponseEntity.ok(chatService.sendMessage(chatId, messageRQDTO.message(), authService.decode(jwt)));
    }

    @GetMapping("/{chatId}/list")
	public ResponseEntity<?> list(
            @PathVariable UUID chatId,
            @RequestHeader String jwt
    ) throws ChatAPIException {
        return ResponseEntity.ok(chatService.listMessages(chatId, authService.decode(jwt)).toString());
	}
}