package com.listeningraccoon.pamietaj_o_zdrowiu.backend.services;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.WebUser;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.repositories.WebUserRepository;
import com.listeningraccoon.pamietaj_o_zdrowiu.frontend.views.MainList;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private WebUserRepository webUserRepository;


    public WebUser getWebUser(String email) {
        return webUserRepository.findByEmail(email);
    }

    public WebUser createWebUser(WebUser webUser) {
        return webUserRepository.save(webUser);
    }

    public void authUser(String email, String password) {
        WebUser webUser = getWebUser(email);
        if (webUser == null) {
            Notification.show("No account with that email was found");
        }
        else if (!webUser.comparePassword(password)) {
            Notification.show("Incorrect password");
        }
        else {
            createRoutes();
            VaadinSession.getCurrent().getSession().setAttribute("currentUser", webUser.getId());
            UI.getCurrent().navigate(MainList.class);
        }
    }

    private void createRoutes() {
        RouteConfiguration.forSessionScope().setRoute("home", MainList.class);
    }

}
