package ejb;

import entity.Event;
import entity.User;
import enums.CountryName;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.List;

@Stateless
public class EventEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private UserEJB userEJB;

    public EventEJB(){
    }

    public synchronized Event createNewEvent(String title, @NotNull CountryName countryName,
                                             String location, String description,
                                             @NotNull User author){
        User u = em.find(User.class, author.getUsername());
        if(u == null){
            throw new IllegalArgumentException("No user with username: " + u);
        }

        Event event = new Event();
        event.setTitle(title);
        event.setCountry(countryName);
        event.setLocation(location);
        event.setDescription(description);
        event.setAuthor(author.getUsername());

        em.persist(event);
        return event;
    }

    public List<Event> getAllEvents(){
        Query query = em.createNamedQuery(Event.GET_ALL_EVENTS);
        List<Event> events = query.getResultList();
        return events;
    }

    public List<Event> getEventsByCountry(CountryName country){
        return em.createNamedQuery(Event.GET_EVENTS_BY_COUNTRY).setParameter("country",country).getResultList();
    }

    public long countAllPosts(){
        Query query = em.createNamedQuery(Event.COUNT_ALL_EVENTS);
        List<Long> r = query.getResultList();
        return r.get(0);
    }

    public void deletePost(long id){
        Query query = em.createNamedQuery(Event.DELETE_EVENT);
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
