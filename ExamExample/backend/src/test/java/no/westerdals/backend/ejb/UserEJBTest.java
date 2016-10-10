package no.westerdals.backend.ejb;

import no.westerdals.backend.entity.User;
import no.westerdals.backend.enums.CountryName;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserEJBTest {
    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "no.westerdals.backend")
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserEJB userEJB;

    @Test
    public void testCreateNewUser(){
        long originalUserCount = userEJB.countAllUsers();
        userEJB.createNewUser("AA", "AAA", "AAA", null, "AAA", CountryName.Denmark);
        assertEquals(originalUserCount + 1, userEJB.countAllUsers());
    }

    @Test
    public void testLoginWithoutUser() {
        boolean loggedIn = userEJB.login("Brukernavn", "Password");
        assertFalse(loggedIn);
    }

    @Test
    public void testLoginWithValidUser(){
        String password = "AAA";
        User user = userEJB.createNewUser("BB", password, "AAA", null, "AAA", CountryName.Albania);
        boolean loggedIn = userEJB.login(user.getUsername(), password);
        assertTrue(loggedIn);
    }

    @Test(expected = EJBException.class)
    public void testCreateNewUserWithEmptyField(){
        userEJB.createNewUser("CC", "AAA", "", null, "AAA", CountryName.Albania);
    }

    @Test(expected = EJBException.class)
    public void testCreateNewUserWithFieldFilledBySpaces(){
        String name = "       ";
        User user = userEJB.createNewUser("DD", "AAA", name, null, "AAA", CountryName.Albania);
    }

    @Test(expected = EJBException.class)
    public void testCreateNewUserWithUserNameCharactersInvalid(){
        userEJB.createNewUser("!!DD", "AAA", "AAA", null, "AAA", CountryName.Albania);
    }

    @Test(expected = EJBException.class)
    public void testCreateNewUserWithTooShortUsername(){
        userEJB.createNewUser("A", "AAA", "AAA", null, "AAA", CountryName.Albania);
    }
}
