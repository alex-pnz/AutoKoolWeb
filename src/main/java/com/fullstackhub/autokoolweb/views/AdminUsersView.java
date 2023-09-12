package com.fullstackhub.autokoolweb.views;

import com.fullstackhub.autokoolweb.dtos.UserAdminViewIn;
import com.fullstackhub.autokoolweb.models.User;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@PageTitle("Autokool: Users")
@Route(value = "admin-users", layout = MainLayout.class)
public class AdminUsersView extends VerticalLayout {
    private Grid<UserAdminViewIn> usersTable = new Grid<>(UserAdminViewIn.class);
    private static final Logger logger = LoggerFactory.getLogger(AdminUsersView.class);
    private AdminUserEditForm userEditForm;
    private AdminUserNewForm userNewForm;
    private SOChart soChart = new SOChart();
    private UserAdminViewIn userAdminViewIn;
    private final UserAdminViewService userAdminViewService;

    public AdminUsersView(UserAdminViewService userAdminViewService) {
        this.userAdminViewService = userAdminViewService;
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

        tabSheet.add("Редактировать данные студента",
                new Div(setFormEditLayout()));
        tabSheet.add("Добавить нового студента",
                new Div(setFormNewLayout()));
        
        return tabSheet;
    }

    private void reloadUsersTable() {
        List<UserAdminViewIn> users = userAdminViewService.getAllDto();
        logger.info("Users are not empty: {}", users.stream().findAny().isPresent());
        usersTable.setItems(users);
    }

    private Component setFormEditLayout(){
        userEditForm = new AdminUserEditForm();
        userEditForm.addSaveListener(this::saveUser);
        userEditForm.addDeleteListener(this::deleteUser);
        setChart(userAdminViewIn);
        soChart.addClassName("admin-chart-hidden");
        HorizontalLayout horizontalLayout = new HorizontalLayout(userEditForm, soChart);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        return horizontalLayout;
    }

    private Component setFormNewLayout(){
        userNewForm = new AdminUserNewForm();
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
        User user = userAdminViewService.saveUserToDataBase(event.getUser());
        reloadUsersTable();
        return user;
    }

    private User saveNewUser(AdminUserNewForm.SaveEvent event){
        logger.info("Saving New user: Username: {}", event.getUser().getUsername());
        User user = userAdminViewService.saveNewUserToDataBase(event.getUser());
        reloadUsersTable();
        return user;
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
