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


}
