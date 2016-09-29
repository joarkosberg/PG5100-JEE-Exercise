package ejb;

import controller.PostController;
import controller.UserController;
import helpers.DeleterEJB;
import entity.Comment;
import entity.Post;
import entity.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(Arquillian.class)
public class UserEJBTest {
    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(User.class, Post.class, Comment.class,
                        UserEJB.class, DeleterEJB.class, UserController.class,
                        PostController.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserEJB userEJB;
    @EJB
    private DeleterEJB deleterEJB;

    @Before
    @After
    public void clearDatabase(){
        deleterEJB.deleteEntities(User.class);
        deleterEJB.deleteEntities(Post.class);
        deleterEJB.deleteEntities(Comment.class);
    }

    @Test
    public void testCreatingNewUsers(){
        userEJB.createNewUser("AA", "A", "A", null, User.CountryName.Albania, "abc@abc.com");
        userEJB.createNewUser("BB", "B", "B", null, User.CountryName.Albania, "abc@abc.com");
        long usersCount = userEJB.countUsers();
        assertEquals(2, usersCount);
    }

    @Test
    public void testCreatingNewPost(){
        User user = userEJB.createNewUser("AA", "A", "A", null, User.CountryName.Albania, "abc@abc.com");
        userEJB.createNewPost(user, "Title", "Text text");
        long postCount = userEJB.countPosts();
        assertEquals(1, postCount);
    }

    @Test
    public void testCreatingNewComments(){
        User user = userEJB.createNewUser("AA", "A", "B", null, User.CountryName.Albania, "abc@abc.com");
        Post post = userEJB.createNewPost(user, "Title", "Text text");

        Comment comment = userEJB.createNewCommentOnPost(user, post, "Comment!");
        userEJB.createNewCommentOnComment(user, comment, "comment 2");

        long commentCount = userEJB.countComments();
        assertEquals(2, userEJB.getAllUsers().get(0).getComments().size());
        assertEquals(2, commentCount);
    }

    @Test
    public void testUserConstraints(){
        //TODO
    }

    @Test
    public void testPostConstraints(){
        //TODO
    }

    @Test
    public void testCommentConstraints(){
        //TODO
    }

    @Test
    public void testGetRepresentedCountries(){
        userEJB.createNewUser("AA", "A", "A", null, User.CountryName.Albania, "abc@abc.com");
        userEJB.createNewUser("BB", "B", "B", null, User.CountryName.Albania, "abc@abc.com");
        userEJB.createNewUser("CC", "C", "C", null, User.CountryName.China, "abc@abc.com");
        userEJB.createNewUser("DD", "D", "D", null, User.CountryName.Norway, "abc@abc.com");
        List<User.CountryName> countries= userEJB.getRepresentedCountries();

        assertEquals(3, countries.size());
        assertNotNull(countries.stream().anyMatch(c -> c.equals(User.CountryName.Albania)));
    }

    @Test
    public void testCountingByCountry(){
        User user1 = userEJB.createNewUser("AA", "A", "A", null, User.CountryName.Albania, "abc@abc.com");
        User user2 = userEJB.createNewUser("BB", "B", "B", null, User.CountryName.Albania, "abc@abc.com");
        User user3 = userEJB.createNewUser("CC", "C", "C", null, User.CountryName.China, "abc@abc.com");
        User user4 = userEJB.createNewUser("DD", "D", "D", null, User.CountryName.Norway, "abc@abc.com");

        assertEquals(2, userEJB.countOfUsersByCountry(User.CountryName.Albania));
        assertEquals(1, userEJB.countOfUsersByCountry(User.CountryName.Norway));
        assertEquals(0, userEJB.countOfUsersByCountry(User.CountryName.Brasil));

        userEJB.createNewPost(user1, "Title", "Text text");
        userEJB.createNewPost(user2, "Title", "Text text");
        userEJB.createNewPost(user3, "Title", "Text text");
        userEJB.createNewPost(user4, "Title", "Text text");
        userEJB.createNewPost(user4, "Title", "Text text");
        userEJB.createNewPost(user1, "Title", "Text text");

        assertEquals(3, userEJB.countOfPostsByCountry(User.CountryName.Albania));
        assertEquals(2, userEJB.countOfPostsByCountry(User.CountryName.Norway));
        assertEquals(0, userEJB.countOfPostsByCountry(User.CountryName.Brasil));
    }

    @Test
    public void testGetMostActiveUsers(){
        User user1 = userEJB.createNewUser("AA", "A", "A", null, User.CountryName.Albania, "abc@abc.com");
        User user2 = userEJB.createNewUser("BB", "B", "B", null, User.CountryName.Albania, "abc@abc.com");
        User user3 = userEJB.createNewUser("CC", "C", "C", null, User.CountryName.China, "abc@abc.com");
        User user4 = userEJB.createNewUser("DD", "D", "D", null, User.CountryName.Norway, "abc@abc.com");

        Post post = userEJB.createNewPost(user1, "Title", "Text text");
        userEJB.createNewPost(user2, "Title", "Text text");
        userEJB.createNewPost(user3, "Title", "Text text");
        userEJB.createNewPost(user4, "Title", "Text text");
        userEJB.createNewPost(user4, "Title", "Text text");
        userEJB.createNewPost(user1, "Title", "Text text");

        userEJB.createNewCommentOnPost(user1, post, "Comment!");
        userEJB.createNewCommentOnPost(user1, post, "Comment!");
        userEJB.createNewCommentOnPost(user3, post, "Comment!");

        List<User> users = userEJB.getMostActiveUsers();
        assertTrue(users.get(0).getId() == user1.getId());
        assertTrue(users.get(3).getId() == user2.getId());
    }

    @Test
    public void testConcurrency(){
        final int numberOfThreads = 4;
        final int rounds = 1_000;
        List<Thread> threads = new ArrayList<>();

        for(int i = 0; i < numberOfThreads; i++){
            int threadNumber = i;
            Runnable r = () -> {
                for(int j = 0; j < rounds; j++)
                    userEJB.createNewUser(("A" + threadNumber + "b" + j), "A", "Name",
                            null, User.CountryName.Albania, "email@email.com");
            };
            Thread t = new Thread(r);
            t.start();
            threads.add(t);
        }

        threads.stream().forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        });

        int expected = numberOfThreads * rounds;
        assertEquals(expected, userEJB.countUsers());
    }

    @Test
    public void testVotingOnPost(){
        User user = userEJB.createNewUser("AA", "A", "A", null, User.CountryName.Albania, "abc@abc.com");
        Post post = userEJB.createNewPost(user, "Title", "Text text");

        assertEquals(0, post.getUpVotes());
        userEJB.upVotePost(post);
        assertEquals(1, userEJB.findPost(post.getId()).getUpVotes());

        assertEquals(0, post.getDownVotes());
        userEJB.downVotePost(post);
        assertEquals(1, userEJB.findPost(post.getId()).getDownVotes());
    }

    @Test
    public void testVotingOnComment(){
        User user = userEJB.createNewUser("AA", "A", "A", null, User.CountryName.Albania, "abc@abc.com");
        Post post = userEJB.createNewPost(user, "Title", "Text text");
        Comment comment = userEJB.createNewCommentOnPost(user, post, "hei");

        assertEquals(0, comment.getUpVotes());
        userEJB.upVoteComment(comment);
        assertEquals(1, userEJB.findComment(comment.getId()).getUpVotes());

        assertEquals(0, comment.getDownVotes());
        userEJB.downVoteComment(comment);
        assertEquals(1, userEJB.findComment(comment.getId()).getDownVotes());
    }
}
