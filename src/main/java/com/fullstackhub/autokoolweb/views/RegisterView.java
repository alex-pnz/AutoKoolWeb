package com.fullstackhub.autokoolweb.views;

import com.fullstackhub.autokoolweb.security.SecurityService;
import com.fullstackhub.autokoolweb.services.NotificationService;
import com.fullstackhub.autokoolweb.services.RegisterService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import static com.fullstackhub.autokoolweb.constants.StringConstants.*;

@AnonymousAllowed
@Route(value = REGISTER_VIEW_URL)
public class RegisterView extends VerticalLayout {

    private final NotificationService notificationService;
    private final RegisterService registerService;
    
    public RegisterView(NotificationService notificationService,
                        RegisterService registerService){
        this.notificationService = notificationService;
        this.registerService = registerService;
        
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        TextField usernameField = new TextField(REGISTER_USERNAME_TEXT);
        TextField passwordField = new TextField(REGISTER_PASSWORD_TEXT);
        TextField confirmPasswordField = new TextField(REGISTER_CONFIRM_PASSWORD_TEXT);
        add(
                new H2(REGISTER_TEXT),
                usernameField,
                passwordField,
                confirmPasswordField,
                new Button(REGISTER_TEXT, event-> register(
                        usernameField.getValue(),
                        passwordField.getValue(),
                        confirmPasswordField.getValue()
                ))
        );
    }

    private void register(String usernameField, String passwordField, String confirmPasswordField) {
        if(usernameField.isBlank()){
            notificationService.showNotification(NOTIFICATION_RED, REGISTER_ENTER_USERNAME);
        } else if (passwordField.isBlank()){
            notificationService.showNotification(NOTIFICATION_RED, REGISTER_ENTER_PASSWORD);
        } else if (!passwordField.equals(confirmPasswordField)) {
            notificationService.showNotification(NOTIFICATION_RED, REGISTER_MATCH_PASSWORD);
        } else {
            int check = registerService.registerUser(usernameField, passwordField);
            if (check == 0){
                notificationService.showNotification(NOTIFICATION_GREEN, REGISTER_SUCCESS);
                this.getUI().get().navigate(LOGIN_VIEW_URL);
            } else if (check == 1) {
                notificationService.showNotification(NOTIFICATION_RED, REGISTER_USERNAME_EXISTS);
            } else {
                notificationService.showNotification(NOTIFICATION_RED, REGISTER_FAIL);
            }
        }
    }
}
