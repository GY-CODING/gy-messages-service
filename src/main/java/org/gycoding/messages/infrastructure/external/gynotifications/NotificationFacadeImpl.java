package org.gycoding.messages.infrastructure.external.gynotifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationFacadeImpl {
    @Autowired
    private SimpMessagingTemplate template;

    public void notify(String message) {
        template.convertAndSend("/messages", message);
    }
}