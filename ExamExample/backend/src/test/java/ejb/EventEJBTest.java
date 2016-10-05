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

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class EventEJBTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(UserEJB.class, EventEJB.class, CountryName.class, User.class, Event.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserEJB userEJB;

    @EJB
    private EventEJB eventEJB;

    @Test
    public void testCreateNewEvent(){
        User user = userEJB.createNewUser("AA", "AAA", "A", null, "AA", CountryName.Denmark);
        eventEJB.createNewEvent(null, CountryName.Denmark, null, null, user);
        assertEquals(1, eventEJB.countAllPosts());
        assertEquals(1, userEJB.findUser(user.getUsername()).getEvents().size());
    }
}
