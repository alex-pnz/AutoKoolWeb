package com.fullstackhub.autokoolweb.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import static com.fullstackhub.autokoolweb.constants.StringConstants.LOGIN_VIEW_URL;
import static com.fullstackhub.autokoolweb.constants.StringConstants.REGISTER_TEXT;

@Route(value = LOGIN_VIEW_URL)
@RouteAlias("")
@PageTitle(value = "Login | AutoKool")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterListener {

    private LoginForm login = new LoginForm();
    public LoginView(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        login.setAction(LOGIN_VIEW_URL);
        login.setForgotPasswordButtonVisible(false);



        add(
                new H1("Autokool: Web"),
                login,
                new RouterLink(REGISTER_TEXT, RegisterView.class)
        );

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}
