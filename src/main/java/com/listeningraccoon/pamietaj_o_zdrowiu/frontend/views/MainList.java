package com.listeningraccoon.pamietaj_o_zdrowiu.frontend.views;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.Prescription;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.User;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.Sender;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.PrescriptionService;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Home | POZ")
public class MainList extends VerticalLayout {
    private final Grid<User> grid = new Grid<>(User.class);
    private final TextField filterText = new TextField();
    private final Button logoutButton = new Button("Logout");
    private final Button filterButton = new Button("Filter");
    private final Button switchThemeButton = new Button("Theme");
    private UserForm form;
    private final DetailsList detailsList;

    private final UserService userService;
    private final PrescriptionService prescriptionService;
    private final Sender sender;

    private boolean displayOnlyAssigned = false;

    public MainList(UserService userService, PrescriptionService prescriptionService, Sender sender) {
        this.userService = userService;
        this.prescriptionService = prescriptionService;
        this.sender = sender;
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
        horizontalLayout.add(filterText, filterButton, spacer, switchThemeButton, logoutButton);
        filterText.setPlaceholder("Find user...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.setWidth("100%");

        filterText.addValueChangeListener(event -> updateList());
        filterButton.addClickListener(event -> setAssignedFilter());
        logoutButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        logoutButton.addClickListener(event -> logout());
        switchThemeButton.addClickListener(event -> switchTheme());
        return horizontalLayout;
    }

    private void switchTheme() {
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();

        if (themeList.contains(Lumo.DARK)) {
            themeList.remove(Lumo.DARK);
        } else {
            themeList.add(Lumo.DARK);
        }
    }

    private void setAssignedFilter() {
        displayOnlyAssigned = !displayOnlyAssigned;
        if (displayOnlyAssigned) {
            filterButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }
        else {
            filterButton.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }
        updateList();
    }

    private void closeForm() {
        form.selectUser(new User("", "", "", new ArrayList<>(), new ArrayList<>()));
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
        List<User> users = userService.getUsersByFilter(filterText.getValue());
        List<User> usersToDisplay = new ArrayList<>();
        if(displayOnlyAssigned) {
            usersToDisplay = filterUsers(users);
        }
        else {
            usersToDisplay = users;
        }
        grid.setItems(usersToDisplay);
    }

    private List<User> filterUsers(List<User> users) {
        List<User> usersToDisplay = new ArrayList<>();
        users.forEach(user -> {
            user.getAssignedUsers().forEach(assignedUser -> {
                if (assignedUser.getId().equals((ObjectId) VaadinSession.getCurrent().getSession().getAttribute("currentUser"))) {
                    usersToDisplay.add(user);
                }
            });
        });
        return usersToDisplay;
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
        form = new UserForm(userService);
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

        form.addListener(UserForm.AssignEvent.class, event -> {
            updateList();
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
        detailsList.setPrescriptions(new User("", "", "", new ArrayList<>(), new ArrayList<>()));
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

        //sender.sendEmail(user, prescription);
        updateList();
    }

    private void logout() {
        UI.getCurrent().getPage().setLocation("login");
        VaadinSession.getCurrent().getSession().invalidate();
        VaadinSession.getCurrent().close();
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
