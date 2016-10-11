package no.westerdals.frontend.controller;

import no.westerdals.backend.ejb.EventEJB;
import no.westerdals.backend.entity.Event;
import no.westerdals.backend.enums.CountryName;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named
@RequestScoped
public class EventController implements Serializable {

    @Inject
    private EventEJB eventEJB;
    @Inject
    UserController userController;

    private String formTitle;
    private String formCountryName;
    private String formLocation;
    private String formDescription;

    public EventController(){
    }

    public String createNewEvent(){
        CountryName countryName;
        try {
            countryName = CountryName.valueOf(formCountryName);
        } catch (IllegalArgumentException ex){ //Not valid country
            return "createEvent.jsf";
        }

        Event event = null;
        try {
            event = eventEJB.createNewEvent(formTitle, countryName, formLocation,
                    formDescription, userController.getActiveUser());
        } catch(Exception e){}

        if(event != null){
            return "home.jsf";
        } else {
            return "createEvent.jsf";
        }
    }

    public String[] getCountryNames() {
        return Arrays.stream(CountryName.values()).map(Enum::name).toArray(String[]::new);
    }

    public boolean isAnyEventsMade(){
        return eventEJB.countAllPosts() > 0;
    }

    public List<Event> getAllEvents(){
        return eventEJB.getAllEvents();
    }

    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public String getFormCountryName() {
        return formCountryName;
    }

    public void setFormCountryName(String formCountryName) {
        this.formCountryName = formCountryName;
    }

    public String getFormLocation() {
        return formLocation;
    }

    public void setFormLocation(String formLocation) {
        this.formLocation = formLocation;
    }

    public String getFormDescription() {
        return formDescription;
    }

    public void setFormDescription(String formDescription) {
        this.formDescription = formDescription;
    }
}
