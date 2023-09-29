package com.fullstackhub.autokoolweb.views;

import com.fullstackhub.autokoolweb.dtos.UserAdminViewIn;
import com.fullstackhub.autokoolweb.models.User;
import com.fullstackhub.autokoolweb.services.NotificationService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.fullstackhub.autokoolweb.constants.StringConstants.*;

public class AdminUserEditForm extends FormLayout {
    Binder<UserAdminViewIn> binder = new BeanValidationBinder<>(UserAdminViewIn.class);

    TextField username = new TextField(ADMIN_USER_NAME);
    TextField password = new TextField(ADMIN_USER_PASSWORD);
    ComboBox<User.Role> role = new ComboBox<>(ADMIN_USER_ROLE);
    Button save = new Button(ADMIN_USER_BUTTON_CHANGE);
    Button delete = new Button(ADMIN_USER_BUTTON_DELETE);
    private UserAdminViewIn userAdminViewIn;
    private static final Logger logger = LoggerFactory.getLogger(AdminUserEditForm.class);
    private final NotificationService notificationService;
    public AdminUserEditForm(NotificationService notificationService) {
        this.notificationService = notificationService;
        addClassName("admin-user-edit-form");
        binder.bindInstanceFields(this);
        role.setItems(User.Role.values());
        role.setItemLabelGenerator(User.Role::name);
        add(
                new VerticalLayout(
                        username,
                        password,
                        role,
                        setButtons()
                )
        );
        username.setClassName("user-edit");
        password.setClassName("user-edit");
    }

    public void setUserEditFields(UserAdminViewIn userAdminViewIn){
        this.userAdminViewIn = userAdminViewIn;
        binder.readBean(userAdminViewIn);
    }

    private Component setButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickListener(event -> saveUser());
        delete.addClickListener(event -> deleteUser());

        save.addClickShortcut(Key.ENTER);
        delete.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save,delete);
    }

    private void deleteUser() {
        try{
            binder.writeBean(userAdminViewIn);
            fireEvent(new DeleteEvent(this, userAdminViewIn));
        } catch (NullPointerException e){
            notificationService.showNotification(NOTIFICATION_RED, ADMIN_USER_CHOOSE_STUDENT);
            logger.error(e.getMessage());
        } catch (ValidationException e) {
            logger.error(e.getMessage());
        }
    }

    private void saveUser() {
        try {
            binder.writeBean(userAdminViewIn);
            fireEvent(new SaveEvent(this, userAdminViewIn));
        } catch (NullPointerException e){
            notificationService.showNotification(NOTIFICATION_RED, ADMIN_USER_CHOOSE_STUDENT);
            logger.error(e.getMessage());
        } catch (ValidationException e) {
            logger.error(e.getMessage());
        }
    }

    public static abstract class AdminUserEditFormEvent extends ComponentEvent<AdminUserEditForm> {
        private UserAdminViewIn userAdminViewIn;

        protected AdminUserEditFormEvent(AdminUserEditForm source, UserAdminViewIn userAdminViewIn) {
            super(source, false);
            this.userAdminViewIn = userAdminViewIn;
        }

        public User getUser() {
            User user = new User();
            user.setId(userAdminViewIn.getId());
            user.setUsername(userAdminViewIn.getUsername());
            user.setPassword(userAdminViewIn.getPassword());
            user.setRole(userAdminViewIn.getRole());
            return user;
        }
    }

    public static class SaveEvent extends AdminUserEditFormEvent {
        SaveEvent(AdminUserEditForm source, UserAdminViewIn userAdminViewIn) {
            super(source, userAdminViewIn);
        }
    }

    public static class DeleteEvent extends AdminUserEditFormEvent {
        DeleteEvent(AdminUserEditForm source, UserAdminViewIn userAdminViewIn) {
            super(source, userAdminViewIn);
        }

    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

}
