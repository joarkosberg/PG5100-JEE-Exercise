package no.westerdals.backend.ejb;

import no.westerdals.backend.entity.Event;
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

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class EventEJBTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "no.westerdals.backend")
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserEJB userEJB;

    @EJB
    private EventEJB eventEJB;

    @Test
    public void testCreateNewEvent(){
        long orgCount = eventEJB.countAllEvents();
        User user = userEJB.createNewUser("AA", "AAA", "A", null, "AA", CountryName.Denmark);
        Event event = eventEJB.createNewEvent("Title", CountryName.Denmark, "Location", "Description", user);

        assertEquals(orgCount + 1, eventEJB.countAllEvents());
        assertEquals(event.getAuthor(), user.getUsername());

        userEJB.addEvent(user.getUsername(), event.getId());
        assertEquals(1, userEJB.findUser(user.getUsername()).getEvents().size());
    }

    @Test
    public void testGetEventsByCountry(){
        User user = userEJB.createNewUser("BB", "AAA", "A", null, "AA", CountryName.Denmark);
        eventEJB.createNewEvent("AA", CountryName.China, "Location", "Descrtipion", user);
        eventEJB.createNewEvent("AA", CountryName.China, "Location", "Descrtipion", user);
        eventEJB.createNewEvent("AA", CountryName.France, "Location", "Descrtipion", user);
        eventEJB.createNewEvent("AA", CountryName.China, "Location", "Descrtipion", user);
        eventEJB.createNewEvent("AA", CountryName.China, "Location", "Descrtipion", user);

        assertEquals(4, eventEJB.getEventsByCountry(CountryName.China).size());
        assertEquals(1, eventEJB.getEventsByCountry(CountryName.France).size());
    }

    @Test
    public void testDeletePost(){
        User user = userEJB.createNewUser("CC", "AAA", "A", null, "AA", CountryName.Denmark);
        Event event = eventEJB.createNewEvent("AA", CountryName.China, "Location", "Descrtipion", user);

        long orginalCount= eventEJB.countAllEvents();
        eventEJB.deleteEvent(event);
        assertEquals(orginalCount - 1, eventEJB.countAllEvents());
    }

    @Test(expected = EJBException.class)
    public void testCreateNewEventWithEmptyField(){
        User user = userEJB.createNewUser("DD", "AAA", "A", null, "AAA", CountryName.Albania);
        eventEJB.createNewEvent("", CountryName.China, "Location", "Descrtipion", user);
    }
}
