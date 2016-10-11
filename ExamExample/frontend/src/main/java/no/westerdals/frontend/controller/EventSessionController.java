package no.westerdals.frontend.controller;

import no.westerdals.backend.ejb.EventEJB;
import no.westerdals.backend.entity.Event;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class EventSessionController implements Serializable {

    @Inject
    private EventEJB eventEJB;
    @Inject
    private EventRequestController eventRequestController;
    @Inject
    private UserController userController;

    private boolean showOnlyOwnCountry;

    public EventSessionController(){
        showOnlyOwnCountry = true;
    }

    public List<Event> getEvents(){
        if(userController.isLoggedIn()){
            if(showOnlyOwnCountry){
                return eventRequestController.getAllEventsByCountry(userController.getActiveUser().getCountry());
            }
        }
        return eventRequestController.getAllEvents();
    }

    public boolean isAnyEventsMade(){
        return getEvents().size() > 0;
    }

    public boolean isShowOnlyOwnCountry() {
        return showOnlyOwnCountry;
    }

    public void setShowOnlyOwnCountry(boolean showOnlyOwnCountry) {
        this.showOnlyOwnCountry = showOnlyOwnCountry;
    }
}
