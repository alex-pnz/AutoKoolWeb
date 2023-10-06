package com.fullstackhub.autokoolweb.views;

import com.fullstackhub.autokoolweb.dtos.UserAdminViewIn;
import com.fullstackhub.autokoolweb.models.User;
import com.fullstackhub.autokoolweb.services.NotificationService;
import com.fullstackhub.autokoolweb.services.UserAdminViewService;
import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.fullstackhub.autokoolweb.constants.StringConstants.*;

@PageTitle(ADMIN_USERS_TITLE)
@Route(value = ADMIN_USERS_URL, layout = AdminLayout.class)
@RolesAllowed({"ADMIN"})
public class AdminUsersView extends VerticalLayout {
    private Grid<UserAdminViewIn> usersTable = new Grid<>(UserAdminViewIn.class);
    private static final Logger logger = LoggerFactory.getLogger(AdminUsersView.class);
    private AdminUserEditForm userEditForm;
    private AdminUserNewForm userNewForm;
    private SOChart soChart = new SOChart();
    private UserAdminViewIn userAdminViewIn;
    private final UserAdminViewService userAdminViewService;
    private final NotificationService notificationService;
    public AdminUsersView(UserAdminViewService userAdminViewService,
                          NotificationService notificationService) {
        this.userAdminViewService = userAdminViewService;
        this.notificationService = notificationService;
        addClassName("admin-users-view");
        setSizeFull();
        setGrid();

        add(
            usersTable,
            setTabs()
        );

        reloadUsersTable();
    }

    private Component setTabs() {
        TabSheet tabSheet = new TabSheet();

        tabSheet.add(ADMIN_USERS_EDIT,
                new Div(setFormEditLayout()));
        tabSheet.add(ADMIN_USERS_NEW,
                new Div(setFormNewLayout()));
        
        return tabSheet;
    }

    private void reloadUsersTable() {
        List<UserAdminViewIn> users = userAdminViewService.getAllDto();
        logger.info("Users are not empty: {}", users.stream().findAny().isPresent());
        usersTable.setItems(users);
    }

    private Component setFormEditLayout(){
        userEditForm = new AdminUserEditForm(notificationService);
        userEditForm.addSaveListener(this::saveUser);
        userEditForm.addDeleteListener(this::deleteUser);
        setChart(userAdminViewIn);
        soChart.addClassName("admin-chart-hidden");
        HorizontalLayout horizontalLayout = new HorizontalLayout(userEditForm, soChart);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        return horizontalLayout;
    }

    private Component setFormNewLayout(){
        userNewForm = new AdminUserNewForm(notificationService);
        userNewForm.addSaveListener(this::saveNewUser);

        HorizontalLayout horizontalLayout = new HorizontalLayout(userNewForm);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        return horizontalLayout;
    }

    private boolean deleteUser(AdminUserEditForm.DeleteEvent event) {
        logger.info("Deleting: Selected User ID: {}", event.getUser().getId());
        boolean deleted = userAdminViewService.deleteUserToDataBase(event.getUser());
        reloadUsersTable();
        return deleted;
    }

    private User saveUser(AdminUserEditForm.SaveEvent event){
        logger.info("Editing: Selected User ID: {}", event.getUser().getId());
        User savedUser = userAdminViewService.saveUserToDataBase(event.getUser());

        if(savedUser == null) {
            notificationService.showNotification(NOTIFICATION_RED, ADMIN_USER_CANT_SAVE);
            return null;
        }
        notificationService.showNotification(NOTIFICATION_GREEN, ADMIN_USERS_SAVED);
        reloadUsersTable();
        return savedUser;
    }

    private User saveNewUser(AdminUserNewForm.SaveEvent event){
        logger.info("Saving New user: Username: {}", event.getUser().getUsername());
        User savedUser = userAdminViewService.saveUserToDataBase(event.getUser());

        if(savedUser == null) {
            notificationService.showNotification(NOTIFICATION_RED, ADMIN_USER_CANT_SAVE);
            return null;
        }
        notificationService.showNotification(NOTIFICATION_GREEN, ADMIN_USERS_SAVED);
        userNewForm.clear();
        reloadUsersTable();
        return savedUser;
    }


    private void setGrid() {
        usersTable.addClassName("user-table");
        usersTable.setSizeFull();
        usersTable.setColumns("id","username", "password", "role", "passed", "failed", "incomplete", "total");
        usersTable.getColumnByKey("id").setHeader("N");
        usersTable.getColumnByKey("username").setHeader("Имя");
        usersTable.getColumnByKey("password").setHeader("Пароль");
        usersTable.getColumnByKey("role").setHeader("Роль");
        usersTable.getColumnByKey("passed").setHeader("Успешно");
        usersTable.getColumnByKey("failed").setHeader("Не удачно");
        usersTable.getColumnByKey("incomplete").setHeader("Не завершил");
        usersTable.getColumnByKey("total").setHeader("Всего");
        usersTable.getColumns().forEach(column -> column.setAutoWidth(true));

        usersTable.asSingleSelect().addValueChangeListener(user-> setUser(user.getValue()));
    }

    private void setUser(UserAdminViewIn userAdminViewIn){
        userEditForm.setUserEditFields(userAdminViewIn);
        this.userAdminViewIn = userAdminViewIn;
        setChart(userAdminViewIn);
        soChart.clear();
        try {
            soChart.update();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setChart(UserAdminViewIn userAdminViewIn){

        CategoryData labels = new CategoryData("Успешно", "Не удачно", "Не завершил", "Всего");
        Data data;
        if (userAdminViewIn == null) {
            data = new Data(0, 0, 0, 0);
        } else {
            soChart.removeClassName("admin-chart-hidden");
            soChart.setClassName("admin-chart-visible");
            data = new Data(userAdminViewIn.getPassed(), userAdminViewIn.getFailed(),
                    userAdminViewIn.getIncomplete(), userAdminViewIn.getTotal());
        }
        BarChart bc = new BarChart(labels, data);

        RectangularCoordinate rc = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        Position p = new Position();
        p.setBottom(Size.percentage(10));
        rc.setPosition(p);
        bc.plotOn(rc);
        bc.setName("");
        soChart.add(bc);
    }

}
