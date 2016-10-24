package no.joarkosberg.exam.backend.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserEJBTest {

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

    @Test
    public void testKarmaWithModeration(){
        //create a user, a post, and 4 comments for that post
        String username1 = "username123";
        userEJB.createNewUser(username1, "password", "first", null, "last");
        long postId = postEJB.createNewPost(username1, "text");
        long commentId1 = postEJB.createNewComment(username1, postId, "text");
        long commentId2 = postEJB.createNewComment(username1, postId, "text");
        long commentId3 = postEJB.createNewComment(username1, postId, "text");
        long commentId4 = postEJB.createNewComment(username1, postId, "text");
        assertEquals(4, postEJB.countCommentsByPostId(postId));

        //downvote the post
        postEJB.vote(postId, username1, -1);

        //upvote 2 comments
        postEJB.vote(commentId1, username1, 1);
        postEJB.vote(commentId2, username1, 1);

        //moderate the other 2 comments
        postEJB.moderateComment(username1, commentId3, true);
        postEJB.moderateComment(username1, commentId4, true);

        //create a new user, which will upvote one of the non-moderated comments
        String username2 = "username234";
        userEJB.createNewUser(username2, "password", "first", null, "last");
        postEJB.vote(commentId1, username2, 1);

        //verify that the “karma” of the first user is “-1 + 2 + (-20) + 1 = -18”
        assertEquals(-18, userEJB.calculateKarma(username1));
    }

    /*
    Extra tests i have added
     */
    @Test
    public void loginWithoutValidUser(){
        boolean loggedIn = userEJB.login("NotSoValid", "password");
        assertFalse(loggedIn);
    }
}
