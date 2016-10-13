package no.westerdals.frontend.frontend;

import no.westerdals.backend.enums.CountryName;
import no.westerdals.frontend.po.CreateEventPageObject;
import no.westerdals.frontend.po.HomePageObject;
import no.westerdals.frontend.po.LoginPageObject;
import no.westerdals.frontend.po.NewUserPageObject;
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

        createEventPageObject.createNewEvent("Title", CountryName.China.toString());
        assertTrue(homePageObject.isOnPage());
        assertEquals(orgNumberOfEvents + 1, homePageObject.getCountOfEvents());
    }

    @Test
    public void testCreateEventInDifferentCountries(){
        //Create user
        String username = "bbb";
        String password = "bbb";
        createAndLoginUser(username, password, homePageObject);

        //Count original amount of events
        int originalAmount = homePageObject.getCountOfEvents();

        //Create events
        createNewEvent("Title", CountryName.China.toString(), homePageObject);
        createNewEvent("Title", CountryName.France.toString(), homePageObject);

        //Check amount of events with and without toggle
        assertEquals(originalAmount + 1, homePageObject.getCountOfEvents());
        homePageObject.toggleOnlyMyCountry();
        assertEquals(originalAmount + 2, homePageObject.getCountOfEvents());
        homePageObject.toggleOnlyMyCountry(); //Turn it on again so session has it on for rest of tests.
    }

    @Test
    public void testCreateEventsFromDifferentUsers(){
        createAndLoginUser("ccc", "ccc", homePageObject);

        int originalAmount = homePageObject.getCountOfEvents();

        createNewEvent("Title", CountryName.China.toString(), homePageObject);
        homePageObject.logout();

        createAndLoginUser("ddd", "ddd", homePageObject);
        createNewEvent("Title", CountryName.China.toString(), homePageObject);

        assertEquals(originalAmount + 2, homePageObject.getCountOfEvents());
    }

    @Test
    public void testCreateAndAttendEvent(){
        createAndLoginUser("eee", "eee", homePageObject);

        String title = "aaa";
        createNewEvent(title, CountryName.China.toString(), homePageObject);

        assertEquals(0, homePageObject.getNumberOfAttendees(title));
        homePageObject.toggleAttendEvent(title);
        assertEquals(1, homePageObject.getNumberOfAttendees(title));
        homePageObject.toggleAttendEvent(title);
        assertEquals(0, homePageObject.getNumberOfAttendees(title));
    }

    @Test
    public void testTwoUsersAttendingSameEvent(){
        String username1 = "fff";
        String password1 = "fff";
        createAndLoginUser(username1, password1, homePageObject);

        String title = "bbb";
        createNewEvent(title, CountryName.China.toString(), homePageObject);

        homePageObject.toggleAttendEvent(title);
        assertEquals(1, homePageObject.getNumberOfAttendees(title));

        homePageObject.logout();
        createAndLoginUser("ggg", "ggg", homePageObject);
        homePageObject.toggleAttendEvent(title);
        assertEquals(2, homePageObject.getNumberOfAttendees(title));

        logout(homePageObject);
        login(username1, password1, homePageObject);
        assertEquals(2, homePageObject.getNumberOfAttendees(title));
        homePageObject.toggleAttendEvent(title);
        assertEquals(1, homePageObject.getNumberOfAttendees(title));
    }
}
