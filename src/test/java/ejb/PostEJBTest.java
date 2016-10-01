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
public class PostEJBTest {
    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(User.class, Post.class, Comment.class,
                        UserEJB.class, PostEJB.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserEJB userEJB;
    @EJB
    private PostEJB postEJB;

    @Test
    public void testCreatingNewPost(){
        User user = userEJB.createNewUser("AA", "A", "A", null, User.CountryName.Albania, "abc@abc.com");
        long orgPostCount = postEJB.countPosts();
        postEJB.createNewPost(user, "Title", "Text text");
        long postCount = postEJB.countPosts();
        assertEquals(orgPostCount + 1, postCount);
    }

    @Test
    public void testPostConstraints(){
        //TODO
    }

    @Test
    public void testVotingOnPost(){
        User user = userEJB.createNewUser("BB", "B", "B", null, User.CountryName.Albania, "abc@abc.com");
        Post post = postEJB.createNewPost(user, "Title", "Text text");

        assertEquals(0, post.getUpVotes());
        postEJB.upVotePost(post);
        assertEquals(1, postEJB.findPost(post.getId()).getUpVotes());

        assertEquals(0, post.getDownVotes());
        postEJB.downVotePost(post);
        assertEquals(1, postEJB.findPost(post.getId()).getDownVotes());
    }
}
