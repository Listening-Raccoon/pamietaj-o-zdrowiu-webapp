package com.listeningraccoon.pamietaj_o_zdrowiu.frontend.views;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.Prescription;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.User;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.PDFExportService;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.PrescriptionService;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    private final UserService userService;

    public DetailsList(PrescriptionService prescriptionService, UserService userService) {
        this.prescriptionService = prescriptionService;
        this.userService = userService;
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
        printButton.addClickListener(event -> {
            try {
                printPrescription(selectedPrescription);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

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

    private void printPrescription(Prescription prescription) throws IOException {
        String urlString = String.format("http://localhost:8080/api/v1/pdf/export?userId=%s&prescriptionId=%s",
                user.getId().toString(),
                prescription.getId().toString());
        UI.getCurrent().getPage().open(urlString, "_blank");
    }

    private void deletePrescription(Prescription prescription) {
        userService.deletePrescriptionOfUserById(user.getEmail(), prescription.getId());
        prescriptionService.deletePrescription(prescription.getId());
        fireEvent(new DeleteEvent(this, user, prescription));
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

    @Getter
    public static abstract class DetailsListEvent extends ComponentEvent<DetailsList> {
        private final User user;
        private final Prescription prescription;

        protected DetailsListEvent(DetailsList source, User user, Prescription prescription) {
            super(source, false);
            this.user = user;
            this.prescription = prescription;
        }
    }

    public static class DeleteEvent extends DetailsList.DetailsListEvent {
        public DeleteEvent(DetailsList source, User user, Prescription prescription) {
            super(source, user, prescription);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
