package controller;

import ejb.UserEJB;
import entity.User;
import enums.CountryName;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;

@Named
@SessionScoped
public class UserController implements Serializable {

    @Inject
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
