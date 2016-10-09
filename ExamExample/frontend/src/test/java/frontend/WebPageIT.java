package frontend;

import org.junit.Before;
import org.junit.Test;
import po.HomePageObject;

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
}
