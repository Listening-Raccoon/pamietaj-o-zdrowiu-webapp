package com.listeningraccoon.pamietaj_o_zdrowiu.frontend.views;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.Prescription;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.User;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.PrescriptionService;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;

@PageTitle("Home | POZ")
@Route(value = "")
//@CssImport(value = "./themes/styles.css")
public class MainList extends VerticalLayout {
    private final Grid<User> grid = new Grid<>(User.class);
    private final TextField filterText = new TextField();
    private final Button logoutButton = new Button("Logout");
    private UserForm form;
    private final DetailsList detailsList;

    private final UserService userService;
    private final PrescriptionService prescriptionService;

    public MainList(UserService userService, PrescriptionService prescriptionService) {
        this.userService = userService;
        this.prescriptionService = prescriptionService;
        this.detailsList = new DetailsList(prescriptionService, userService);
        addClassName("main-view");
        setSizeFull();

        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        
        updateList();
        closeForm();
        closeDetails();
    }

    private Component getToolbar() {
        Div spacer = new Div();
        spacer.setWidth("19%");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.add(filterText, spacer, logoutButton);
        filterText.setPlaceholder("Find user...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.setWidth("100%");

        filterText.addValueChangeListener(event -> updateList());
        logoutButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        logoutButton.addClickListener(event -> logout());
        return horizontalLayout;
    }

    private void closeForm() {
        form.selectUser(new User("", "", "", new ArrayList<>()));
        form.setVisible(false);
        grid.deselectAll();
        detailsList.deselectAll();
        closeDetails();
        removeClassName("form-open");
    }

    private void openForm(User user) {
        if (user == null) {
            closeForm();
        }
        else {
            form.selectUser(user);
            form.setVisible(true);
            addClassName("form-open");
        }
    }

    private void updateList() {
        grid.setItems(userService.getUsersByFilter(filterText.getValue()));
    }

    private Component getContent() {
        VerticalLayout verticalLayout = new VerticalLayout(grid, detailsList);
        detailsList.setHeight("40em");
        verticalLayout.setFlexGrow(3, grid);
        verticalLayout.setFlexGrow(1, detailsList);
        verticalLayout.setSizeFull();

        detailsList.addListener(DetailsList.DeleteEvent.class, event -> {
            updateList();
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout(verticalLayout, form);
        horizontalLayout.setFlexGrow(2, verticalLayout);
        horizontalLayout.setFlexGrow(1, form);
        horizontalLayout.setSizeFull();
        horizontalLayout.addClassName("content");

        return horizontalLayout;
    }

    private void configureForm() {
        form = new UserForm();
        form.setWidth("30em");

        form.addListener(UserForm.SaveEvent.class, event -> {
            savePrescription(event.getUser(), event.getPrescription());
        });

        form.addListener(UserForm.DetailsEvent.class, event -> {
            openDetails(event.getUser());
        });

        form.addListener(UserForm.CancelEvent.class, event -> {
            closeForm();
        });
    }

    private void openDetails(User user) {
        if (detailsList.isVisible() || user == null) {
            closeDetails();
        }
        else {
            detailsList.setPrescriptions(user);
            detailsList.setVisible(true);
            addClassName("details-open");
        }
    }

    private void closeDetails() {
        detailsList.setVisible(false);
        detailsList.setPrescriptions(new User("", "", "", new ArrayList<>()));
        detailsList.clearFilter();
        removeClassName("details-open");
    }

    private void savePrescription(User user, Prescription prescription) {
        prescriptionService.createPrescription( prescription.getName(),
                                                prescription.getGroup(),
                                                prescription.getStartDate(),
                                                prescription.getEndDate(),
                                                prescription.getTime(),
                                                user.getEmail());
        updateList();
    }

    private void logout() {
        //TODO: create a logout event
    }

    private void configureGrid() {
        grid.addClassName("user-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email");
        grid.addColumn(user -> user.getPrescriptions().isEmpty()?"[   ]":"[...]").setHeader("Prescriptions");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.asSingleSelect().addValueChangeListener(event -> {
            openForm(event.getValue());
            if (event.getValue() != null) {
                detailsList.setPrescriptions(event.getValue());
                detailsList.clearFilter();
            }
        });
    }




}
