package org.gycoding.messages.application.service.group;

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
import org.gycoding.messages.domain.repository.GroupRepository;
import org.gycoding.messages.domain.repository.GYAccountsFacade;
import org.gycoding.messages.infrastructure.external.gynotifications.NotificationFacadeImpl;
import org.gycoding.messages.shared.MessageStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public GroupODTO create(String userId, GroupIDTO group) throws APIException {
            final var groupChat = mapper.toMO(group);

            gyNotificationsFacade.notify(groupChat.chatId().toString());
            gyAccountsFacade.addChat(userId, groupChat.chatId(), Boolean.TRUE);

            final var savedGroupChat = repository.save(groupChat).orElseThrow(() ->
                    new APIException(
                        ChatAPIError.CONFLICT.getCode(),
                        ChatAPIError.CONFLICT.getMessage(),
                        ChatAPIError.CONFLICT.getStatus()
                    )
            );

            return mapper.toODTO(savedGroupChat);
    }

    @Override
    public void delete(String userId, UUID chatId) throws APIException {
        var userIsMember    = false;

        try {
            for(MemberMO memberMO : repository.listMembers(chatId)) {
                if(!this.get(userId, chatId).owner().equals(userId)) {
                    throw new APIException(
                            ChatAPIError.FORBIDDEN.getCode(),
                            ChatAPIError.FORBIDDEN.getMessage(),
                            ChatAPIError.FORBIDDEN.getStatus()
                    );
                }

                if(memberMO.userId().equals(userId)) {
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

            for(MemberMO memberMO : repository.listMembers(chatId)) {
                gyAccountsFacade.removeChat(userId, chatId);
            }

            gyNotificationsFacade.notify(chatId.toString());
            repository.delete(chatId);
        } catch(NullPointerException e) {
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
            repository.addMember(chatId, member);
        } catch(Exception e) {
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
                throw new APIException(
                        ChatAPIError.FORBIDDEN.getCode(),
                        ChatAPIError.FORBIDDEN.getMessage(),
                        ChatAPIError.FORBIDDEN.getStatus()
                );
            }

            return repository.listMembers(chatId).stream().map(mapper::toODTO).toList();
        } catch(Exception e) {
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
                    memberMOFound = memberMO;
                    break;
                }
            }

            if(memberMOFound == null) {
                throw new APIException(
                        ChatAPIError.FORBIDDEN.getCode(),
                        ChatAPIError.FORBIDDEN.getMessage(),
                        ChatAPIError.FORBIDDEN.getStatus()
                );
            }

            gyNotificationsFacade.notify(chatId.toString());
            gyAccountsFacade.removeChat(userId, chatId);
            repository.removeMember(chatId, memberMOFound);
        } catch(Exception e) {
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

            var message = MessageMO.builder()
                    .author(userId)
                    .content(content)
                    .date(OffsetDateTime.now().format(MessageMO.DATE_FORMAT))
                    .state(MessageStates.SENT)
                    .build();

            gyNotificationsFacade.notify(chatId.toString());

            return mapper.toODTO(repository.sendMessage(chatId, message));
        } catch(NullPointerException e) {
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

            return repository.listMessages(chatId).stream().map(mapper::toODTO).toList();
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.RESOURCE_NOT_FOUND.getCode(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getMessage(),
                    ChatAPIError.RESOURCE_NOT_FOUND.getStatus()
            );
        }
    }
}
