package data;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.List;

@Stateless
public class UserBean {

    @PersistenceContext
    private EntityManager em;

    public UserBean(){}

    public void registerNewUser(@NotNull String name, @NotNull String surname,
                                CountryName countryName, @NotNull String email){
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setCountry(countryName);
        user.setEmail(email);

        em.persist(user);
    }

    public void createNewPost(@NotNull User user, @NotNull String title, @NotNull String text){
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        user.getPosts().add(post);

        em.persist(post);
    }

    public void createNewCommentOnPost(@NotNull User user, @NotNull Post post, @NotNull String text){
        Comment comment = new Comment();
        comment.setText(text);
        post.getComments().add(comment);
        user.getComments().add(comment);

        em.persist(comment);
    }

    public void createNewCommentOnComment(@NotNull User user, @NotNull Comment orgComment, @NotNull String text){
        Comment comment = new Comment();
        comment.setText(text);
        orgComment.getComments().add(comment);
        user.getComments().add(comment);

        em.persist(comment);
    }

    public List<String> representedCountries(){
        return null;
    }
}
