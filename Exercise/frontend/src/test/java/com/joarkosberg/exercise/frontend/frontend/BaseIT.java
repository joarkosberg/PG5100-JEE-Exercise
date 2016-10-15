package com.joarkosberg.exercise.frontend.frontend;

import com.joarkosberg.exercise.frontend.po.LandingPageObject;
import com.joarkosberg.exercise.frontend.po.LoginPageObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseIT extends TestBase{
    private LandingPageObject landing;

    @Before
    public void initPage(){
        assertTrue(JBossUtil.isJBossUpAndRunning());
        landing = new LandingPageObject(getDriver());
        landing.toStartingPage();
        assertTrue(landing.isOnPage());
        logout(landing);
    }

    @Test
    public void testGoToLandingPage(){
        landing.toStartingPage();
        assertTrue(landing.isOnPage());
    }

    @Test
    public void testGoToLoginPage(){
        LoginPageObject login = landing.toLoginPage();
        assertTrue(login.isOnPage());
    }

    @Test
    public void testLoginWithoutValidUser(){
        LoginPageObject login = landing.toLoginPage();
        assertTrue(login.isOnPage());
        LandingPageObject l = login.login("NOTaUSER", "SUCH PASSWORD");
        assertNull(l);
        assertTrue(login.isOnPage());
    }

    @Test
    public void testCreateUserWithoutPassword(){
        LoginPageObject login = landing.toLoginPage();
        assertTrue(login.isOnPage());
        LandingPageObject l = login.createNewUser("userWihoutPass", "");
        assertNull(l);
        assertTrue(login.isOnPage());
    }

    @Test
    public void testCreateValidUser(){
        LoginPageObject login = landing.toLoginPage();
        assertTrue(login.isOnPage());

        String username = "AAA";
        login.createNewUser(username, "AAA");

        assertTrue(landing.isOnPage());
        assertTrue(landing.isLoggedIn(username));
    }
}
