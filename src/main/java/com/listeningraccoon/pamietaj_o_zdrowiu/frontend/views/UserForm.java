package com.listeningraccoon.pamietaj_o_zdrowiu.frontend.views;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.Prescription;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class UserForm extends FormLayout {
    Binder<User> userBinder = new Binder<>(User.class);
    Binder<Prescription> prescriptionBinder = new Binder<>(Prescription.class);
    Prescription prescription;

    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    TextField email = new TextField("Email");

    TextField name = new TextField("Name");
    TextField group = new TextField("Group");
    DatePicker startDate = new DatePicker("Start Date");
    DatePicker endDate = new DatePicker("End Date");
    TimePicker time = new TimePicker("Time");


    Button saveButton = new Button("Save");
    Button detailsButton = new Button("Details");
    Button addPrescription = new Button("Add new");
    Button cancelButton = new Button("Cancel");

    VerticalLayout addNewLayout = new VerticalLayout();

    private User user;

    public UserForm() {
        addClassName("user-form");

        firstName.setReadOnly(true);
        lastName.setReadOnly(true);
        email.setReadOnly(true);

        addNewLayout = (VerticalLayout) createAddNewLayout();

        add(firstName, lastName, email, addNewLayout, createButtonsLayout());
    }

    public void selectUser(User user) {
        this.user = user;
        this.firstName.setValue(user.getFirstName());
        this.lastName.setValue(user.getLastName());
        this.email.setValue(user.getEmail());
        clearAddNewLayout();
    }

    private void clearAddNewLayout() {
        name.clear();
        group.clear();
        startDate.clear();
        endDate.clear();
        time.clear();
    }

    private Component createAddNewLayout() {
        VerticalLayout verticalLayout = new VerticalLayout(name, group, startDate, endDate, time);
        verticalLayout.setVisible(false);
        endDate.setEnabled(false);
        startDate.setMin(LocalDate.now());
        startDate.addValueChangeListener(event -> {
            endDate.setMin(startDate.getValue());
            endDate.setEnabled(!startDate.isEmpty());
        });

        return verticalLayout;
    }

    private Component createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        detailsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addPrescription.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        saveButton.addClickListener(event -> validateAndSave());
        cancelButton.addClickListener(event -> fireEvent(new CancelEvent(this)));
        detailsButton.addClickListener(event -> fireEvent(new DetailsEvent(this, user)));
        addPrescription.addClickListener(event -> switchAddNewForm());

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.setVisible(false);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(addPrescription, detailsButton, saveButton, cancelButton);
        horizontalLayout.setSizeFull();

        return horizontalLayout;
    }

    private void switchAddNewForm() {
        addNewLayout.setVisible(!addNewLayout.isVisible());
        saveButton.setVisible(!saveButton.isVisible());
    }

    private void validateAndSave() {
        boolean valid = true;
        if (name.isEmpty()) {
            Notification.show("Please enter the medicine's name");
            valid = false;
        }
        if (group.isEmpty()) {
            Notification.show("Please enter the medicine's group");
            valid = false;
        }
        if (startDate.isEmpty()) {
            Notification.show("Please pick the start date");
            valid = false;
        }
        if (endDate.isEmpty()) {
            Notification.show("Please pick the end date");
            valid = false;
        }
        if (time.isEmpty()) {
            Notification.show("Please enter the time");
            valid = false;
        }
        if (valid) {
            Prescription prescription = getPrescription();
            fireEvent(new SaveEvent(this, user, prescription));
        }
    }

    private Prescription getPrescription() {
        LocalDate startDateValue = startDate.getValue();
        LocalDate endDateValue = endDate.getValue();
        LocalTime timeValue = time.getValue();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        String startDateString = dateFormat.format(startDateValue);
        String endDateString = dateFormat.format(endDateValue);
        String timeString = timeFormat.format(timeValue);
        return new Prescription(name.getValue(),
                                group.getValue(),
                                startDateString,
                                endDateString,
                                timeString);
    }

    @Getter
    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private final User user;
        private final Prescription prescription;

        protected UserFormEvent(UserForm source, User user, Prescription prescription) {
            super(source, false);
            this.user = user;
            this.prescription = prescription;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        public SaveEvent(UserForm source, User user, Prescription prescription) {
            super(source, user, prescription);
        }
    }

    public static class DetailsEvent extends UserFormEvent {
        public DetailsEvent(UserForm source, User user) {
            super(source, user, null);
        }
    }

    public static class CancelEvent extends UserFormEvent {
        public CancelEvent(UserForm source) {
            super(source, null, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
