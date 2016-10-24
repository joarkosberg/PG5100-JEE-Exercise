package no.joarkosberg.exam.backend.ejb;

import no.joarkosberg.exam.backend.entity.Comment;
import no.joarkosberg.exam.backend.entity.Post;
import no.joarkosberg.exam.backend.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

@Stateless
public class UserEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private PostEJB postEJB;

    public UserEJB (){
    }

    public synchronized User createNewUser(@NotNull String username, @NotNull String password,
                                           @NotNull String first_name, String middle_name,
                                           @NotNull String last_name){
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }

        User u = findUser(username);
        if (u != null) {
            return null;
        }

        User user = new User();
        user.setUserId(username);
        user.setFirstName(first_name);
        user.setMiddleName(middle_name);
        user.setLastName(last_name);
        user.setRegistrationTime(new Date());

        String salt = getSalt();
        user.setSalt(salt);
        String hash = computeHash(password, salt);
        user.setHash(hash);

        em.persist(user);
        return user;
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

    public User findUser(String username){
        User user = em.find(User.class, username);
        return user;
    }

    public int calculateKarma(String userId){
        List<Post> posts =  postEJB.getPostsByUserId(userId);
        List<Comment> comments = postEJB.getCommentsByUserId(userId);

        int score = 0;
        for(Post post : posts)
            score += post.getVotes().values().stream().mapToInt(Integer::intValue).sum();
        for(Comment comment : comments) {
            if (comment.isModerated())
                score -= 10;
            else //Don't count if moderated
                score += comment.getVotes().values().stream().mapToInt(Integer::intValue).sum();
        }

        return score;
    }

    public long countAllUsers(){
        Query query = em.createNamedQuery(User.COUNT_ALL_USERS);
        return (long) query.getSingleResult();
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
