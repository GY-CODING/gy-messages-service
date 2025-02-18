package org.gycoding.messages.infrastructure.api.controller;

import lombok.AllArgsConstructor;
import org.gycoding.exceptions.model.APIException;
import org.gycoding.messages.application.service.group.GroupService;
import org.gycoding.messages.infrastructure.api.dto.in.GroupRQDTO;
import org.gycoding.messages.infrastructure.api.dto.in.MessageRQDTO;
import org.gycoding.messages.infrastructure.api.mapper.GroupControllerMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/group")
public class GroupController {
    private final GroupService service;

    private final GroupControllerMapper mapper;

    @GetMapping("/{chatId}")
    public ResponseEntity<?> getChat(
            @PathVariable UUID chatId,
            @RequestHeader("x-user-id") String userId
    ) throws APIException {
        return ResponseEntity.ok(mapper.toRSDTO(service.get(userId, chatId)));
    }

    @GetMapping("")
    public ResponseEntity<?> listChats(
            @RequestHeader("x-user-id") String userId
    ) throws APIException {
        return ResponseEntity.ok(service.list(userId).stream().map(mapper::toRSDTO).toList());
    }

    @PostMapping("")
    public ResponseEntity<?> create(
            @RequestBody GroupRQDTO chat,
            @RequestHeader("x-user-id") String userId
    ) throws APIException {
        return ResponseEntity.ok(mapper.toRSDTO(service.create(userId, mapper.toIDTO(chat, userId))));
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<?> delete(
            @PathVariable UUID chatId,
            @RequestHeader("x-user-id") String userId
    ) throws APIException {
        service.delete(userId, chatId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<?> sendMessage(
            @PathVariable UUID chatId,
            @RequestBody MessageRQDTO messageRQDTO,
            @RequestHeader("x-user-id") String userId
    ) throws APIException {
        return ResponseEntity.ok(mapper.toRSDTO(service.sendMessage(userId, chatId, messageRQDTO.message())));
    }

    @GetMapping("/{chatId}/messages/list")
    public ResponseEntity<?> listMessages(
            @PathVariable UUID chatId,
            @RequestHeader("x-user-id") String userId
    ) throws APIException {
        return ResponseEntity.ok(service.listMessages(userId, chatId).stream().map(mapper::toRSDTO).toList());
    }

    // TODO. Paginate messages.

    @GetMapping("/{chatId}/members/list")
    public ResponseEntity<?> listMembers(
            @PathVariable UUID chatId,
            @RequestHeader("x-user-id") String userId
    ) throws APIException {
        return ResponseEntity.ok(service.listMembers(userId, chatId).stream().map(mapper::toRSDTO).toList());
    }

    @PostMapping("/{chatId}/members")
    public ResponseEntity<?> addMember(
            @PathVariable UUID chatId,
            @RequestHeader("x-user-id") String userId
    ) throws APIException {
        service.addMember(userId, chatId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{chatId}/members")
    public ResponseEntity<?> leave(
            @PathVariable UUID chatId,
            @RequestHeader("x-user-id") String userId
    ) throws APIException {
        service.leave(userId, chatId);

        return ResponseEntity.noContent().build();
    }
}