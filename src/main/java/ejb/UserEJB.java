package ejb;

import entity.Comment;
import entity.Post;
import entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

@Stateless
public class UserEJB {

    @PersistenceContext
    private EntityManager em;

    public UserEJB(){}

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

    public synchronized Post createNewPost(@NotNull User user, @NotNull String title,
                                           @NotNull String text){
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        post.setCreated(new Date());

        em.persist(post);

        //Making sure the post gets linked in db and not in cache
        User u = findUser(user.getId());
        u.getPosts().add(post);

        return post;
    }

    public synchronized Comment createNewCommentOnPost(@NotNull User user, @NotNull Post post,
                                                       @NotNull String text){
        Comment comment = new Comment();
        comment.setText(text);
        comment.setCreated(new Date());
        comment.setUpdated(new Date());

        em.persist(comment);

        //Making sure the post gets linked in db and not in cache
        User u = findUser(user.getId());
        Post p = findPost(post.getId());
        u.getComments().add(comment);
        p.getComments().add(comment);

        return comment;
    }

    public synchronized Comment createNewCommentOnComment(@NotNull User user, @NotNull Comment orgComment,
                                          @NotNull String text){
        Comment comment = new Comment();
        comment.setText(text);
        comment.setCreated(new Date());
        comment.setUpdated(new Date());

        em.persist(comment);

        //Making sure the post gets linked in db and not in cache
        User u = findUser(user.getId());
        Comment c = findComment(comment.getId());
        u.getComments().add(comment);
        c.getComments().add(comment);

        return comment;
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

    public long countPosts(){
        Query query = em.createNamedQuery(User.GET_COUNT_OF_ALL_POSTS);
        List <Long> r = query.getResultList();
        return r.get(0);
    }

    public long countComments(){
        Query query = em.createNamedQuery(User.GET_COUNT_OF_ALL_COMMENTS);
        List <Long> r = query.getResultList();
        return r.get(0);
    }

    public List<User> getAllUsers(){
        Query query = em.createQuery("select u from User u");
        List<User> users = query.getResultList();
        return users;
    }

    public List<Post> getAllPosts(){
        Query query = em.createQuery("select p.comments from Post p");
        List<Post> posts = query.getResultList();
        return posts;
    }

    public List<Comment> getAllComments(){
        Query query = em.createQuery("select c from Comment c");
        List<Comment> comments = query.getResultList();
        return comments;
    }

    public void deletePost(long id){
        Query query = em.createQuery("delete from Post p where p.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public void upVotePost(Post post){
        Post p = findPost(post.getId());
        p.setUpVotes(p.getUpVotes() + 1);
    }

    public void downVotePost(Post post){
        Post p = findPost(post.getId());
        p.setDownVotes(p.getDownVotes() + 1);
    }

    public void upVoteComment(Comment comment){
        Comment c = findComment(comment.getId());
        c.setUpVotes(c.getUpVotes() + 1);
    }

    public void downVoteComment(Comment comment){
        Comment c = findComment(comment.getId());
        c.setDownVotes(c.getDownVotes() + 1);
    }

    public User findUser(long id){
        User user = em.find(User.class, id);
        return user;
    }

    public User findUserByUserName(String userName){
        TypedQuery<User> q = em.createQuery("select u from User u  WHERE userName = :userName", User.class);
        User user = q.setParameter("userName", userName).getSingleResult();
        return user;
    }

    public Post findPost(long id){
        Post post = em.find(Post.class, id);
        return post;
    }

    public Comment findComment(long id){
        Comment comment = em.find(Comment.class, id);
        return comment;
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