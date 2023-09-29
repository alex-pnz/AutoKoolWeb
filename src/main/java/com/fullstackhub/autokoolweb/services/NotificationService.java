package com.fullstackhub.autokoolweb.services;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private Notification notification;
    private Span span;

    public void showNotification(String type, String message) {
        span = new Span(message);
        span.addClassName(type);
        notification = new Notification(span);
        notification.setDuration(2000);
        notification.open();
    }


}
