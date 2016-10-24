package no.joarkosberg.exam.frontend.controller;

import no.joarkosberg.exam.backend.ejb.UserEJB;
import no.joarkosberg.exam.backend.entity.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoggingController implements Serializable {

    @EJB
    private UserEJB userEJB;

    private String formUsername;
    private String formPassword;
    private String formConfirmPassword;
    private String formFirstName;
    private String formMiddleName;
    private String formLastName;

    private String activeUser;

    public LoggingController(){
    }

    public boolean isLoggedIn(){
        return activeUser != null;
    }

    public String login(){
        boolean valid = userEJB.login(formUsername, formPassword);
        if(valid){
            activeUser = formUsername;
            return "home.jsf";
        } else {
            return "login.jsf";
        }
    }

    public String registerNew(){
        if(! formPassword.equals(formConfirmPassword))
            return "newUser.jsf";

        User user = null;
        try {
            user = userEJB.createNewUser(formUsername, formPassword, formFirstName, formMiddleName,
                    formLastName);
        } catch(Exception e){}

        if(user != null){
            activeUser = formUsername;
            return "home.jsf";
        } else {
            return "newUser.jsf";
        }
    }

    public String logOut(){
        activeUser = null;
        return "home.jsf";
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

    public String getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(String activeUser) {
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
}
