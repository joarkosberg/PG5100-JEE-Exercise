package controller;

import ejb.UserEJB;
import entity.User;

import javax.faces.bean.SessionScoped;
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

    public boolean isLoggedIn(){
        return activeUser != null;
    }

    public User getActiveUser(){
        return activeUser;
    }



}
