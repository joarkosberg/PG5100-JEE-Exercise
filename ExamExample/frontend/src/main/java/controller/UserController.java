package controller;

import ejb.UserEJB;
import entity.User;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class UserController implements Serializable {

    @Inject
    private UserEJB userEJB;

    private String formUsername;
    private String formPassword;

    private User activeUser;

    public UserController(){
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

    public String logOut(){
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
}
