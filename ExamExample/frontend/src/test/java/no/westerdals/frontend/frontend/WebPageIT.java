package no.westerdals.frontend.frontend;

import no.westerdals.frontend.po.HomePageObject;
import no.westerdals.frontend.po.LoginPageObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WebPageIT extends WebTestBase{
    private HomePageObject homePageObject;

    @Before
    public void initPage(){
        assertTrue(JBossUtil.isJBossUpAndRunning());
        homePageObject = new HomePageObject(getDriver());
        homePageObject.toStartingPage();
        homePageObject.logout();
        assertTrue(homePageObject.isOnPage());
        assertFalse(homePageObject.isLoggedIn());
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
}
