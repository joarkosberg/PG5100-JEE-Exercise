package ejb;

import entity.Event;
import entity.User;
import enums.CountryName;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserEJBTest {
    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(UserEJB.class, CountryName.class, User.class, Event.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserEJB userEJB;

    @Test
    public void testCreateNewUser(){
        long orginalUserCount = userEJB.countAllUsers();
        userEJB.createNewUser("AA", "AAA", "A", null, "AA", CountryName.Denmark);
        assertEquals(orginalUserCount + 1, userEJB.countAllUsers());
    }
}
