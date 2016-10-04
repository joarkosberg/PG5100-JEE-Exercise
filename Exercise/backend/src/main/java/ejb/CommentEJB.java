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
public class CommentEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private UserEJB userEJB;
    @EJB
    private PostEJB postEJB;

    public CommentEJB(){
    }

    public synchronized Comment createNewCommentOnPost(@NotNull User user, @NotNull Post post,
                                                       @NotNull String text){
        Comment comment = new Comment();
        comment.setCommenter(user);
        comment.setText(text);
        comment.setCreated(new Date());
        comment.setUpdated(new Date());

        em.persist(comment);

        //Making sure the post gets linked in db and not in cache
        User u = userEJB.findUser(user.getId());
        Post p = postEJB.findPost(post.getId());
        u.getComments().add(comment);
        p.getComments().add(comment);

        return comment;
    }

    public synchronized Comment createNewCommentOnComment(@NotNull User user, @NotNull Comment orgComment,
                                                          @NotNull String text){
        Comment comment = new Comment();
        comment.setCommenter(user);
        comment.setText(text);
        comment.setCreated(new Date());
        comment.setUpdated(new Date());

        em.persist(comment);

        //Making sure the post gets linked in db and not in cache
        User u = userEJB.findUser(user.getId());
        Comment c = findComment(comment.getId());
        u.getComments().add(comment);
        c.getComments().add(comment);

        return comment;
    }

    public long countComments(){
        Query query = em.createNamedQuery(User.GET_COUNT_OF_ALL_COMMENTS);
        List<Long> r = query.getResultList();
        return r.get(0);
    }

    public List<Comment> getAllComments(){
        Query query = em.createQuery("select c from Comment c");
        List<Comment> comments = query.getResultList();
        return comments;
    }

    public void deleteComment(long id){
        Query query = em.createQuery("delete from Comment c where c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public void upVoteComment(Comment comment){
        Comment c = findComment(comment.getId());
        c.setUpVotes(c.getUpVotes() + 1);
    }

    public void downVoteComment(Comment comment){
        Comment c = findComment(comment.getId());
        c.setDownVotes(c.getDownVotes() + 1);
    }

    public Comment findComment(long id){
        Comment comment = em.find(Comment.class, id);
        return comment;
    }
}
