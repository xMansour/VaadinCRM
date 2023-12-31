package com.example.application.views;

import com.example.application.security.SecurityService;
import com.example.application.views.list.ListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService){
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createDrawer() {
        RouterLink routerLink = new RouterLink("List", ListView.class);
        routerLink.setHighlightCondition(HighlightConditions.sameLocation());
        addToDrawer(new VerticalLayout(routerLink, new RouterLink("Dashboard", DashboardView.class)));
    }

    private void createHeader() {
        H1 logo = new H1("Vaadin CRM");
        logo.addClassNames("text-l", "m-m");
        String authenticatedUser = securityService.getAuthenticatedUser().getUsername();
        Button logoutBtn = new Button("Log out " + authenticatedUser, e -> securityService.logout());



        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logoutBtn);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");
        addToNavbar(header);

    }
}
