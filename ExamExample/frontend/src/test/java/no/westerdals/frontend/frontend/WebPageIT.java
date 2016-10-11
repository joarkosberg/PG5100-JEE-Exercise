package no.westerdals.frontend.frontend;

import no.westerdals.frontend.controller.EventRequestController;
import no.westerdals.frontend.po.CreateEventPageObject;
import no.westerdals.frontend.po.HomePageObject;
import no.westerdals.frontend.po.LoginPageObject;
import no.westerdals.frontend.po.NewUserPageObject;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.*;

public class WebPageIT extends WebTestBase{
    private HomePageObject homePageObject;

    @Before
    public void initPage(){
        assertTrue(JBossUtil.isJBossUpAndRunning());
        homePageObject = new HomePageObject(getDriver());
        homePageObject.toStartingPage();
        logout(homePageObject);
    }

    @Test
    public void testHomePage(){
        homePageObject.toStartingPage();
        assertTrue(homePageObject.isOnPage());
    }

    @Test
    public void testLoginLink(){
        LoginPageObject loginPageObject = homePageObject.toLoginPage();
        assertTrue(loginPageObject.isOnPage());
    }

    @Test
    public void testLoginWrongUser(){
        LoginPageObject loginPageObject = homePageObject.toLoginPage();
        assertTrue(loginPageObject.isOnPage());

        String username = "TotalyNotValid";
        String password = "SuchValidDoe";
        HomePageObject home = loginPageObject.login(username, password);
        assertNull(home);
        assertTrue(loginPageObject.isOnPage());
    }

    @Test
    public void testCreateUserFailDueToPasswordMismatch (){
        LoginPageObject loginPageObject = homePageObject.toLoginPage();
        assertTrue(loginPageObject.isOnPage());

        NewUserPageObject newUserPageObject = loginPageObject.toNewUserPage();
        assertTrue(newUserPageObject.isOnPage());

        HomePageObject home = newUserPageObject.createNewUser("Username", "Pass1", "Pass2");
        assertNull(home);
        assertTrue(newUserPageObject.isOnPage());
    }

    @Test
    public void testCreateValidUser (){
        LoginPageObject loginPageObject = homePageObject.toLoginPage();
        assertTrue(loginPageObject.isOnPage());

        NewUserPageObject newUserPageObject = loginPageObject.toNewUserPage();
        assertTrue(newUserPageObject.isOnPage());

        String username = "Username";
        newUserPageObject.createNewUser(username, "Pass", "Pass");
        assertTrue(homePageObject.isOnPage());
        assertTrue(homePageObject.isLoggedIn(username));
    }

    @Test
    public void testLogin(){
        String username = "user";
        String password = "pass";

        createAndLoginUser(username, password, homePageObject);
        logout(homePageObject);

        LoginPageObject loginPageObject = homePageObject.toLoginPage();
        assertTrue(loginPageObject.isOnPage());
        loginPageObject.login(username, password);
        assertTrue(homePageObject.isOnPage());
        assertTrue(homePageObject.isLoggedIn(username));
    }

    @Test
    public void testCreateOneEvent(){
        String username = "aaa";
        String password = "aaa";
        createAndLoginUser(username, password, homePageObject);

        int orgNumberOfEvents = homePageObject.getCountOfEvents();

        CreateEventPageObject createEventPageObject = homePageObject.toCreateEventPage();
        assertTrue(createEventPageObject.isOnPage());

        createEventPageObject.createNewEvent();
        assertTrue(homePageObject.isOnPage());
        assertEquals(orgNumberOfEvents + 1, homePageObject.getCountOfEvents());
    }

    @Test
    public void testCreateEventInDifferentCountries(){
        String username = "bbb";
        String password = "bbb";
        createAndLoginUser(username, password, homePageObject);
    }

    @Test
    public void testCreateEventsFromDifferentUsers(){

    }
}
