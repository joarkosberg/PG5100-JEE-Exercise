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
        long orgCount = eventEJB.countAllPosts();
        User user = userEJB.createNewUser("AA", "AAA", "A", null, "AA", CountryName.Denmark);
        Event event = eventEJB.createNewEvent("Title", CountryName.Denmark, "Location", "Description", user);

        assertEquals(orgCount + 1, eventEJB.countAllPosts());
        assertEquals(event.getAuthor(), user.getUsername());

        userEJB.addEvent(user.getUsername(), event.getId());
        assertEquals(1, userEJB.findUser(user.getUsername()).getEvents().size());
    }

    @Test
    public void testGetEventsByCountry(){
        User user = userEJB.createNewUser("AAA", "AAA", "A", null, "AA", CountryName.Denmark);
        eventEJB.createNewEvent("AA", CountryName.China, "Location", "Descrtipion", user);
        eventEJB.createNewEvent("AA", CountryName.China, "Location", "Descrtipion", user);
        eventEJB.createNewEvent("AA", CountryName.France, "Location", "Descrtipion", user);
        eventEJB.createNewEvent("AA", CountryName.China, "Location", "Descrtipion", user);
        eventEJB.createNewEvent("AA", CountryName.China, "Location", "Descrtipion", user);

        assertEquals(4, eventEJB.getEventsByCountry(CountryName.China).size());
        assertEquals(1, eventEJB.getEventsByCountry(CountryName.France).size());
    }
}
