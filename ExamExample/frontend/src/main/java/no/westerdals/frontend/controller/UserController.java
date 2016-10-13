package no.westerdals.frontend.controller;

import no.westerdals.backend.ejb.UserEJB;
import no.westerdals.backend.entity.Event;
import no.westerdals.backend.entity.User;
import no.westerdals.backend.enums.CountryName;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;

@Named
@SessionScoped
public class UserController implements Serializable {

    @EJB
    private UserEJB userEJB;

    private String formUsername;
    private String formPassword;
    private String formConfirmPassword;
    private String formFirstName;
    private String formMiddleName;
    private String formLastName;
    private String formCountryName;

    private User activeUser;

    public UserController(){
    }

    public String[] getCountryNames() {
        return Arrays.stream(CountryName.values()).map(Enum::name).toArray(String[]::new);
    }

    public String login(){
        boolean valid = userEJB.login(formUsername, formPassword);
        if(valid){
            activeUser = userEJB.findUser(formUsername);
            return "home.jsf";
        } else {
            return "login.jsf";
        }
    }

    public String registerNew(){
        if(! formPassword.equals(formConfirmPassword))
            return "newUser.jsf";

        CountryName countryName;
        try {
            countryName = CountryName.valueOf(formCountryName);
        } catch (IllegalArgumentException ex){ //Not valid country
            return "newUser.jsf";
        }

        User user = null;
        try {
            user = userEJB.createNewUser(formUsername, formPassword, formFirstName, formMiddleName,
                    formLastName, countryName);
        } catch(Exception e){}

        if(user != null){
            activeUser = user;
            return "home.jsf";
        } else {
            return "newUser.jsf";
        }
    }

    public boolean isUserAttendingEvent(Event event){
        return userEJB.isUserAttendingEvent(activeUser, event);
    }

    public void addEvent(long eventId){
        userEJB.addEvent(activeUser.getUsername(), eventId);
    }

    public void removeEvent(long eventId){
        userEJB.removeEvent(activeUser.getUsername(), eventId);
    }

    public String logout(){
        activeUser = null;
        return "home.jsf";
    }

    public boolean isLoggedIn(){
        return activeUser != null;
    }

    public String getFormUsername() {
        return formUsername;
    }

    public void setFormUsername(String formUsername) {
        this.formUsername = formUsername;
    }

    public String getFormPassword() {
        return formPassword;
    }

    public void setFormPassword(String formPassword) {
        this.formPassword = formPassword;
    }

    public User getActiveUser(){
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }


    public String getFormConfirmPassword() {
        return formConfirmPassword;
    }

    public void setFormConfirmPassword(String formConfirmPassword) {
        this.formConfirmPassword = formConfirmPassword;
    }

    public String getFormFirstName() {
        return formFirstName;
    }

    public void setFormFirstName(String formFirstName) {
        this.formFirstName = formFirstName;
    }

    public String getFormMiddleName() {
        return formMiddleName;
    }

    public void setFormMiddleName(String formMiddleName) {
        this.formMiddleName = formMiddleName;
    }

    public String getFormLastName() {
        return formLastName;
    }

    public void setFormLastName(String formLastName) {
        this.formLastName = formLastName;
    }

    public String getFormCountryName() {
        return formCountryName;
    }

    public void setFormCountryName(String formCountryName) {
        this.formCountryName = formCountryName;
    }
}
