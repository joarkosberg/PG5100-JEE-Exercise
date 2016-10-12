package no.westerdals.frontend.controller;

import no.westerdals.backend.entity.Event;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class EventSessionController implements Serializable {
    @Inject
    private EventRequestController eventRequestController;
    @Inject
    private UserController userController;

    private boolean showOnlyOwnCountry;
    private Map<Long, Boolean> attendingEvent;

    public EventSessionController(){
    }

    @PostConstruct
    public void init(){
        showOnlyOwnCountry = true;
        attendingEvent = new HashMap<>();
    }

    public List<Event> getEvents(){
        List<Event> events;
        if(userController.isLoggedIn() && showOnlyOwnCountry)
            events = eventRequestController.getAllEventsByCountry(userController.getActiveUser().getCountry());
        else
            events = eventRequestController.getAllEvents();

        if(userController.isLoggedIn()){
            for (Event event : events){
                boolean attending = userController.isUserAttendingEvent(event);
                attendingEvent.put(event.getId(), attending);
            }
        }

        return events;
    }

    public void updateAttendance(Long id, Boolean attend){
        if(attend != null && attend){
            userController.addEvent(id);
        } else {
            userController.removeEvent(id);
        }
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

    public Map<Long, Boolean> getAttendingEvent() {
        return attendingEvent;
    }

    public void setAttendingEvent(Map<Long, Boolean> attendingEvent) {
        this.attendingEvent = attendingEvent;
    }
}
