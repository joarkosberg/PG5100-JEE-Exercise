package ejb;

import entity.Comment;
import entity.Post;
import entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Stateless
public class UserEJB {

    @PersistenceContext
    private EntityManager em;

    public UserEJB(){}

    public synchronized User createNewUser(@NotNull String name, String surname,
                                           User.CountryName countryName, @NotNull String email){
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setCountry(countryName);
        user.setEmail(email);

        em.persist(user);

        return user;
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
        Query query = em.createQuery("select p from Post p");
        List<Post> posts = query.getResultList();
        return posts;
    }

    public List<Comment> getAllComments(){
        Query query = em.createQuery("select c from Comment c");
        List<Comment> comments = query.getResultList();
        return comments;
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

    public Post findPost(long id){
        Post post = em.find(Post.class, id);
        return post;
    }

    public Comment findComment(long id){
        Comment comment = em.find(Comment.class, id);
        return comment;
    }
}
