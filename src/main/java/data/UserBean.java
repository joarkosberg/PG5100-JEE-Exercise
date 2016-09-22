package data;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Stateless
public class UserBean {

    @PersistenceContext
    private EntityManager em;

    public UserBean(){}

    public synchronized void createNewUser(@NotNull String name, String surname,
                                User.CountryName countryName, @NotNull String email){
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setCountry(countryName);
        user.setEmail(email);

        em.persist(user);
    }

    public synchronized void createNewPost(@NotNull User user, @NotNull String title,
                                           @NotNull String text){
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        post.setCreated(new Date());
        user.getPosts().add(post);

        em.persist(post);
    }

    public synchronized void createNewCommentOnPost(@NotNull User user, @NotNull Post post,
                                                    @NotNull String text){
        Comment comment = new Comment();
        comment.setText(text);
        comment.setCreated(new Date());
        comment.setUpdated(new Date());
        post.getComments().add(comment);
        user.getComments().add(comment);

        em.persist(comment);
    }

    public synchronized void createNewCommentOnComment(@NotNull User user, @NotNull Comment orgComment,
                                          @NotNull String text){
        Comment comment = new Comment();
        comment.setText(text);
        comment.setCreated(new Date());
        comment.setUpdated(new Date());
        orgComment.getComments().add(comment);
        user.getComments().add(comment);

        em.persist(comment);
    }

    public List<String> getRepresentedCountries(){
        Query query = em.createNamedQuery(User.GET_COUNTRIES);
        return query.getResultList();
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
        Query query = em.createQuery("select count(*) from Comment c");
        List <Long> r = query.getResultList();
        return r.get(0);
    }

    public List<User> getAllUsers(){
        Query query = em.createQuery("select u from User u");
        List<User> users = query.getResultList();
        return users;
    }

    public List<Post> getAllPosts(){
        Query query = em.createQuery("select p from Post p");
        List<Post> posts = query.getResultList();
        return posts;
    }

    public List<Comment> getAllComments(){
        Query query = em.createQuery("select c from Comment c");
        List<Comment> comments = query.getResultList();
        return comments;
    }
}
