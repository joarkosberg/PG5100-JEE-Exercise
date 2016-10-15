package com.joarkosberg.exercise.frontend.controller;

import com.joarkosberg.exercise.backend.ejb.UserEJB;
import com.joarkosberg.exercise.backend.entity.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class UserController implements Serializable{

    @EJB
    private UserEJB userEJB;

    private String formUserName;
    private String formPassword;

    private User activeUser;

    public UserController(){
    }

    public boolean isLoggedIn(){
        return activeUser != null;
    }

    public String logOut(){
        activeUser = null;
        return "landingPage.jsf";
    }

    public String logIn(){
        boolean valid = userEJB.login(formUserName, formPassword);
        if(valid){
            activeUser = userEJB.findUserByUserName(formUserName);
            return "landingPage.jsf";
        } else {
            return "login.jsf";
        }
    }

    public String registerNew(){
        User user = null;

        try {
            user = userEJB.createNewUser(formUserName, formPassword, "Name", null,
                    User.CountryName.France, "Email@mail.com");
        } catch (Exception e){}

        if(user != null){
            activeUser = user;
            return "landingPage.jsf";
        } else {
            return "login.jsf";
        }
    }

    public String getFormUserName() {
        return formUserName;
    }

    public void setFormUserName(String formUserName) {
        this.formUserName = formUserName;
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

    public void setActiveUser(User activeUser){
        this.activeUser = activeUser;
    }
}
