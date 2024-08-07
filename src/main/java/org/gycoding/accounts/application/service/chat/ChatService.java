package org.gycoding.accounts.application.service.chat;

import org.gycoding.accounts.application.service.auth.AuthService;
import org.gycoding.accounts.application.service.websocket.NotificationService;
import org.gycoding.accounts.domain.entities.EntityChat;
import org.gycoding.accounts.domain.entities.Member;
import org.gycoding.accounts.domain.entities.Message;
import org.gycoding.accounts.domain.enums.MessageStates;
import org.gycoding.accounts.domain.exceptions.ChatAPIError;
import org.gycoding.accounts.infrastructure.dto.ChatRQDTO;
import org.gycoding.accounts.infrastructure.external.database.service.ChatMongoService;
import org.gycoding.accounts.infrastructure.external.gyaccounts.GYAccountsFacadeImpl;
import org.gycoding.exceptions.model.APIException;
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
    public EntityChat create(ChatRQDTO chatRQDTO, String jwt) throws APIException {
        try {
            var userId = authService.decode(jwt);
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
            gyAccountsFacade.addChat(jwt, chat.chatId(), Boolean.TRUE);
            return chatMongoService.create(chat);
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.CHAT_EXISTS.getCode(),
                    ChatAPIError.CHAT_EXISTS.getMessage(),
                    ChatAPIError.CHAT_EXISTS.getStatus()
            );
        }
    }

    @Override
    public void delete(UUID chatId, String jwt) throws APIException {
        var userId = authService.decode(jwt);
        var userIsMember    = false;

        try {
            for(Member member : chatMongoService.listMembers(chatId)) {
                if(!this.getChat(chatId, jwt).owner().equals(userId)) {
                    throw new APIException(
                            ChatAPIError.USER_NOT_ADMIN.getCode(),
                            ChatAPIError.USER_NOT_ADMIN.getMessage(),
                            ChatAPIError.USER_NOT_ADMIN.getStatus()
                    );
                }

                if(member.userId().equals(userId)) {
                    userIsMember = true;
                    break;
                }
            }

            if(!userIsMember) {
                throw new APIException(
                        ChatAPIError.USER_NOT_MEMBER.getCode(),
                        ChatAPIError.USER_NOT_MEMBER.getMessage(),
                        ChatAPIError.USER_NOT_MEMBER.getStatus()
                );
            }

            for(Member member : chatMongoService.listMembers(chatId)) {
                gyAccountsFacade.removeChat(member.userId(), chatId);
            }

            notificationService.notify(chatId.toString());
            chatMongoService.delete(chatId);
        } catch(NullPointerException e) {
            throw new APIException(
                    ChatAPIError.CHAT_NOT_FOUND.getCode(),
                    ChatAPIError.CHAT_NOT_FOUND.getMessage(),
                    ChatAPIError.CHAT_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public void leave(UUID chatId, String jwt) throws APIException {
        var userId          = authService.decode(jwt);
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
                        ChatAPIError.USER_NOT_MEMBER.getCode(),
                        ChatAPIError.USER_NOT_MEMBER.getMessage(),
                        ChatAPIError.USER_NOT_MEMBER.getStatus()
                );
            }

            notificationService.notify(chatId.toString());
            gyAccountsFacade.removeChat(userId, chatId);
            chatMongoService.removeMember(chatId, memberFound);
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.CHAT_NOT_FOUND.getCode(),
                    ChatAPIError.CHAT_NOT_FOUND.getMessage(),
                    ChatAPIError.CHAT_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public Message sendMessage(UUID chatId, String content, String jwt) throws APIException {
        var userId              = authService.decode(jwt);
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
                        ChatAPIError.USER_NOT_MEMBER.getCode(),
                        ChatAPIError.USER_NOT_MEMBER.getMessage(),
                        ChatAPIError.USER_NOT_MEMBER.getStatus()
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
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.MESSAGE_NOT_SENT.getCode(),
                    ChatAPIError.MESSAGE_NOT_SENT.getMessage(),
                    ChatAPIError.MESSAGE_NOT_SENT.getStatus()
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
                        ChatAPIError.USER_NOT_MEMBER.getCode(),
                        ChatAPIError.USER_NOT_MEMBER.getMessage(),
                        ChatAPIError.USER_NOT_MEMBER.getStatus()
                );
            }

            return chatMongoService.getChat(chatId);
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.CHAT_NOT_FOUND.getCode(),
                    ChatAPIError.CHAT_NOT_FOUND.getMessage(),
                    ChatAPIError.CHAT_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public List<Message> listMessages(UUID chatId, String jwt) throws APIException {
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
                        ChatAPIError.USER_NOT_MEMBER.getCode(),
                        ChatAPIError.USER_NOT_MEMBER.getMessage(),
                        ChatAPIError.USER_NOT_MEMBER.getStatus()
                );
            }

            return chatMongoService.listMessages(chatId);
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.CHAT_NOT_FOUND.getCode(),
                    ChatAPIError.CHAT_NOT_FOUND.getMessage(),
                    ChatAPIError.CHAT_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public void addMember(UUID chatId, String jwt) throws APIException {
        var userId          = authService.decode(jwt);

        try {
            for(Member member : chatMongoService.listMembers(chatId)) {
                if(member.userId().equals(userId)) {
                    throw new APIException(
                            ChatAPIError.USER_ALREADY_MEMBER.getCode(),
                            ChatAPIError.USER_ALREADY_MEMBER.getMessage(),
                            ChatAPIError.USER_ALREADY_MEMBER.getStatus()
                    );
                }
            }

            var member = Member.builder()
                    .userId(userId)
                    .build();

            gyAccountsFacade.addChat(jwt, chatId.toString(), Boolean.FALSE);
            chatMongoService.addMember(chatId, member);
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.CHAT_NOT_FOUND.getCode(),
                    ChatAPIError.CHAT_NOT_FOUND.getMessage(),
                    ChatAPIError.CHAT_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public List<Member> listMembers(UUID chatId, String jwt) throws APIException {
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
                        ChatAPIError.USER_NOT_MEMBER.getCode(),
                        ChatAPIError.USER_NOT_MEMBER.getMessage(),
                        ChatAPIError.USER_NOT_MEMBER.getStatus()
                );
            }

            return chatMongoService.listMembers(chatId);
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.CHAT_NOT_FOUND.getCode(),
                    ChatAPIError.CHAT_NOT_FOUND.getMessage(),
                    ChatAPIError.CHAT_NOT_FOUND.getStatus()
            );
        }
    }

    @Override
    public List<EntityChat> listChats(String jwt) throws APIException {
        try {
            return chatMongoService.listChats(gyAccountsFacade.listChats(jwt));
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.CHAT_NOT_FOUND.getCode(),
                    ChatAPIError.CHAT_NOT_FOUND.getMessage(),
                    ChatAPIError.CHAT_NOT_FOUND.getStatus()
            );
        }
    }
}
