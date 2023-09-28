package com.fullstackhub.autokoolweb.views;

import com.fullstackhub.autokoolweb.dtos.UserAdminViewIn;
import com.fullstackhub.autokoolweb.models.Question;
import com.fullstackhub.autokoolweb.models.User;
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
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminQuestionEditForm extends FormLayout {
    Binder<Question> binder = new BeanValidationBinder<>(Question.class);

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
    private Question questionIn;
    private static final Logger logger = LoggerFactory.getLogger(AdminQuestionEditForm.class);

    public AdminQuestionEditForm() {
        addClassName("admin-question-edit-form");
        binder.forField(question).bind(Question::getQuestion,Question::setQuestion);
        binder.forField(option1).bind(Question::getOption1,Question::setOption1);
        binder.forField(option2).bind(Question::getOption2,Question::setOption2);
        binder.forField(option3).bind(Question::getOption3,Question::setOption3);
        binder.forField(answer1).withConverter(i->i?1:0,i->i==1).bind(Question::getAnswer1,Question::setAnswer1);
        binder.forField(answer2).withConverter(i->i?1:0,i->i==1).bind(Question::getAnswer2,Question::setAnswer2);
        binder.forField(answer3).withConverter(i->i?1:0,i->i==1).bind(Question::getAnswer3,Question::setAnswer3);
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

    public void setQuestionEditFields(Question questionIn){

        this.questionIn = questionIn;
        binder.readBean(questionIn);
        if (option3.isEmpty()) {
            answer3.setValue(false);
        }

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
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickListener(event -> saveQuestion());
        delete.addClickListener(event -> deleteQuestion());

        save.addClickShortcut(Key.ENTER);
        delete.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save,delete);
    }

    private void deleteQuestion() {
        try{
            binder.writeBean(questionIn);
            fireEvent(new AdminQuestionEditForm.DeleteEvent(this, questionIn));

        } catch (NullPointerException e){
            Span red = new Span("Выберите вопрос из списка!");
            red.addClassName("red");
            notification.close();
            notification = new Notification(red);
            notification.open();
            logger.error(e.getMessage());
        } catch (ValidationException e) {
            logger.error(e.getMessage());
        }
    }

    private void saveQuestion() {
        try {
            binder.writeBean(questionIn);
            fireEvent(new AdminQuestionEditForm.SaveEvent(this, questionIn));
        } catch (NullPointerException e){
            Span red = new Span("Выберите вопрос из списка!");
            red.addClassName("red");
            notification.close();
            notification = new Notification(red);
            notification.open();
            logger.error(e.getMessage());
        } catch (ValidationException e) {
            logger.error(e.getMessage());
        }
    }

    public static abstract class AdminQuestionEditFormEvent extends ComponentEvent<AdminQuestionEditForm> {
        private Question question;

        protected AdminQuestionEditFormEvent(AdminQuestionEditForm source, Question question) {
            super(source, false);
            this.question = question;
        }

        public Question getQuestion() {
            return question;
        }
    }

    public static class SaveEvent extends AdminQuestionEditForm.AdminQuestionEditFormEvent {
        SaveEvent(AdminQuestionEditForm source, Question question) {
            super(source, question);
        }
    }

    public static class DeleteEvent extends AdminQuestionEditForm.AdminQuestionEditFormEvent {
        DeleteEvent(AdminQuestionEditForm source, Question question) {
            super(source, question);
        }

    }

    public Registration addSaveListener(ComponentEventListener<AdminQuestionEditForm.SaveEvent> listener) {
        return addListener(AdminQuestionEditForm.SaveEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<AdminQuestionEditForm.DeleteEvent> listener) {
        return addListener(AdminQuestionEditForm.DeleteEvent.class, listener);
    }
}
