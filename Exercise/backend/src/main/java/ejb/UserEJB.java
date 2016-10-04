package ejb;

import entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

@Stateless
public class UserEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private PostEJB postEJB;

    public UserEJB(){
    }

    public synchronized User createNewUser(@NotNull String userName, @NotNull String password,
                                           @NotNull String name, String surname,
                                           User.CountryName countryName, @NotNull String email){
        User user = new User();
        user.setUserName(userName);
        user.setName(name);
        user.setSurname(surname);
        user.setCountry(countryName);
        user.setEmail(email);

        //Hash Password
        String salt = getSalt();
        user.setSalt(salt);
        String hash = computeHash(password, salt);
        user.setHash(hash);

        em.persist(user);
        return user;
    }

    public boolean login(String userName, String password) {
        User user = findUserByUserName(userName);
        if (user == null) {
            computeHash(password, getSalt());
            return false;
        }

        String hash = computeHash(password, user.getSalt());
        return  hash.equals(user.getHash());
    }

    public List<User.CountryName> getRepresentedCountries(){
        Query query = em.createNamedQuery(User.GET_COUNTRIES);
        return query.getResultList();
    }

    public long countOfPostsByCountry(User.CountryName country){
        Query query = em.createNamedQuery(User.GET_COUNT_OF_POSTS_BY_COUNTRY);
        query.setParameter("country", country);
        List<Long> result =  query.getResultList();
        return result.get(0);
    }

    public long countOfUsersByCountry(User.CountryName country){
        Query query = em.createNamedQuery(User.GET_COUNT_OF_USERS_BY_COUNTRY);
        query.setParameter("country", country);
        List<Long> result =  query.getResultList();
        return result.get(0);
    }

    public List<User> getMostActiveUsers(){
        Query query = em.createNamedQuery(User.GET_MOST_ACTIVE_USERS);
        List<User> result =  query.getResultList();
        return result;
    }

    public long countUsers(){
        Query query = em.createNamedQuery(User.GET_COUNT_OF_ALL_USERS);
        List <Long> r = query.getResultList();
        return r.get(0);
    }

    public List<User> getAllUsers(){
        Query query = em.createQuery("select u from User u");
        List<User> users = query.getResultList();
        return users;
    }

    public User findUser(long id){
        User user = em.find(User.class, id);
        return user;
    }

    public User findUserByUserName(String userName){
        try {
            TypedQuery<User> q = em.createQuery("select u from User u  WHERE userName = :userName", User.class);
            User user = q.setParameter("userName", userName).getSingleResult();
            return user;
        } catch (NoResultException ex){
            return null;
        }
    }

    @NotNull
    protected String getSalt(){
        SecureRandom random = new SecureRandom();
        int bitsPerChar = 5;
        int twoPowerOfBits = 32; // 2^5
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
