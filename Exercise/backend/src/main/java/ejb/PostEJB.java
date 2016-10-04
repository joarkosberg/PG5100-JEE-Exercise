package ejb;

import entity.Comment;
import entity.Post;
import entity.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Stateless
public class PostEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private UserEJB userEJB;

    public PostEJB(){}

    public synchronized Post createNewPost(@NotNull User user, @NotNull String title,
                                           @NotNull String text){
        Post post = new Post();
        post.setPoster(user);
        post.setTitle(title);
        post.setText(text);
        post.setCreated(new Date());

        em.persist(post);

        //Making sure the post gets linked in db and not in cache
        User u = userEJB.findUser(user.getId());
        u.getPosts().add(post);

        return post;
    }

    public long countPosts(){
        Query query = em.createNamedQuery(User.GET_COUNT_OF_ALL_POSTS);
        List<Long> r = query.getResultList();
        return r.get(0);
    }

    public List<Post> getAllPosts(){
        Query query = em.createQuery("select p from Post p");
        List<Post> posts = query.getResultList();
        return posts;
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

    public Post findPost(long id){
        Post post = em.find(Post.class, id);
        return post;
    }

    public User getPoster(Post post){
        return post.getPoster();
    }
}
