package com.joarkosberg.exercise.backend.ejb;

import com.joarkosberg.exercise.backend.helpers.DeleterEJB;
import com.joarkosberg.exercise.backend.entity.Comment;
import com.joarkosberg.exercise.backend.entity.Post;
import com.joarkosberg.exercise.backend.entity.User;
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

import static org.junit.Assert.*;


@RunWith(Arquillian.class)
public class UserEJBTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "com.joarkosberg.exercise.backend")
                .addClass(DeleterEJB.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserEJB userEJB;
    @EJB
    private PostEJB postEJB;
    @EJB
    private CommentEJB commentEJB;
    @EJB
    private DeleterEJB deleterEJB;

    @Before
    @After
    public void clearDatabase(){
        deleterEJB.deleteEntities(Post.class);
        deleterEJB.deleteEntities(Comment.class);
        deleterEJB.deleteEntities(User.class);
    }

    @Test
    public void testCreatingNewUsers(){
        userEJB.createNewUser("AA", "A", "A", null, User.CountryName.Albania, "abc@abc.com");
        userEJB.createNewUser("BB", "B", "B", null, User.CountryName.Albania, "abc@abc.com");
        long usersCount = userEJB.countUsers();
        assertEquals(2, usersCount);
    }

    @Test
    public void testUserConstraints(){
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

        postEJB.createNewPost(user1, "Title", "Text text");
        postEJB.createNewPost(user2, "Title", "Text text");
        postEJB.createNewPost(user3, "Title", "Text text");
        postEJB.createNewPost(user4, "Title", "Text text");
        postEJB.createNewPost(user4, "Title", "Text text");
        postEJB.createNewPost(user1, "Title", "Text text");

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

        Post post = postEJB.createNewPost(user1, "Title", "Text text");
        postEJB.createNewPost(user2, "Title", "Text text");
        postEJB.createNewPost(user3, "Title", "Text text");
        postEJB.createNewPost(user4, "Title", "Text text");
        postEJB.createNewPost(user4, "Title", "Text text");
        postEJB.createNewPost(user1, "Title", "Text text");

        commentEJB.createNewCommentOnPost(user1, post, "Comment!");
        commentEJB.createNewCommentOnPost(user1, post, "Comment!");
        commentEJB.createNewCommentOnPost(user3, post, "Comment!");

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
    public void getNullWhenAskingForNonExistUser(){
        User user = userEJB.findUserByUserName("heihei");
        assertNull(user);
    }
}
