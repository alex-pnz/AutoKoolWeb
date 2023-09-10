package com.fullstackhub.autokoolweb.views;

import com.fullstackhub.autokoolweb.models.User;
import com.fullstackhub.autokoolweb.repositories.AdminUsersRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@PageTitle("Autokool: Users")
@Route(value = "admin-users", layout = MainLayout.class)
public class AdminUsersView extends VerticalLayout {
    private Grid<User> usersTable = new Grid<>(User.class);
    private static final Logger logger = LoggerFactory.getLogger(AdminUsersView.class);
    private final AdminUsersRepository adminUsersRepository;

    public AdminUsersView(AdminUsersRepository adminUsersRepository) {
        this.adminUsersRepository= adminUsersRepository;
        addClassName("admin-users-view");
        setSizeFull();
        setGrid();

        add(
            usersTable
        );

        List<User> users = adminUsersRepository.findAll();
        logger.info("Users are not empty: {}", users.stream().findAny().isPresent());
        usersTable.setItems(users);
        
    }

    private void setGrid() {
        usersTable.addClassName("user-table");
        usersTable.setSizeFull();
        usersTable.setColumns("id","username", "password", "role");
        usersTable.getColumns().forEach(column -> column.setAutoWidth(true));
    }

}
