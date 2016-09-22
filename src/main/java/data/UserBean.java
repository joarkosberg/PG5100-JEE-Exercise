package data;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
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
        user.getPosts().add(post);

        em.persist(post);
    }

    public synchronized void createNewCommentOnPost(@NotNull User user, @NotNull Post post,
                                                    @NotNull String text){
        Comment comment = new Comment();
        comment.setText(text);
        post.getComments().add(comment);
        user.getComments().add(comment);

        em.persist(comment);
    }

    public synchronized void createNewCommentOnComment(@NotNull User user, @NotNull Comment orgComment,
                                          @NotNull String text){
        Comment comment = new Comment();
        comment.setText(text);
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
}
