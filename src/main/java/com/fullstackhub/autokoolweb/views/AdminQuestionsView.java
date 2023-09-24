package com.fullstackhub.autokoolweb.views;

import com.fullstackhub.autokoolweb.models.Question;
import com.fullstackhub.autokoolweb.services.QuestionAdminViewService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Autokool: Questions")
@Route(value = "admin-questions", layout = AdminLayout.class)
public class AdminQuestionsView extends VerticalLayout {

    private Grid<Question> questionsTable = new Grid<>(Question.class);
    private final QuestionAdminViewService questionAdminViewService;
    private Image image = new Image();
    private AdminQuestionEditForm questionEditForm;
    private AdminQuestionNewForm questionNewForm;
    private static final Logger logger = LoggerFactory.getLogger(AdminQuestionsView.class);
    private Question question;
    MemoryBuffer memoryBuffer = new MemoryBuffer();
    private Upload upload = new Upload(memoryBuffer);
    private Button uploadButton = new Button("Добавить картинку...");

    private List<Question> questionsList = new ArrayList<>();
    Notification notification = new Notification();

    public AdminQuestionsView(QuestionAdminViewService questionAdminViewService) {
        this.questionAdminViewService = questionAdminViewService;
        addClassName("admin-questions-view");
        setSizeFull();
        setGrid();
        upload.setMaxFiles(1);
        upload.setAcceptedFileTypes("application/jpg", ".jpg");
        uploadButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        upload.setUploadButton(uploadButton);
        if (memoryBuffer.getFileData() == null) {
            upload.setVisible(false);
        }

        upload.addFinishedListener(e -> {
            logger.info("MemoryBuffer {}", memoryBuffer.getFileName());

            image.setSrc(createResource());
            image.setVisible(true);
        });

        add(
                questionsTable,
                setTabs()
        );

        reloadQuestionsTable();

    }

    private AbstractStreamResource createResource() {
        return new StreamResource("img.jpg", () -> memoryBuffer.getInputStream());
    }

    private Component setTabs() {
        TabSheet tabSheet = new TabSheet();

        tabSheet.add("Редактировать вопрос",
                new Div(setFormEditLayout()));
        tabSheet.add("Добавить новый воопрос",
                new Div(setFormNewLayout()));

        return tabSheet;
    }

    private Component setFormEditLayout() {
        questionEditForm = new AdminQuestionEditForm();
        questionEditForm.addSaveListener(this::saveQuestion);
        questionEditForm.addDeleteListener(this::deleteQuestion);
        HorizontalLayout horizontalLayout = new HorizontalLayout(questionEditForm, new VerticalLayout(image, upload));
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Component setFormNewLayout(){
        questionNewForm = new AdminQuestionNewForm();
        questionNewForm.addSaveListener(this::saveNewQuestion);

        HorizontalLayout horizontalLayout = new HorizontalLayout(questionNewForm);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        return horizontalLayout;
    }

    private void setImage(Question question) {
//        String path = String.format("themes/autokoolweb/images/%s", question.getImage());
        String path = String.format("C:/Users/Sasha/IdeaProjects/AutoKool/Images/%s", question.getImage());

        StreamResource imageResource = new StreamResource("MyResourceName", () -> {
            try {
                return new FileInputStream(new File(path));
            } catch (final IOException e) {
                e.printStackTrace();
                return null;
            }
        });

        logger.info("Image path: {}", path);
        image.setSrc(imageResource);
    }

    private boolean deleteQuestion(AdminQuestionEditForm.DeleteEvent event) {
        Question tempQuestion = event.getQuestion();
        logger.info("Deleting: Selected Question ID: {}", tempQuestion.getIdquestions());

        boolean deleted = questionAdminViewService.deleteQuestionFromDataBase(tempQuestion);

        if(deleted){
            if (event.getQuestion().getImage() != null){
                String path = String.format("C:/Users/Sasha/IdeaProjects/AutoKool/Images/%s", tempQuestion.getImage());
                File file = new File(path);
                file.delete();
            }

            reloadQuestionsTable();

            if(!questionsList.isEmpty()){
                questionsTable.getSelectionModel().select(questionsList.get(questionsList.size()-1));
            }

            Span green = new Span("Вопрос удален!");
            green.addClassName("green");
            notification.close();
            notification = new Notification(green);
            notification.open();
        }

        return deleted;
    }

    private Question saveQuestion(AdminQuestionEditForm.SaveEvent event) {
        logger.info("Editing: Selected Question ID: {}", event.getQuestion().getIdquestions());
        Question currentQuestion = event.getQuestion();
        if (currentQuestion.getOption3() == null || currentQuestion.getOption3().isBlank()) {

            logger.info("currentQuestion: Option Null or Empty");
            currentQuestion.setOption3(null);
            currentQuestion.setAnswer3(null);
        }

        if (!memoryBuffer.getFileName().isBlank()) {
            currentQuestion.setImage(memoryBuffer.getFileName());
            String path = String.format("C:/Users/Sasha/IdeaProjects/AutoKool/Images/%s", memoryBuffer.getFileName());
            logger.info("MemoryBuffer image name : {}", memoryBuffer.getFileName());
            File file = new File(path);
            try (OutputStream output = new FileOutputStream(file, false)) {
                memoryBuffer.getInputStream().transferTo(output);
            } catch (IOException e) {
                Span red = new Span("Не получилось сохранить файл!");
                red.addClassName("red");
                notification.close();
                notification = new Notification(red);
                notification.open();
                logger.error(e.getMessage());
                return null;
            }
        }
        Question savedQuestion = questionAdminViewService.saveQuestionToDataBase(currentQuestion);
        if(savedQuestion == null) {
            Span red = new Span("Не получилось сохранить вопрос!");
            red.addClassName("red");
            notification.close();
            notification = new Notification(red);
            notification.open();
            return null;
        }
        Span green = new Span("Вопрос сохранен!");
        green.addClassName("green");
        notification.close();
        notification = new Notification(green);
        notification.open();
        reloadQuestionsTable();
        return savedQuestion;
    }


    private Question saveNewQuestion(AdminQuestionNewForm.SaveEvent event) {
        logger.info("Saving new question");
        Question currentQuestion = event.getQuestion();

        Question savedQuestion = questionAdminViewService.saveQuestionToDataBase(currentQuestion);
        if(savedQuestion == null) {
            Span red = new Span("Не получилось сохранить вопрос!");
            red.addClassName("red");
            notification.close();
            notification = new Notification(red);
            notification.open();
            return null;
        }
        Span green = new Span("Вопрос сохранен!");
        green.addClassName("green");
        notification.close();
        notification = new Notification(green);
        notification.open();
        reloadQuestionsTable();
        return savedQuestion;
    }



    private void reloadQuestionsTable() {
        questionsList = questionAdminViewService.getAll();
        logger.info("Questions are not empty: {}", questionsList.stream().findAny().isPresent());
        questionsTable.setItems(questionsList);
    }

    private void setGrid() {
        questionsTable.addClassName("questions-table");
        questionsTable.setSizeFull();

        questionsTable.setColumns("question", "option1", "option2", "option3", "answer1", "answer2", "answer3", "image");

        questionsTable.getColumnByKey("question").setHeader("Вопрос").setWidth("40%");
        questionsTable.getColumnByKey("option1").setHeader("Вариант ответа 1").setWidth("10%");
        questionsTable.getColumnByKey("option2").setHeader("Вариант ответа 2").setWidth("10%");
        questionsTable.getColumnByKey("option3").setHeader("Вариант ответа 3").setWidth("10%");
        questionsTable.getColumnByKey("answer1").setHeader(" ").setTextAlign(ColumnTextAlign.CENTER);
        questionsTable.getColumnByKey("answer2").setHeader(" ").setTextAlign(ColumnTextAlign.CENTER);
        questionsTable.getColumnByKey("answer3").setHeader(" ").setTextAlign(ColumnTextAlign.CENTER);
        questionsTable.getColumnByKey("image").setHeader("Картинка");

        questionsTable.asSingleSelect().addValueChangeListener(question -> setQuestion(question.getValue()));
    }

    private void setQuestion(Question question) {
        if (question != null) {
            if (question.getOption3() == null || question.getOption3().isBlank()) {
                question.setAnswer3(0);
            }
            questionEditForm.setQuestionEditFields(question);
            this.question = question;
            if (question.getImage() == null || question.getImage().isBlank()) {
                image.setVisible(false);
            } else {
                setImage(question);
                image.setVisible(true);
            }
            upload.setVisible(true);
            upload.clearFileList();

            memoryBuffer = new MemoryBuffer();
            upload.setReceiver(memoryBuffer);
            notification.close();
            questionEditForm.notification.close();
            questionNewForm.notification.close();
        }
    }

}
