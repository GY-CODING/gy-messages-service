package org.gycoding.messages.application.service.chat;

import org.gycoding.messages.application.service.auth.AuthService;
import org.gycoding.messages.application.service.websocket.NotificationService;
import org.gycoding.messages.domain.entities.EntityChat;
import org.gycoding.messages.domain.entities.Member;
import org.gycoding.messages.domain.entities.Message;
import org.gycoding.messages.domain.enums.MessageStates;
import org.gycoding.messages.domain.exceptions.ChatAPIError;
import org.gycoding.messages.infrastructure.dto.ChatRQDTO;
import org.gycoding.messages.infrastructure.external.database.service.ChatMongoService;
import org.gycoding.messages.infrastructure.external.gyaccounts.GYAccountsFacadeImpl;
import org.gycoding.exceptions.model.APIException;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService implements ChatRepository {
    @Autowired
    private ChatMongoService chatMongoService = null;

    @Autowired
    private AuthService authService;

    @Autowired
    private GYAccountsFacadeImpl gyAccountsFacade;

    @Autowired
    private NotificationService notificationService;

    @Override
    public EntityChat create(ChatRQDTO chatRQDTO, String token) throws APIException {
        try {
            var userId = authService.decode(token);
            var initialMembers = List.of(Member.builder().userId(userId).build());

            var chat = EntityChat.builder()
                    .chatId(UUID.randomUUID().toString())
                    .name(chatRQDTO.name())
                    .creator(userId)
                    .owner(userId)
                    .members(initialMembers)
                    .messages(List.of())
                    .build();

            notificationService.notify(chat.chatId().toString());
            gyAccountsFacade.addChat(token, chat.chatId(), Boolean.TRUE);
            return chatMongoService.create(chat);
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.CONFLICT.getCode(),
                    ChatAPIError.CONFLICT.getMessage(),
                    ChatAPIError.CONFLICT.getStatus()
            );
        }
    }

    @Override
    public void delete(UUID chatId, String token) throws APIException {
        var userId = authService.decode(token);
        var userIsMember    = false;

        try {
            for(Member member : chatMongoService.listMembers(chatId)) {
                if(!this.getChat(chatId, token).owner().equals(userId)) {
                    throw new APIException(
                            ChatAPIError.FORBIDDEN.getCode(),
                            ChatAPIError.FORBIDDEN.getMessage(),
                            ChatAPIError.FORBIDDEN.getStatus()
                    );
                }

                if(member.userId().equals(userId)) {
                    userIsMember = true;
                    break;
                }
            }

            if(!userIsMember) {
                throw new APIException(
                        ChatAPIError.FORBIDDEN.getCode(),
                        ChatAPIError.FORBIDDEN.getMessage(),
                        ChatAPIError.FORBIDDEN.getStatus()
                );
            }

            for(Member member : chatMongoService.listMembers(chatId)) {
                gyAccountsFacade.removeChat(token, chatId);
            }

            notificationService.notify(chatId.toString());
            chatMongoService.delete(chatId);
        } catch(NullPointerException e) {
            throw new APIException(
                    ChatAPIError.RESOURCE_NOT_FOUND.getCode(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getMessage(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public void leave(UUID chatId, String token) throws APIException {
        var userId          = authService.decode(token);
        Member memberFound  = null;

        try {
            for(Member member : chatMongoService.listMembers(chatId)) {
                if(member.userId().equals(userId)) {
                    memberFound = member;
                    break;
                }
            }

            if(memberFound == null) {
                throw new APIException(
                        ChatAPIError.FORBIDDEN.getCode(),
                        ChatAPIError.FORBIDDEN.getMessage(),
                        ChatAPIError.FORBIDDEN.getStatus()
                );
            }

            notificationService.notify(chatId.toString());
            gyAccountsFacade.removeChat(userId, chatId);
            chatMongoService.removeMember(chatId, memberFound);
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.RESOURCE_NOT_FOUND.getCode(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getMessage(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public Message sendMessage(UUID chatId, String content, String token) throws APIException {
        var userId              = authService.decode(token);
        var userIsMember        = false;

        try {
            for(Member member : chatMongoService.listMembers(chatId)) {
                if(member.userId().equals(userId)) {
                    userIsMember = true;
                    break;
                }
            }

            if(!userIsMember) {
                throw new APIException(
                        ChatAPIError.FORBIDDEN.getCode(),
                        ChatAPIError.FORBIDDEN.getMessage(),
                        ChatAPIError.FORBIDDEN.getStatus()
                );
            }

            var message = Message.builder()
                    .author(userId)
                    .content(content)
                    .date(OffsetDateTime.now().format(Message.DATE_FORMAT))
                    .state(MessageStates.SENT)
                    .build();

            notificationService.notify(chatId.toString());
            return chatMongoService.sendMessage(chatId, message);
        } catch(NullPointerException e) {
            throw new APIException(
                    ChatAPIError.CONFLICT.getCode(),
                    ChatAPIError.CONFLICT.getMessage(),
                    ChatAPIError.CONFLICT.getStatus()
            );
        }
    }

    @Override
    public EntityChat getChat(UUID chatId, String jwt) throws APIException {
        var userId          = authService.decode(jwt);
        var userIsMember    = false;

        try {
            for(Member member : chatMongoService.listMembers(chatId)) {
                if(member.userId().equals(userId)) {
                    userIsMember = true;
                    break;
                }
            }

            if(!userIsMember) {
                throw new APIException(
                        ChatAPIError.FORBIDDEN.getCode(),
                        ChatAPIError.FORBIDDEN.getMessage(),
                        ChatAPIError.FORBIDDEN.getStatus()
                );
            }

            return chatMongoService.getChat(chatId);
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.RESOURCE_NOT_FOUND.getCode(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getMessage(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public List<Message> listMessages(UUID chatId, String token) throws APIException {
        var userId          = authService.decode(token);
        var userIsMember    = false;

        try {
            for(Member member : chatMongoService.listMembers(chatId)) {
                if(member.userId().equals(userId)) {
                    userIsMember = true;
                    break;
                }
            }

            if(!userIsMember) {
                throw new APIException(
                        ChatAPIError.FORBIDDEN.getCode(),
                        ChatAPIError.FORBIDDEN.getMessage(),
                        ChatAPIError.FORBIDDEN.getStatus()
                );
            }

            return chatMongoService.listMessages(chatId);
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.RESOURCE_NOT_FOUND.getCode(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getMessage(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public void addMember(UUID chatId, String token) throws APIException {
        var userId          = authService.decode(token);

        try {
            for(Member member : chatMongoService.listMembers(chatId)) {
                if(member.userId().equals(userId)) {
                    throw new APIException(
                            ChatAPIError.CONFLICT.getCode(),
                            ChatAPIError.CONFLICT.getMessage(),
                            ChatAPIError.CONFLICT.getStatus()
                    );
                }
            }

            var member = Member.builder()
                    .userId(userId)
                    .build();

            gyAccountsFacade.addChat(token, chatId.toString(), Boolean.FALSE);
            chatMongoService.addMember(chatId, member);
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.RESOURCE_NOT_FOUND.getCode(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getMessage(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public List<Member> listMembers(UUID chatId, String token) throws APIException {
        var userId          = authService.decode(token);
        var userIsMember    = false;

        try {
            for(Member member : chatMongoService.listMembers(chatId)) {
                if(member.userId().equals(userId)) {
                    userIsMember = true;
                    break;
                }
            }

            if(!userIsMember) {
                throw new APIException(
                        ChatAPIError.FORBIDDEN.getCode(),
                        ChatAPIError.FORBIDDEN.getMessage(),
                        ChatAPIError.FORBIDDEN.getStatus()
                );
            }

            return chatMongoService.listMembers(chatId);
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.RESOURCE_NOT_FOUND.getCode(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getMessage(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public List<EntityChat> listChats(String token) throws APIException {
        try {
            return chatMongoService.listChats(gyAccountsFacade.listChats(token));
        } catch(ParseException | ClassCastException e) {
            throw new APIException(
                    ChatAPIError.DB_ERROR.getCode(),
                    ChatAPIError.DB_ERROR.getMessage(),
                    ChatAPIError.DB_ERROR.getStatus()
            );
        }
    }
}
