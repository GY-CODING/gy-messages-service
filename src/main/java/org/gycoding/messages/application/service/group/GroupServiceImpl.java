package org.gycoding.messages.application.service.group;

import kong.unirest.json.JSONObject;
import lombok.AllArgsConstructor;
import org.gycoding.exceptions.model.APIException;
import org.gycoding.messages.application.dto.in.group.GroupIDTO;
import org.gycoding.messages.application.dto.out.MessageODTO;
import org.gycoding.messages.application.dto.out.group.GroupODTO;
import org.gycoding.messages.application.dto.out.group.MemberODTO;
import org.gycoding.messages.application.mapper.GroupServiceMapper;
import org.gycoding.messages.domain.exceptions.ChatAPIError;
import org.gycoding.messages.domain.model.MessageMO;
import org.gycoding.messages.domain.model.group.MemberMO;
import org.gycoding.messages.domain.repository.GYAccountsFacade;
import org.gycoding.messages.domain.repository.GroupRepository;
import org.gycoding.messages.infrastructure.external.gynotifications.NotificationFacadeImpl;
import org.gycoding.messages.shared.MessageStates;
import org.gycoding.messages.shared.utils.logger.Logger;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository repository;

    private final GroupServiceMapper mapper;

    private final GYAccountsFacade gyAccountsFacade;

    private final NotificationFacadeImpl gyNotificationsFacade;

    @Override
    public GroupODTO get(String userId, UUID chatId) throws APIException {
        var userIsMember    = false;

            for(MemberMO memberMO : repository.listMembers(chatId)) {
                if(memberMO.userId().equals(userId)) {
                    Logger.info("User has been confirmed as a member of the specified chat.", new JSONObject().put("userId", userId));

                    userIsMember = true;
                    break;
                }
            }

            if(!userIsMember) {
                Logger.error("User is not a member of the chat.", new JSONObject().put("chatId", chatId).put("userId", userId));

                throw new APIException(
                        ChatAPIError.FORBIDDEN.getCode(),
                        ChatAPIError.FORBIDDEN.getMessage(),
                        ChatAPIError.FORBIDDEN.getStatus()
                );
            }

            final var group = repository.get(chatId).orElseThrow(() ->
                    new APIException(
                        ChatAPIError.RESOURCE_NOT_FOUND.getCode(),
                        ChatAPIError.RESOURCE_NOT_FOUND.getMessage(),
                        ChatAPIError.RESOURCE_NOT_FOUND.getStatus()
                    )
            );

            return mapper.toODTO(group);
    }

    @Override
    public List<GroupODTO> list(String userId) throws APIException {
        final var chatIDs = gyAccountsFacade.listChats(userId);

        return chatIDs.stream()
                .map(chatId -> repository.get(chatId).orElse(null))
                .map(mapper::toODTO)
                .toList();
    }

    @Override
    public GroupODTO create(String userId, GroupIDTO group) throws APIException {
        final var groupChat = mapper.toMO(group);

        gyNotificationsFacade.notify(groupChat.chatId().toString());
        gyAccountsFacade.addChat(userId, groupChat.chatId(), Boolean.TRUE);

        Logger.info("Chat added to user metadata.", new JSONObject().put("chatId", group.chatId()).put("userId", userId));

        final var savedGroupChat = repository.save(groupChat).orElseThrow(() ->
            new APIException(
                ChatAPIError.CONFLICT.getCode(),
                ChatAPIError.CONFLICT.getMessage(),
                ChatAPIError.CONFLICT.getStatus()
            )
        );

        Logger.info("Chat saved on the database.", new JSONObject().put("chatId", group.chatId()).put("userId", userId));

        return mapper.toODTO(savedGroupChat);
    }

    @Override
    public void delete(String userId, UUID chatId) throws APIException {
        var userIsMember    = false;

        try {
            for(MemberMO memberMO : repository.listMembers(chatId)) {
                if(!this.get(userId, chatId).owner().equals(userId)) {
                    Logger.error("User is not the owner of this chat, and so is not allowed to remove it.", new JSONObject().put("chatId", chatId).put("userId", userId));

                    throw new APIException(
                            ChatAPIError.FORBIDDEN.getCode(),
                            ChatAPIError.FORBIDDEN.getMessage(),
                            ChatAPIError.FORBIDDEN.getStatus()
                    );
                }

                if(memberMO.userId().equals(userId)) {
                    Logger.info("User has been confirmed as a member of the specified chat.", new JSONObject().put("userId", userId));

                    userIsMember = true;
                    break;
                }
            }

            if(!userIsMember) {
                Logger.error("User is not a member of the chat.", new JSONObject().put("chatId", chatId).put("userId", userId));

                throw new APIException(
                        ChatAPIError.FORBIDDEN.getCode(),
                        ChatAPIError.FORBIDDEN.getMessage(),
                        ChatAPIError.FORBIDDEN.getStatus()
                );
            }

            for(MemberMO memberMO : repository.listMembers(chatId)) {
                gyAccountsFacade.removeChat(userId, chatId);
            }

            Logger.info("Chat has been successfully removed from all of their members' metadata.", new JSONObject().put("chatId", chatId));

            gyNotificationsFacade.notify(chatId.toString());
            repository.delete(chatId);
        } catch(NullPointerException e) {
            Logger.error("Chat could not be found.", new JSONObject().put("chatId", chatId));

            throw new APIException(
                    ChatAPIError.RESOURCE_NOT_FOUND.getCode(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getMessage(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public void addMember(String userId, UUID chatId) throws APIException {
        try {
            for(MemberMO memberMO : repository.listMembers(chatId)) {
                if(memberMO.userId().equals(userId)) {
                    Logger.error("User is already a member of the chat.", new JSONObject().put("chatId", chatId).put("userId", userId));

                    throw new APIException(
                            ChatAPIError.CONFLICT.getCode(),
                            ChatAPIError.CONFLICT.getMessage(),
                            ChatAPIError.CONFLICT.getStatus()
                    );
                }
            }

            var member = MemberMO.builder()
                    .userId(userId)
                    .build();

            gyAccountsFacade.addChat(userId, chatId, Boolean.FALSE);

            Logger.info("Chat has been successfully set on user's metadata.", new JSONObject().put("chatId", chatId).put("userId", userId));

            repository.addMember(chatId, member);

            Logger.info("Member has been added to chat's object on database.", new JSONObject().put("chatId", chatId).put("userId", userId));
        } catch(Exception e) {
            Logger.error("Chat could not be found.", new JSONObject().put("chatId", chatId));

            throw new APIException(
                    ChatAPIError.RESOURCE_NOT_FOUND.getCode(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getMessage(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public List<MemberODTO> listMembers(String userId, UUID chatId) throws APIException {
        var userIsMember = false;

        try {
            for(MemberMO memberMO : repository.listMembers(chatId)) {
                if(memberMO.userId().equals(userId)) {
                    userIsMember = true;
                    break;
                }
            }

            if(!userIsMember) {
                Logger.error("User is not a member of the chat.", new JSONObject().put("chatId", chatId).put("userId", userId));

                throw new APIException(
                        ChatAPIError.FORBIDDEN.getCode(),
                        ChatAPIError.FORBIDDEN.getMessage(),
                        ChatAPIError.FORBIDDEN.getStatus()
                );
            }

            return repository.listMembers(chatId).stream().map(mapper::toODTO).toList();
        } catch(Exception e) {
            Logger.error("Chat could not be found.", new JSONObject().put("chatId", chatId));

            throw new APIException(
                    ChatAPIError.RESOURCE_NOT_FOUND.getCode(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getMessage(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public void leave(String userId, UUID chatId) throws APIException {
        MemberMO memberMOFound = null;

        try {
            for(MemberMO memberMO : repository.listMembers(chatId)) {
                if(memberMO.userId().equals(userId)) {
                    Logger.info("User has been confirmed as a member of the specified chat.", new JSONObject().put("userId", userId));

                    memberMOFound = memberMO;
                    break;
                }
            }

            if(memberMOFound == null) {
                Logger.error("User is not a member of the chat.", new JSONObject().put("chatId", chatId).put("userId", userId));

                throw new APIException(
                        ChatAPIError.FORBIDDEN.getCode(),
                        ChatAPIError.FORBIDDEN.getMessage(),
                        ChatAPIError.FORBIDDEN.getStatus()
                );
            }

            gyNotificationsFacade.notify(chatId.toString());
            gyAccountsFacade.removeChat(userId, chatId);

            Logger.info("Chat has been successfully removed from user's metadata.", new JSONObject().put("chatId", chatId).put("userId", userId));

            repository.removeMember(chatId, memberMOFound);

            Logger.info("User has been successfully removed from the chat as a member.", new JSONObject().put("chatId", chatId).put("userId", userId));
        } catch(Exception e) {
            Logger.error("Chat could not be found.", new JSONObject().put("chatId", chatId));

            throw new APIException(
                    ChatAPIError.RESOURCE_NOT_FOUND.getCode(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getMessage(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public MessageODTO sendMessage(String userId, UUID chatId, String content) throws APIException {
        var userIsMember        = false;

        try {
            for(MemberMO memberMO : repository.listMembers(chatId)) {
                if(memberMO.userId().equals(userId)) {
                    Logger.info("User has been confirmed as a member of the specified chat.", new JSONObject().put("userId", userId));

                    userIsMember = true;
                    break;
                }
            }

            if(!userIsMember) {
                Logger.error("User is not a member of the chat.", new JSONObject().put("chatId", chatId).put("userId", userId));

                throw new APIException(
                        ChatAPIError.FORBIDDEN.getCode(),
                        ChatAPIError.FORBIDDEN.getMessage(),
                        ChatAPIError.FORBIDDEN.getStatus()
                );
            }

            var message = MessageMO.builder()
                    .author(userId)
                    .content(content)
                    .date(OffsetDateTime.now().format(MessageMO.DATE_FORMAT))
                    .state(MessageStates.SENT)
                    .build();

            gyNotificationsFacade.notify(chatId.toString());

            return mapper.toODTO(repository.sendMessage(chatId, message));
        } catch(NullPointerException e) {
            Logger.error("Chat could not be found.", new JSONObject().put("chatId", chatId));

            throw new APIException(
                    ChatAPIError.CONFLICT.getCode(),
                    ChatAPIError.CONFLICT.getMessage(),
                    ChatAPIError.CONFLICT.getStatus()
            );
        }
    }

    @Override
    public List<MessageODTO> listMessages(String userId, UUID chatId) throws APIException {
        var userIsMember    = false;

        try {
            for(MemberMO memberMO : repository.listMembers(chatId)) {
                if(memberMO.userId().equals(userId)) {
                    Logger.info("User has been confirmed as a member of the specified chat.", new JSONObject().put("userId", userId));

                    userIsMember = true;
                    break;
                }
            }

            if(!userIsMember) {
                Logger.error("User is not a member of the chat.", new JSONObject().put("chatId", chatId).put("userId", userId));

                throw new APIException(
                        ChatAPIError.FORBIDDEN.getCode(),
                        ChatAPIError.FORBIDDEN.getMessage(),
                        ChatAPIError.FORBIDDEN.getStatus()
                );
            }

            return repository.listMessages(chatId).stream().map(mapper::toODTO).toList();
        } catch(Exception e) {
            Logger.error("Chat could not be found.", new JSONObject().put("chatId", chatId));

            throw new APIException(
                    ChatAPIError.RESOURCE_NOT_FOUND.getCode(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getMessage(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getStatus()
            );
        }
    }
}
