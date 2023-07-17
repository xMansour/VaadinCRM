package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CRMService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import jakarta.annotation.security.PermitAll;

import java.util.Collections;

@PageTitle("Contacts | Vaadin CRM")
@Route(value = "", layout = MainLayout.class)
@PermitAll
public class ListView extends VerticalLayout {
    private Grid<Contact> grid = new Grid<>(Contact.class);
    private TextField filterText = new TextField();

    private ContactForm form;

    private CRMService service;

    public ListView(CRMService service) {
        this.service = service;

        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());

        updateList();
        closeEditor();

    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();
        return content;


    }

    private void configureForm() {
        form = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
        form.setWidth("25em");

        form.addSaveListener(this::saveContact);
        form.addDeleteListener(this::deleteContact);
        form.addCloseListener(closeEvent -> closeEditor());
    }

    private void deleteContact(ContactForm.DeleteEvent deleteEvent) {
        service.deleteContact(deleteEvent.getContact());
        updateList();
        closeEditor();
    }

    private void saveContact(ContactForm.SaveEvent saveEvent) {
        service.saveContact(saveEvent.getContact());
        updateList();
        closeEditor();
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name.");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());


        Button addContactButton = new Button("Add contact");
        addContactButton.addClickListener(buttonClickEvent -> addContact());
        HorizontalLayout horizontalLayout = new HorizontalLayout(filterText, addContactButton);
        horizontalLayout.addClassName("toolbar");
        return horizontalLayout;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();

        grid.setColumns("firstName", "lastName", "email");
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(gridContactComponentValueChangeEvent -> editContact(gridContactComponentValueChangeEvent.getValue()));
    }

    private void editContact(Contact contact) {
        if (contact == null)
            closeEditor();
        else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

}
