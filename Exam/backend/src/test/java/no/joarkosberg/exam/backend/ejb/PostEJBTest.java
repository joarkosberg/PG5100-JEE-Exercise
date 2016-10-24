package no.joarkosberg.exam.backend.ejb;

import no.joarkosberg.exam.backend.entity.Post;
import no.joarkosberg.exam.backend.util.DeleterEJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class PostEJBTest {
    private final String username = "username";
    private final String username1 = "username1";

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "no.joarkosberg.exam.backend")
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserEJB userEJB;
    @EJB
    private PostEJB postEJB;
    @EJB
    private DeleterEJB deleterEJB;

    @Before
    @After
    public void clearDatabase(){
        deleterEJB.deleteEntities(Post.class);
    }

    /*
    Creates same user for each test, doesn't throw exception only return null if same user i registered.
    Which means only first test registers the user and the others use that user.
     */
    @Test
    public void testCreatePost(){
        userEJB.createNewUser(username, "password", "first", null, "last");

        postEJB.createNewPost(username, "text");
        assertEquals(1, postEJB.countPostsAndComments());
    }

    @Test
    public void testVoteFor(){
        userEJB.createNewUser(username, "password", "first", null, "last");

        long id = postEJB.createNewPost(username, "text");
        assertEquals(0, postEJB.getScore(id));

        postEJB.vote(id, username, 1);
        assertEquals(1, postEJB.getScore(id));
    }

    @Test
    public void testVoteAgainst(){
        userEJB.createNewUser(username, "password", "first", null, "last");

        long id = postEJB.createNewPost(username, "text");
        assertEquals(0, postEJB.getScore(id));

        postEJB.vote(id, username, -1);
        assertEquals(-1, postEJB.getScore(id));
    }

    @Test
    public void testCannotVoteForTwice(){
        userEJB.createNewUser(username, "password", "first", null, "last");

        long id = postEJB.createNewPost(username, "text");
        assertEquals(0, postEJB.getScore(id));

        postEJB.vote(id, username, 1);
        assertEquals(1, postEJB.getScore(id));
        postEJB.vote(id, username, 1);
        assertEquals(1, postEJB.getScore(id));
    }

    @Test
    public void testCannotVoteAgainstTwice(){
        userEJB.createNewUser(username, "password", "first", null, "last");

        long id = postEJB.createNewPost(username, "text");
        assertEquals(postEJB.getScore(id), 0);

        postEJB.vote(id, username, -1);
        assertEquals(postEJB.getScore(id), -1);
        postEJB.vote(id, username, -1);
        assertEquals(postEJB.getScore(id), -1);
    }


    @Test
    public void testUnvote(){
        userEJB.createNewUser(username, "password", "first", null, "last");

        long id = postEJB.createNewPost(username, "text");
        assertEquals(0, postEJB.getScore(id));

        postEJB.vote(id, username, -1);
        assertEquals(-1, postEJB.getScore(id));
        postEJB.vote(id, username, 0);
        assertEquals(0, postEJB.getScore(id));
    }

    @Test
    public void testChangeVote(){
        userEJB.createNewUser(username, "password", "first", null, "last");

        long id = postEJB.createNewPost(username, "text");
        assertEquals(0, postEJB.getScore(id));

        postEJB.vote(id, username, -1);
        assertEquals(-1, postEJB.getScore(id));
        postEJB.vote(id, username, 1);
        assertEquals(1, postEJB.getScore(id));
    }

    @Test
    public void testGetAllPostByTime(){
        userEJB.createNewUser(username, "password", "first", null, "last");

        long id1 = postEJB.createNewPost(username, "text");
        postEJB.createNewPost(username, "text");
        long id3 = postEJB.createNewPost(username, "text");

        List<Post> posts = postEJB.getPostsSortedByTime();
        assertEquals(id3, posts.get(0).getId());
        assertEquals(id1, posts.get(2).getId());
    }

    @Test
    public void testGetAllPostByScore(){
        userEJB.createNewUser(username, "password", "first", null, "last");
        userEJB.createNewUser(username1, "password", "first", null, "last");

        postEJB.createNewPost(username, "text");
        long id2 = postEJB.createNewPost(username, "text");
        long id3 = postEJB.createNewPost(username, "text");

        postEJB.vote(id2, username, 1);
        postEJB.vote(id2, username1, 1);
        postEJB.vote(id3, username, -1);
        postEJB.vote(id3, username1, -1);

        List<Post> posts = postEJB.getPostsSortedByScore();
        assertEquals(id2, posts.get(0).getId());
        assertEquals(id3, posts.get(posts.size() - 1).getId());
    }

    @Test
    public void testCreateComment(){
        userEJB.createNewUser(username, "password", "first", null, "last");

        long postId = postEJB.createNewPost(username, "text");
        assertEquals(0, postEJB.countCommentsByPostId(postId));

        postEJB.createNewComment(username, postId, "text");
        assertEquals(1, postEJB.countCommentsByPostId(postId));
    }

    @Test
    public void testModerateOwn(){
        userEJB.createNewUser(username, "password", "first", null, "last");

        long postId = postEJB.createNewPost(username, "text");
        assertEquals(0, postEJB.countCommentsByPostId(postId));

        long commentId = postEJB.createNewComment(username, postId, "text");
        assertFalse(postEJB.findComment(commentId).isModerated());

        postEJB.moderateComment(username, commentId, true);
        assertTrue(postEJB.findComment(commentId).isModerated());
    }

    @Test
    public void testFailModerateOther(){
        userEJB.createNewUser(username, "password", "first", null, "last");
        userEJB.createNewUser(username1, "password", "first", null, "last");

        long postId = postEJB.createNewPost(username, "text");
        long commentId = postEJB.createNewComment(username, postId, "text");
        assertFalse(postEJB.findComment(commentId).isModerated());

        postEJB.moderateComment(username1, commentId, true);
        assertFalse(postEJB.findComment(commentId).isModerated());
    }

    @Test
    public void testVoteForComment(){
        userEJB.createNewUser(username, "password", "first", null, "last");

        long postId = postEJB.createNewPost(username, "text");
        assertEquals(0, postEJB.countCommentsByPostId(postId));

        long commentId = postEJB.createNewComment(username, postId, "text");
        assertEquals(0, postEJB.getScore(commentId));

        postEJB.vote(commentId, username, 1);
        assertEquals(1, postEJB.getScore(commentId));
    }

    /*
    Extra tests i have added for exceptions
     */
    @Test(expected = EJBException.class)
    public void testCreatePostWithoutValidUser(){
        postEJB.createNewPost("TotalyNotValid", "Text");
    }

    @Test(expected = EJBException.class)
    public void testCreateCommentWithoutAValidPost(){
        userEJB.createNewUser(username, "password", "first", null, "last");
        postEJB.createNewComment(username, 1, "Text");
    }
}
