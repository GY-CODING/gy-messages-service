package org.gycoding.accounts.application.service.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate template;

    public void notify(String message) {
        template.convertAndSend("/messages", message);
    }
}