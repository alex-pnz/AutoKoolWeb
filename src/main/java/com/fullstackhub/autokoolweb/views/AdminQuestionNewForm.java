package com.fullstackhub.autokoolweb.views;

import com.fullstackhub.autokoolweb.models.Question;
import com.fullstackhub.autokoolweb.services.NotificationService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.fullstackhub.autokoolweb.constants.StringConstants.*;

public class AdminQuestionNewForm extends FormLayout {
    TextField question = new TextField(ADMIN_QUESTION_FIELD);
    TextField option1 = new TextField(ADMIN_QUESTION_OPTION1);
    TextField option2 = new TextField(ADMIN_QUESTION_OPTION2);
    TextField option3 = new TextField(ADMIN_QUESTION_OPTION3);
    Checkbox answer1 = new Checkbox();
    Checkbox answer2 = new Checkbox();
    Checkbox answer3 = new Checkbox();
    Button save = new Button(ADMIN_QUESTION_NEW_BUTTON);
    private static final Logger logger = LoggerFactory.getLogger(AdminQuestionNewForm.class);
    private final NotificationService notificationService;
    public AdminQuestionNewForm(NotificationService notificationService) {
        this.notificationService = notificationService;

        addClassName("admin-question-edit-form");

        add(
                new VerticalLayout(
                        setOptions(),
                        setButtons()
                )
        );
        question.setClassName("user-edit");
        option1.setClassName("user-edit");
        option2.setClassName("user-edit");
        option3.setClassName("user-edit");
    }

    private Component setOptions() {
        HorizontalLayout horizontalLayout = new HorizontalLayout(question);
        HorizontalLayout horizontalLayout1 = new HorizontalLayout(option1, answer1);
        HorizontalLayout horizontalLayout2 = new HorizontalLayout(option2, answer2);
        HorizontalLayout horizontalLayout3 = new HorizontalLayout(option3, answer3);
        horizontalLayout1.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        horizontalLayout2.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        horizontalLayout3.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout, horizontalLayout1, horizontalLayout2, horizontalLayout3);
        verticalLayout.setClassName("vert-layout-margin");
        return verticalLayout;
    }

    private Component setButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> saveNewQuestion());
        save.addClickShortcut(Key.ENTER);

        return new HorizontalLayout(save);
    }

    private void saveNewQuestion() {
        try {
            Question questionIn = null;
            if (!question.isEmpty() && !option1.isEmpty() && !option2.isEmpty()) {
                questionIn = new Question();
                questionIn.setQuestion(question.getValue());
                questionIn.setOption1(option1.getValue());
                questionIn.setOption2(option2.getValue());
                questionIn.setAnswer1(answer1.getValue() ? 1 : 0);
                questionIn.setAnswer2(answer2.getValue() ? 1 : 0);

                if (option3.getValue() != null) {
                    questionIn.setOption3(option3.getValue());
                    if (answer3.getValue()) {
                        questionIn.setAnswer3(1);
                    } else {
                        questionIn.setAnswer3(0);
                    }
                }
            }

            fireEvent(new AdminQuestionNewForm.SaveEvent(this, questionIn));
        } catch (NullPointerException e) {
            notificationService.showNotification(NOTIFICATION_RED, ADMIN_QUESTION_NEW_ENTER_INFO);
            logger.error(e.getMessage());
        }
    }

    public static abstract class AdminQuestionNewFormEvent extends ComponentEvent<AdminQuestionNewForm> {
        private Question question;

        protected AdminQuestionNewFormEvent(AdminQuestionNewForm source, Question question) {
            super(source, false);
            this.question = question;
        }

        public Question getQuestion() {
            return question;
        }
    }

    public static class SaveEvent extends AdminQuestionNewFormEvent {
        SaveEvent(AdminQuestionNewForm source, Question question) {
            super(source, question);
        }
    }

    public Registration addSaveListener(ComponentEventListener<AdminQuestionNewForm.SaveEvent> listener) {
        return addListener(AdminQuestionNewForm.SaveEvent.class, listener);
    }

    public void clear() {
        question.clear();
        option1.clear();
        option2.clear();
        option3.clear();
        answer1.clear();
        answer2.clear();
        answer3.clear();
    }
}
