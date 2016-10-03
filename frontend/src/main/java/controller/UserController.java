package controller;

import ejb.UserEJB;
import entity.User;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class UserController implements Serializable{

    @Inject
    private UserEJB userEJB;

    private String formUserName;
    private String formPassword;

    private User registeredUser;

    public UserController(){
    }

    public boolean isLoggedIn(){
        return registeredUser != null;
    }

    public User getRegisteredUser(){
        return registeredUser;
    }

    public String logOut(){
        registeredUser = null;
        return "login.jsf";
    }

    public String logIn(){
        boolean valid = userEJB.login(formUserName, formPassword);
        if(valid){
            registeredUser = userEJB.findUserByUserName(formUserName);
            return "landingPage.jsf";
        } else {
            return "login.jsf";
        }
    }

    public String registerNew(){
        User user = userEJB.createNewUser(formUserName, formPassword, "Name", null,
                User.CountryName.France, "Email@mail.com");
        if(user != null){
            registeredUser = user;
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
}
