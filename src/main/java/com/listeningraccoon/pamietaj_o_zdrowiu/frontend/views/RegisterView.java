package com.listeningraccoon.pamietaj_o_zdrowiu.frontend.views;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.WebUser;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.AuthService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Register | POZ")
@Route("register")
public class RegisterView extends VerticalLayout {
    H1 title = new H1("Sign up");
    EmailField email = new EmailField("Email");
    PasswordField password = new PasswordField("Password");
    PasswordField confirmPassword = new PasswordField("Confirm password");
    Button register = new Button("Register");

    private final AuthService authService;

    public RegisterView(AuthService authService) {
        RouteConfiguration.forSessionScope().removeRoute(MainList.class);
        this.authService = authService;
        addClassName("register-view");
        setSizeFull();

        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        add(
                getRegisterForm()
        );
    }


    private Component getRegisterForm() {
        VerticalLayout form = new VerticalLayout();

        form.setMaxHeight("60em");
        form.setMaxWidth("20em");

        form.setAlignItems(FlexComponent.Alignment.CENTER);
        form.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        register.setWidth("60%");
        register.addClickListener(buttonClickEvent -> {
            if (validateEmail() && validatePassword() && comparePasswords()) {
                authService.createWebUser(new WebUser(email.getValue(), password.getValue()));
                Notification.show("Registration successful");
            }
        });
        form.add(
                title,
                email,
                password,
                confirmPassword,
                register,
                new RouterLink("Login", LoginView.class)
        );

        return form;
    }

    private boolean validateEmail() {
        if (email.getValue().trim().isEmpty() || !email.getValue().trim().matches("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$")) {
            email.setErrorMessage("Valid email is required");
            email.setInvalid(true);
            return false;
        }
        else {
            email.setInvalid(false);
            return true;
        }
    }

    private boolean validatePassword() {
        if (password.getValue().trim().length() < 8) {
            password.setErrorMessage("Password must have at least 8 characters");
            password.setInvalid(true);
            return false;
        } else if (password.getValue().trim().isEmpty()) {
            password.setErrorMessage("Password is required");
            password.setInvalid(true);
            return false;
        } else {
            password.setInvalid(false);
            return true;
        }
    }

    private boolean comparePasswords() {
        if (confirmPassword.getValue().trim().isEmpty()) {
            confirmPassword.setErrorMessage("Enter the password again");
            confirmPassword.setInvalid(true);
            return false;
        }
        else if (!password.getValue().trim().equals(confirmPassword.getValue().trim())) {
            confirmPassword.setErrorMessage("Passwords do not match");
            confirmPassword.setInvalid(true);
            return false;
        }
        else {
            confirmPassword.setInvalid(false);
            return true;
        }
    }

}
