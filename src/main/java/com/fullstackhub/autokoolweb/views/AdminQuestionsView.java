package com.fullstackhub.autokoolweb.views;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fullstackhub.autokoolweb.models.Question;
import com.fullstackhub.autokoolweb.services.NotificationService;
import com.fullstackhub.autokoolweb.services.QuestionAdminViewService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.fullstackhub.autokoolweb.constants.StringConstants.*;

@PageTitle(ADMIN_QUESTIONS_TITLE)
@Route(value = ADMIN_QUESTIONS_URL, layout = AdminLayout.class)
@RolesAllowed("ADMIN")
public class AdminQuestionsView extends VerticalLayout {

    private Grid<Question> questionsTable = new Grid<>(Question.class);
    private final QuestionAdminViewService questionAdminViewService;
    private Image image = new Image();
    private Image imageNew = new Image();
    private AdminQuestionEditForm questionEditForm;
    private AdminQuestionNewForm questionNewForm;
    private static final Logger logger = LoggerFactory.getLogger(AdminQuestionsView.class);
    private Question question;
    MemoryBuffer memoryBuffer = new MemoryBuffer();
    MemoryBuffer memoryBufferNew = new MemoryBuffer();
    private Upload upload = new Upload(memoryBuffer);
    private Upload uploadNew = new Upload(memoryBufferNew);
    private Button uploadButton = new Button(ADMIN_QUESTIONS_ADD_IMAGE);
    private Button uploadButtonNew = new Button(ADMIN_QUESTIONS_ADD_IMAGE);
    Checkbox imageCheckboxEdit = new Checkbox(ADMIN_QUESTIONS_IMAGE_CHECKBOX);
    Checkbox imageCheckboxNew = new Checkbox(ADMIN_QUESTIONS_IMAGE_CHECKBOX);

    private List<Question> questionsList = new ArrayList<>();
    private final NotificationService notificationService;

    @Value("${cloudinary.secret}")
    private String apiSecret;
    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "diizdixw91",
                "api_key", "438437266359647",
                "api_secret", "mpsj6UbdXp3ZgjmGmwBWPr7yMkA"));

    public AdminQuestionsView(QuestionAdminViewService questionAdminViewService,
                              NotificationService notificationService) {
        this.questionAdminViewService = questionAdminViewService;
        this.notificationService = notificationService;
        addClassName("admin-questions-view");
        setSizeFull();
        setGrid();
        upload.setMaxFiles(1);
        upload.setAcceptedFileTypes("application/jpg", ".jpg");
        uploadButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        upload.setUploadButton(uploadButton);
        uploadNew.setMaxFiles(1);
        uploadNew.setAcceptedFileTypes("application/jpg", ".jpg");
        uploadButtonNew.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        uploadNew.setUploadButton(uploadButtonNew);
        if (memoryBuffer.getFileData() == null) {
            upload.setVisible(false);
        }
        upload.addFinishedListener(e -> {
            logger.info("MemoryBuffer {}", memoryBuffer.getFileName());

            image.setSrc(createResource());
            image.setVisible(true);
        });
        uploadNew.addFinishedListener(e -> {
            logger.info("MemoryBufferNew {}", memoryBufferNew.getFileName());

            imageNew.setSrc(createResourceNew());
        });
        add(
                questionsTable,
                setTabs()
        );
        reloadQuestionsTable();
//        logger.info("cloudiary {}", apiSecret);
    }

    private AbstractStreamResource createResource() {
        return new StreamResource("img.jpg", () -> memoryBuffer.getInputStream());
    }
    private AbstractStreamResource createResourceNew() {
        return new StreamResource("img.jpg", () -> memoryBufferNew.getInputStream());
    }

    private Component setTabs() {
        TabSheet tabSheet = new TabSheet();

        tabSheet.add(ADMIN_QUESTIONS_TAB1,
                new Div(setFormEditLayout()));
        tabSheet.add(ADMIN_QUESTIONS_TAB2,
                new Div(setFormNewLayout()));
        return tabSheet;
    }

    private Component setFormEditLayout() {
        questionEditForm = new AdminQuestionEditForm(notificationService);
        questionEditForm.addSaveListener(this::saveQuestion);
        questionEditForm.addDeleteListener(this::deleteQuestion);
        imageCheckboxEdit.setValue(false);
        HorizontalLayout horizontalLayout = new HorizontalLayout(questionEditForm, new VerticalLayout(image, upload, imageCheckboxEdit));
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Component setFormNewLayout(){
        questionNewForm = new AdminQuestionNewForm(notificationService);
        questionNewForm.addSaveListener(this::saveNewQuestion);
        imageCheckboxNew.setValue(false);
        HorizontalLayout horizontalLayout = new HorizontalLayout(questionNewForm, new VerticalLayout(imageNew, uploadNew, imageCheckboxNew));
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        return horizontalLayout;
    }

    private void setImage(Question question) {

        logger.info("Image path: {}", question.getImage());
        StreamResource imageResource = new StreamResource("MyResourceName", () -> {
            try {
                URL url = new URL(question.getImage());
                return url.openStream();
            } catch (final IOException e) {
                e.printStackTrace();
                return null;
            }
        });
        image.setSrc(imageResource);
    }

    private String getPublicImageId (String path) {
        String tempStr = path
                .replace("https://res.cloudinary.com/diizdixw9/image/upload/","")
                .replace(".jpg", ""); //TODO Implement regex when have time, not urgent

        String[] tempArr = tempStr.split("/");

        return tempArr[1];
    }

    private boolean deleteQuestion(AdminQuestionEditForm.DeleteEvent event) {
        Question tempQuestion = event.getQuestion();
        logger.info("Deleting: Selected Question ID: {}", tempQuestion.getIdquestions());

        boolean deleted = questionAdminViewService.deleteQuestionFromDataBase(tempQuestion);

        if(deleted){
            try {
                cloudinary.uploader().destroy(getPublicImageId(tempQuestion.getImage()), ObjectUtils.emptyMap());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (event.getQuestion().getImage() != null){
                String path = String.format(IMAGE_FOLDER_PATH, tempQuestion.getImage());
                File file = new File(path);
                file.delete();
            }

            reloadQuestionsTable();

            if(!questionsList.isEmpty()){
                questionsTable.getSelectionModel().select(questionsList.get(questionsList.size()-1));
            }

            notificationService.showNotification(NOTIFICATION_GREEN, ADMIN_QUESTIONS_DELETE);
        }

        return deleted;
    }

    private Question saveQuestion(AdminQuestionEditForm.SaveEvent event) {
        logger.info("Editing: Selected Question ID: {}", event.getQuestion().getIdquestions());
        Question currentQuestion = event.getQuestion();
        if (currentQuestion.getOption3() == null || currentQuestion.getOption3().isBlank()) {

            logger.info("currentQuestion: Option3 Null or Empty");
            currentQuestion.setOption3(null);
            currentQuestion.setAnswer3(null);
        }
        if (imageCheckboxEdit.getValue()){
            if (!memoryBuffer.getFileName().isBlank()) {
                logger.info("MemoryBuffer image name : {}", memoryBufferNew.getFileName());
                try{
                    String urlToSave = cloudinary.uploader().upload(memoryBufferNew.getInputStream().readAllBytes(),
                            ObjectUtils.asMap("unique_filename", true)).get("secure_url").toString();
                    logger.info("url of saved to remote file: {}", urlToSave);
                    currentQuestion.setImage(urlToSave);

                } catch (IOException e) {
                    notificationService.showNotification(NOTIFICATION_RED, ADMIN_QUESTIONS_CANT_SAVE);
                    logger.error(e.getMessage());
                    return null;
                }
            }
        }
        Question savedQuestion = questionAdminViewService.saveQuestionToDataBase(currentQuestion);
        if(savedQuestion == null) {
            notificationService.showNotification(NOTIFICATION_RED, ADMIN_QUESTIONS_CANT_SAVE);
            return null;
        }
        notificationService.showNotification(NOTIFICATION_GREEN, ADMIN_QUESTIONS_SAVED);
        reloadQuestionsTable();
        return savedQuestion;
    }

    private Question saveNewQuestion(AdminQuestionNewForm.SaveEvent event) {
        logger.info("Saving: Selected Question ID: {}", event.getQuestion().getIdquestions());
        Question currentQuestion = event.getQuestion();
        if (currentQuestion.getOption3() == null || currentQuestion.getOption3().isBlank()) {

            logger.info("currentQuestion: Option3 Null or Empty");
            currentQuestion.setOption3(null);
            currentQuestion.setAnswer3(null);
        }
        if (imageCheckboxNew.getValue()) {
            if (!memoryBufferNew.getFileName().isBlank()) {
                logger.info("MemoryBuffer image name : {}", memoryBufferNew.getFileName());
                    try{
                    String urlToSave = cloudinary.uploader().upload(memoryBufferNew.getInputStream().readAllBytes(),
                            ObjectUtils.asMap("unique_filename", true)).get("secure_url").toString();
                    logger.info("url of saved to remote file: {}", urlToSave);
                    currentQuestion.setImage(urlToSave);

                } catch (IOException e) {
                    notificationService.showNotification(NOTIFICATION_RED, ADMIN_QUESTIONS_CANT_SAVE);
                    logger.error(e.getMessage());
                    return null;
                }
            }
        }
        Question savedQuestion = questionAdminViewService.saveQuestionToDataBase(currentQuestion);
        if(savedQuestion == null) {
            notificationService.showNotification(NOTIFICATION_RED, ADMIN_QUESTIONS_CANT_SAVE_QUESTION);
            return null;
        }
        notificationService.showNotification(NOTIFICATION_GREEN, ADMIN_QUESTIONS_SAVED);

        questionNewForm.clear();

        uploadNew.clearFileList();

        memoryBufferNew = new MemoryBuffer();
        uploadNew.setReceiver(memoryBufferNew);
        reloadQuestionsTable();
        questionsTable.getSelectionModel().select(questionsList.get(questionsList.size()-1));
        questionsTable.scrollToEnd();
        return savedQuestion;
    }

    private void reloadQuestionsTable() {
        questionsList = questionAdminViewService.getAll();
        logger.info("Questions are not empty: {}", questionsList.stream().findAny().isPresent());
        questionsTable.setItems(questionsList);
        imageCheckboxEdit.setValue(false);
        imageCheckboxNew.setValue(false);
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
            imageCheckboxEdit.setValue(false);
            imageCheckboxNew.setValue(false);
            memoryBuffer = new MemoryBuffer();
            upload.setReceiver(memoryBuffer);
        }
    }

}
