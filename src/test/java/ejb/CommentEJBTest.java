package ejb;

import entity.Comment;
import entity.Post;
import entity.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class CommentEJBTest {
    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(User.class, Post.class, Comment.class,
                        UserEJB.class, PostEJB.class, CommentEJB.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserEJB userEJB;
    @EJB
    private PostEJB postEJB;
    @EJB
    private CommentEJB commentEJB;

    @Test
    public void testCreatingNewComments(){
        User user = userEJB.createNewUser("AA", "A", "B", null, User.CountryName.Albania, "abc@abc.com");
        Post post = postEJB.createNewPost(user, "Title", "Text text");

        long orgCommentCount = commentEJB.countComments();
        Comment comment = commentEJB.createNewCommentOnPost(user, post, "Comment!");
        commentEJB.createNewCommentOnComment(user, comment, "comment 2");

        long commentCount = commentEJB.countComments();
        assertEquals(2, userEJB.findUser(user.getId()).getComments().size());
        assertEquals(orgCommentCount + 2, commentCount);
    }

    @Test
    public void testCommentConstraints(){
        //TODO
    }

    @Test
    public void testVotingOnComment(){
        User user = userEJB.createNewUser("BB", "B", "B", null, User.CountryName.Albania, "abc@abc.com");
        Post post = postEJB.createNewPost(user, "Title", "Text text");
        Comment comment = commentEJB.createNewCommentOnPost(user, post, "hei");

        assertEquals(0, comment.getUpVotes());
        commentEJB.upVoteComment(comment);
        assertEquals(1, commentEJB.findComment(comment.getId()).getUpVotes());

        assertEquals(0, comment.getDownVotes());
        commentEJB.downVoteComment(comment);
        assertEquals(1, commentEJB.findComment(comment.getId()).getDownVotes());
    }
}
