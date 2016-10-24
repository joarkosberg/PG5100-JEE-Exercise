package no.joarkosberg.exam.frontend.controller;

import no.joarkosberg.exam.backend.ejb.UserEJB;
import no.joarkosberg.exam.backend.entity.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class UserPageController implements Serializable {

    @EJB
    private UserEJB userEJB;

    private User userProfile;

    public UserPageController(){
    }

    public String goToUserPage(String user) {
        userProfile = userEJB.findUser(user);
        return "user.jsf";
    }

    public int getKarma() {
        return userEJB.calculateKarma(userProfile.getUserId());
    }

    public User getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(User userProfile) {
        this.userProfile = userProfile;
    }
}
