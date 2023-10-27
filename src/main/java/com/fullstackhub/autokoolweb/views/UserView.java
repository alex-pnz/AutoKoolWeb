package com.fullstackhub.autokoolweb.views;

import com.fullstackhub.autokoolweb.dtos.UserAdminViewIn;
import com.fullstackhub.autokoolweb.mappers.UserAdminViewMapper;
import com.fullstackhub.autokoolweb.models.Question;
import com.fullstackhub.autokoolweb.models.Result;
import com.fullstackhub.autokoolweb.models.User;
import com.fullstackhub.autokoolweb.repositories.AdminQuestionsRepository;
import com.fullstackhub.autokoolweb.repositories.UsersRepository;
import com.fullstackhub.autokoolweb.services.NotificationService;
import com.fullstackhub.autokoolweb.security.SecurityService;
import com.fullstackhub.autokoolweb.services.UserResultsService;
import com.storedobject.chart.*;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.fullstackhub.autokoolweb.constants.StringConstants.*;

@PageTitle(USER_VIEW_TITLE)
@Route(value = USER_VIEW_URL)
@RolesAllowed("USER")
public class UserView extends HorizontalLayout {

    private final UsersRepository usersRepository;
    private final AdminQuestionsRepository adminQuestionsRepository;
    private final UserAdminViewMapper userAdminViewMapper;
    private final UserResultsService userResultsService;
    private final NotificationService notificationService;
    private final SecurityService securityService;
    private Span labelId = new Span(LABEL_ID);
    private Span textId = new Span();
    private Span labelName = new Span(LABEL_NAME);
    private Span textName = new Span();
    private Hr hr = new Hr();
    private Span labelStat = new Span(LABEL_STAT);
    private Span labelPass = new Span(LABEL_PASS);
    private Span textPass = new Span();
    private Span labelFail = new Span(LABEL_FAIL);
    private Span textFail = new Span();
    private Span labelIncomplete = new Span(LABEL_INCOMPLETE);
    private Span textIncomplete = new Span();
    private Span labelTotal = new Span(LABEL_TOTAL);
    private Span textTotal = new Span();
    private UserAdminViewIn userDTO;
    private SOChart soChart = new SOChart();
    private Button start = new Button(BUTTON_START);
    private Button exit = new Button(BUTTON_EXIT);
    private H2 h2 = new H2();
    private Span textQuestion = new Span();
    private Image image = new Image();
    private Checkbox answer1 = new Checkbox();
    private Checkbox answer2 = new Checkbox();
    private Checkbox answer3 = new Checkbox();
    private Button buttonCheck = new Button(BUTTON_CHECK);
    private Span labelCorrectOrNot = new Span();
    private Button buttonNext = new Button(BUTTON_NEXT);
    private Button buttonIncomplete = new Button(BUTTON_INCOMPLETE);
    private static int questionsFromDB = 7;
    private static int numberOfQuestionsToPass = 5;
    private static int counter = 0;
    private List<Question> questionList = new ArrayList<>();
    private List<Integer> resultList = new ArrayList<>();

    private Question currentQuestion;
    private VerticalLayout examArea = new VerticalLayout();
    private static final Logger logger = LoggerFactory.getLogger(UserView.class);

    public UserView(UsersRepository usersRepository,
                    UserAdminViewMapper userAdminViewMapper,
                    AdminQuestionsRepository adminQuestionsRepository,
                    UserResultsService userResultsService,
                    NotificationService notificationService,
                    SecurityService securityService) {
        this.usersRepository = usersRepository;
        this.userAdminViewMapper = userAdminViewMapper;
        this.adminQuestionsRepository = adminQuestionsRepository;
        this.userResultsService = userResultsService;
        this.notificationService = notificationService;
        this.securityService = securityService;
        setExamArea();
        examArea.setVisible(false);
        setUser();

        textId.getElement().getStyle().set("font-weight", "bold");
        textName.getElement().getStyle().set("font-weight", "bold");

        start.addClickListener(e -> {
            resultList.clear();
            questionList.clear();
            questionList = adminQuestionsRepository.getRandomQuestions(questionsFromDB);
            counter = 0;
            if (questionList!=null&&!questionList.isEmpty()){
                logger.info("Question list: {}, {}", questionList.size(), questionList);
                setExamFields();
                logger.info("Current Question : {}", currentQuestion.getQuestion());
                examArea.setVisible(true);
                start.setEnabled(false);
            } else {
                logger.info("Question list: EMPTY; check DB");
            }
        });

        exit.addClickListener(e -> securityService.logout());

        start.addClickShortcut(Key.ENTER);
        buttonCheck.addClickListener(e -> {

            if (answer3.getLabel() != null || !answer3.getLabel().isBlank()) {
                if (!answer1.getValue()&&!answer2.getValue()&&!answer3.getValue()){
                    notificationService.showNotification(NOTIFICATION_RED, CHOOSE_ANSWER);
                } else {
                    checkAnswer();
                }
            } else {
                if (!answer1.getValue()&&!answer2.getValue()){
                    notificationService.showNotification(NOTIFICATION_RED, CHOOSE_ANSWER);
                } else {
                    checkAnswer();
                }
            }
        });

        buttonNext.addClickListener(e -> {
            counter++;
            setExamFields();
        });

        buttonIncomplete.addClickListener(e -> {
            notificationService.showNotification(NOTIFICATION_RED, EXAM_INTERRUPTED);

            Result resultEntity = new Result(userAdminViewMapper.fomDto(userDTO), 2);

            if (userResultsService.saveResultToDB(resultEntity) != null) {
                logger.info("Result saved successfully");
            } else {
                logger.info("Can't save the result");
            }
            examArea.setVisible(false);
            start.setEnabled(true);
            setUser();
            setUserInfoFields();
        });

        this.setDefaultVerticalComponentAlignment(Alignment.START);
        setMargin(true);
        labelStat.setClassName("label-stat");
        soChart.setClassName("chart-user-view");

        add(setLeft(), examArea);
    }

    public VerticalLayout setLeft() {
        setUserInfoFields();

        HorizontalLayout hl1 = new HorizontalLayout(labelId, textId);
        HorizontalLayout hl2 = new HorizontalLayout(labelName, textName);
        hr.setClassName("user-hr");
        HorizontalLayout hl3 = new HorizontalLayout(labelPass, textPass);
        HorizontalLayout hl4 = new HorizontalLayout(labelFail, textFail);
        HorizontalLayout hl5 = new HorizontalLayout(labelIncomplete, textIncomplete);
        HorizontalLayout hl6 = new HorizontalLayout(labelTotal, textTotal);

        HorizontalLayout hl7 = new HorizontalLayout(start, exit);
        start.setClassName("user-view-button");
        exit.setClassName("user-view-button");
        hl7.setClassName("user-view-buttons");
        VerticalLayout verticalLayout = new VerticalLayout(hl1, hl2, hr, labelStat, hl3, hl4, hl5, hl6, soChart, hl7);
        verticalLayout.setWidth("25em");
        verticalLayout.setClassName("user-view-left-side");
        return verticalLayout;
    }

    private void setUserInfoFields() {
        if(userDTO != null){
            textId.setText(userDTO.getId()+"");
            textName.setText(userDTO.getUsername());
            textPass.setText(userDTO.getPassed()+"");
            textFail.setText(userDTO.getFailed()+"");
            textIncomplete.setText(userDTO.getIncomplete()+"");
            textTotal.setText(userDTO.getTotal()+"");
        }
        setChart(userDTO);
    }

    public void setExamArea() {
        textQuestion.setClassName("user-view-question-text");
        examArea.setWidth(70, Unit.PERCENTAGE);
        HorizontalLayout hl1 = new HorizontalLayout(buttonCheck, labelCorrectOrNot);
        HorizontalLayout hl4 = new HorizontalLayout(buttonNext, buttonIncomplete);
        buttonNext.setClassName("next-button");
        labelCorrectOrNot.setVisible(false);
        buttonNext.setEnabled(false);
        buttonCheck.setEnabled(true);
        buttonIncomplete.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        hl1.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        hl4.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        answer1.setClassName("exam-area-bottom");;
        answer2.setClassName("exam-area-bottom");;
        answer3.setClassName("exam-area-bottom");;
        hl1.setClassName("exam-area-bottom");;
        hl4.setClassName("exam-area-bottom");;

        VerticalLayout vl1 = new VerticalLayout(h2, textQuestion, image);
        VerticalLayout vl2 = new VerticalLayout(answer1, answer2, answer3, hl1, hl4);

        examArea.add(vl1, vl2);
        examArea.setFlexGrow(2, vl1);
        examArea.setFlexGrow(1, vl2);
        examArea.setClassName("exam-area");
    }

    private void setChart(UserAdminViewIn userAdminViewIn) {

        CategoryData labels = new CategoryData("Успешно", "Не удачно", "Не завершил", "Всего");
        Data data;
        if (userAdminViewIn == null) {
            data = new Data(0, 0, 0, 0);
        } else {
            data = new Data(userAdminViewIn.getPassed(), userAdminViewIn.getFailed(),
                    userAdminViewIn.getIncomplete(), userAdminViewIn.getTotal());
        }
        PieChart pc = new PieChart(labels, data);
        Position p = new Position();
        p.setTop(Size.percentage(10));
        pc.setPosition(p);
        pc.getLabel(true).hide();
        soChart.add(pc);
        soChart.setWidth("23em");
        soChart.getDefaultLegend().getTextStyle(true).setColor(new Color("white"));
        try {
            soChart.update();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean setUser() {
        User user = usersRepository.findByUsername(securityService.getLoggedUser());
        if (user != null) {
            userDTO = userAdminViewMapper.toDto(user);
            return true;
        }
        return false;
    }

    public void setExamFields(){
        currentQuestion = questionList.get(counter);
        h2.setText("Билет " + (counter+1));
        textQuestion.setText(currentQuestion.getQuestion());

        if (currentQuestion.getImage() == null || currentQuestion.getImage().isBlank()) {
            image.setVisible(false);
        } else {
            setImage(currentQuestion);
            image.setVisible(true);
        }
        answer1.setLabel(currentQuestion.getOption1());
        answer2.setLabel(currentQuestion.getOption2());
        answer1.setValue(false);
        answer2.setValue(false);
        if (currentQuestion.getOption3() == null || currentQuestion.getOption3().isBlank()) {
            answer3.setLabel("");
            answer3.setVisible(false);
        } else {
            answer3.setLabel(currentQuestion.getOption3());
            answer3.setValue(false);
            answer3.setVisible(true);
        }
        labelCorrectOrNot.setVisible(false);
        buttonNext.setEnabled(false);
        buttonCheck.setEnabled(true);
    }

    private void setImage(Question question) {

        String path = String.format(IMAGE_FOLDER_PATH, question.getImage());

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

    private void checkAnswer() {
        Integer check1 = answer1.getValue()?1:0;
        Integer check2 = answer2.getValue()?1:0;
        Integer check3 = null;
        if (currentQuestion.getOption3() != null && !currentQuestion.getOption3().isBlank()) {
            check3 = answer3.getValue()?1:0;
        }

        boolean result = check1.equals(currentQuestion.getAnswer1()) && check2.equals(currentQuestion.getAnswer2());
        if (check3 != null) {
            result = result && check3.equals(currentQuestion.getAnswer3());
        }

        resultList.add(result?1:0);

        labelCorrectOrNot.setVisible(true);
        labelCorrectOrNot.setText(result?LABEL_CORRECT_YES:LABEL_CORRECT_NO);
        labelCorrectOrNot.setClassName(result?NOTIFICATION_GREEN:NOTIFICATION_RED);

        buttonNext.setEnabled(true);
        buttonCheck.setEnabled(false);

        if(resultList.size() == questionsFromDB) {

            int successfulN = (int) resultList.stream().filter(i -> i.equals(1)).count();

            int finalResult = (successfulN >= numberOfQuestionsToPass)?1:0;

            notificationService.showNotification(
                    finalResult==1?NOTIFICATION_GREEN:NOTIFICATION_RED,
                    finalResult==1?EXAM_PASSED:EXAM_FAILED);

            Result resultEntity = new Result(userAdminViewMapper.fomDto(userDTO), finalResult);

            if (userResultsService.saveResultToDB(resultEntity) != null) {
                logger.info("Result saved successfully");
            } else {
                logger.info("Can't save the result");
            }
            examArea.setVisible(false);
            start.setEnabled(true);
            setUser();
            setUserInfoFields();
        }
    }

}
