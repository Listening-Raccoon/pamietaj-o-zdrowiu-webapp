package com.listeningraccoon.pamietaj_o_zdrowiu.frontend.views;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.Prescription;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.User;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.PrescriptionService;
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

import java.util.List;

public class DetailsList extends VerticalLayout {
    private final Grid<Prescription> grid = new Grid<>(Prescription.class);
    private final TextField filterText = new TextField();
    private final Button deleteButton = new Button("Delete");
    private final Button printButton = new Button("Print");

    private List<Prescription> prescriptions;
    private User user;
    private Prescription selectedPrescription;
    private final PrescriptionService prescriptionService;

    public DetailsList(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
        addClassName("details-list");

        add(getToolbar(), getGrid());
        switchButtons(false);
    }

    public void setPrescriptions(User user) {
        this.prescriptions = user.getPrescriptions();
        this.user = user;
        updateGrid();
    }

    public void deselectAll() {
        grid.deselectAll();
    }

    public void switchButtons(boolean state) {
        deleteButton.setVisible(state);
        printButton.setVisible(state);
    }

    private void updateGrid() {
        grid.setItems(prescriptions);
    }

    public void filterGrid(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
        updateGrid();
    }

    public void clearFilter() {
        filterText.clear();
    }

    private Component getToolbar() {
        Div spacer = new Div();
        spacer.setWidth("19%");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        filterText.setWidth("100%");
        filterText.setPlaceholder("Find prescription...");
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(event -> filterGrid(prescriptionService.getPrescriptionsOfUserByFilter(user.getEmail(), filterText.getValue())));
        HorizontalLayout horizontalLayout = new HorizontalLayout(filterText, spacer, printButton, deleteButton);
        horizontalLayout.setWidth("100%");

        deleteButton.addClickListener(event -> deletePrescription(selectedPrescription));
        printButton.addClickListener(event -> printPrescription(selectedPrescription));

        return horizontalLayout;
    }

    private void showOptions(Prescription prescription) {
        if (prescription == null) {
            switchButtons(false);
        }
        else {
            switchButtons(true);
            selectedPrescription = prescription;
        }
    }

    private void printPrescription(Prescription prescription) {
        Notification.show("Print " + prescription.getName());
    }

    private void deletePrescription(Prescription prescription) {
        Notification.show("Delete " + prescription.getName());
    }

    private Component getGrid() {
        grid.addClassName("details-grid");
        grid.setSizeFull();
        grid.setColumns("name", "group", "startDate", "endDate", "time");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.asSingleSelect().addValueChangeListener(event -> showOptions(event.getValue()));

        return grid;
    }

}
