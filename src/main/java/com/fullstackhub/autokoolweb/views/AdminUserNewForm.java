package com.fullstackhub.autokoolweb.views;

import com.fullstackhub.autokoolweb.dtos.UserAdminViewIn;
import com.fullstackhub.autokoolweb.models.User;
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

public class AdminUserNewForm extends FormLayout {
    Binder<User> binder = new BeanValidationBinder<>(User.class);

    TextField usernameNew = new TextField("Имя");
    TextField passwordNew = new TextField("Пароль");
    ComboBox<User.Role> roleNew = new ComboBox<>("Роль");
    Button saveNew = new Button("Добавить");
    private User user;
    Notification notification = new Notification();
    private static final Logger logger = LoggerFactory.getLogger(AdminUserNewForm.class);
    public AdminUserNewForm() {

        binder.forField(usernameNew).bind(User::getUsername, User::setUsername);
        binder.forField(passwordNew).bind(User::getPassword, User::setPassword);
        binder.forField(roleNew).bind(User::getRole, User::setRole);
        roleNew.setItems(User.Role.values());
        roleNew.setItemLabelGenerator(User.Role::name);
        roleNew.setValue(User.Role.USER);

        add(
                new VerticalLayout(
                        usernameNew,
                        passwordNew,
                        roleNew,
                        setButtons()
                )
        );

        usernameNew.setClassName("user-edit");
        passwordNew.setClassName("user-edit");
    }

    private Component setButtons() {
        saveNew.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveNew.addClickListener(event -> saveNewUser());
        saveNew.addClickShortcut(Key.ENTER);

        return new HorizontalLayout(saveNew);
    }

    private void saveNewUser() {

        this.user = new User(usernameNew.getValue(),passwordNew.getValue(),roleNew.getValue());
        binder.readBean(user);

        try {
            binder.writeBean(user);
            fireEvent(new AdminUserNewForm.SaveEvent(this, user));
        } catch (ValidationException e) {
            logger.error(e.getMessage());
            Span red = new Span("Пользователь не сохранен!");
            red.addClassName("red");
            notification.close();
            notification = new Notification(red);
            notification.open();
        }
    }

    public static abstract class AdminUserNewFormEvent extends ComponentEvent<AdminUserNewForm> {
        private User user;

        protected AdminUserNewFormEvent(AdminUserNewForm source, User user) {
            super(source, false);
            this.user = user;
        }

        public User getUser() {
            User userOut = new User();
            userOut.setId(user.getId());
            userOut.setUsername(user.getUsername());
            userOut.setPassword(user.getPassword());
            userOut.setRole(user.getRole());
            return userOut;
        }
    }

    public static class SaveEvent extends AdminUserNewForm.AdminUserNewFormEvent {
        SaveEvent(AdminUserNewForm source, User user) {
            super(source, user);
        }
    }

    public Registration addSaveListener(ComponentEventListener<AdminUserNewForm.SaveEvent> listener) {
        return addListener(AdminUserNewForm.SaveEvent.class, listener);
    }

}