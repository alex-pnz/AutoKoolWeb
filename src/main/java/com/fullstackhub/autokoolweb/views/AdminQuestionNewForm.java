package com.fullstackhub.autokoolweb.views;

import com.fullstackhub.autokoolweb.models.Question;
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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminQuestionNewForm extends FormLayout {
    TextField question = new TextField("Вопрос");
    TextField option1 = new TextField("Вариант ответа 1");
    TextField option2 = new TextField("Вариант ответа 2");
    TextField option3 = new TextField("Вариант ответа 3");
    Checkbox answer1 = new Checkbox();
    Checkbox answer2 = new Checkbox();
    Checkbox answer3 = new Checkbox();
    Button save = new Button("Изменить");
    Button delete = new Button("Удалить");
    Notification notification = new Notification();
    private static final Logger logger = LoggerFactory.getLogger(AdminQuestionNewForm.class);

    public AdminQuestionNewForm() {
        addClassName("admin-question-edit-form");

        add(
                new VerticalLayout(
                        question,
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
        HorizontalLayout horizontalLayout1 = new HorizontalLayout(option1, answer1);
        HorizontalLayout horizontalLayout2 = new HorizontalLayout(option2, answer2);
        HorizontalLayout horizontalLayout3 = new HorizontalLayout(option3, answer3);
        horizontalLayout1.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        horizontalLayout2.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        horizontalLayout3.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        return new VerticalLayout(horizontalLayout1, horizontalLayout2, horizontalLayout3);
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
                questionIn.setAnswer1(answer1.getValue()?1:0);
                questionIn.setAnswer2(answer2.getValue()?1:0);

                if(option3.getValue() != null){
                    questionIn.setOption3(option3.getValue());
                    questionIn.setAnswer3(null);
                }
            }

            fireEvent(new AdminQuestionNewForm.SaveEvent(this, questionIn));
        } catch (NullPointerException e) {
            Span red = new Span("Введите вопрос и варианты ответов!");
            red.addClassName("red");
            notification.close();
            notification = new Notification(red);
            notification.open();
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
}
