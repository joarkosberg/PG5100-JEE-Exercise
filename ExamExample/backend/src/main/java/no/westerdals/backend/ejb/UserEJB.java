package no.westerdals.backend.ejb;

import no.westerdals.backend.entity.Event;
import no.westerdals.backend.entity.User;
import no.westerdals.backend.enums.CountryName;
import org.apache.commons.codec.digest.DigestUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.List;

@Stateless
public class UserEJB {

    @PersistenceContext
    private EntityManager em;

    public UserEJB (){
    }

    public synchronized User createNewUser(@NotNull String username, @NotNull String password,
                                           @NotNull String first_name, String middle_name,
                                           @NotNull String last_name, @NotNull CountryName countryName){
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }

        User u = findUser(username);
        if (u != null) {
            return null;
        }

        User user = new User();
        user.setUsername(username);
        user.setFirst_name(first_name);
        user.setMiddle_name(middle_name);
        user.setLast_name(last_name);
        user.setCountry(countryName);

        String salt = getSalt();
        user.setSalt(salt);
        String hash = computeHash(password, salt);
        user.setHash(hash);

        em.persist(user);
        return user;
    }

    public boolean isUserAttendingEvent(User user, Event event){
        User u = findUser(user.getUsername());
        return u.getEvents().stream().anyMatch(e -> e.getId() == event.getId());
    }

    public boolean login(String userName, String password) {
        User user = findUser(userName);
        if (user == null) {
            computeHash(password, getSalt());
            return false;
        }

        String hash = computeHash(password, user.getSalt());
        return  hash.equals(user.getHash());
    }

    public void addEvent(String username, Long eventId){
        User user = findUser(username);
        Event event = em.find(Event.class, eventId);

        if(user.getEvents().stream().anyMatch(e -> e.getId() == (eventId))){
            return;
        }

        user.getEvents().add(event);
        event.getAttendingUsers().add(user);
    }

    public void removeEvent(String username, long eventId){
        User user = findUser(username);

        Iterator<Event> eventIterator = user.getEvents().iterator();
        while(eventIterator.hasNext()){
            Event current = eventIterator.next();
            if(current.getId() == (eventId)){
                eventIterator.remove();

                Iterator<User> iterUser = current.getAttendingUsers().iterator();
                while(iterUser.hasNext()){
                    User user1 = iterUser.next();
                    if(user.getUsername().equals(user1.getUsername())){
                        iterUser.remove();
                        break;
                    }
                }
                break;
            }
        }
    }

    public List<User> getAllUsers(){
        Query query = em.createNamedQuery(User.GET_ALL_USERS);
        List<User> users = query.getResultList();
        return users;
    }

    public long countAllUsers(){
        Query query = em.createNamedQuery(User.COUNT_ALL_USERS);
        List<Long> count = query.getResultList();
        return count.get(0);
    }

    public User findUser(String username){
        User user = em.find(User.class, username);
        return user;
    }

    @NotNull
    protected String getSalt(){
        SecureRandom random = new SecureRandom();
        int bitsPerChar = 5;
        int twoPowerOfBits = 32;
        int n = 26;
        assert n * bitsPerChar >= 128;

        String salt = new BigInteger(n * bitsPerChar, random).toString(twoPowerOfBits);
        return salt;
    }

    @NotNull
    protected String computeHash(String password, String salt){
        String combined = password + salt;
        String hash = DigestUtils.sha256Hex(combined);
        return hash;
    }
}
