package com.fullstackhub.autokoolweb.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.fullstackhub.autokoolweb.security.SecurityService;

import static com.fullstackhub.autokoolweb.constants.StringConstants.*;

/**
 * The main view is a top-level placeholder for other views.
 */
public class AdminLayout extends AppLayout {
    private final SecurityService securityService;
    Button exit = new Button(BUTTON_EXIT);
    private H2 viewTitle;

    public AdminLayout(SecurityService securityService) {
        this.securityService = securityService;

        exit.addClickListener(e->securityService.logout());

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.setWidthFull();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        exit.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        HorizontalLayout hl = new HorizontalLayout(exit);
        hl.setWidth("100px");
        addToNavbar(true, toggle, viewTitle, hl);
    }

    private void addDrawerContent() {
        H1 appName = new H1(ADMIN_LAYOUT_H1);
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);
        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem(ADMIN_LAYOUT_SIDENAV_USERS, AdminUsersView.class));
        nav.addItem(new SideNavItem(ADMIN_LAYOUT_SIDENAV_QUESTIONS, AdminQuestionsView.class));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
