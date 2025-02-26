package com.listeningraccoon.pamietaj_o_zdrowiu.frontend.views;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.AuthService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Login | POZ")
@Route("login")
public class LoginView extends VerticalLayout {
    H1 title = new H1("Sign in");
    EmailField email = new EmailField("Email");
    PasswordField password = new PasswordField("Password");
    Button login = new Button("Login");

    private final AuthService authService;

    public LoginView(AuthService authService) {
        RouteConfiguration.forSessionScope().removeRoute(MainList.class);
        this.authService = authService;
        addClassName("login-view");
        setSizeFull();

        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        add(
                getLoginForm()
        );
    }

    private Component getLoginForm() {
        VerticalLayout form = new VerticalLayout();

        form.setMaxHeight("60em");
        form.setMaxWidth("20em");

        form.setAlignItems(FlexComponent.Alignment.CENTER);
        form.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        login.setWidth("60%");
        login.addClickListener(buttonClickEvent -> {
            if(validateEmail() && validatePassword()) {
                authService.authUser(email.getValue().trim(), password.getValue().trim());
            }
        });
        form.add(
                title,
                email,
                password,
                login,
                new RouterLink("Register", RegisterView.class)
        );

        return form;
    }

    private boolean validateEmail() {
        if (email.getValue().trim().isEmpty()) {
            email.setErrorMessage("Email is required");
            email.setInvalid(true);
            return false;
        }
        else {
            email.setInvalid(false);
            return true;
        }
    }

    private boolean validatePassword() {
        if (password.getValue().trim().isEmpty()) {
            password.setErrorMessage("Password is required");
            password.setInvalid(true);
            return false;
        } else {
            password.setInvalid(false);
            return true;
        }
    }
}
