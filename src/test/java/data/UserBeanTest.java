package data;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class UserBeanTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(User.class, Post.class, Comment.class, UserBean.class, DeleterEJB.class)
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserBean userBean;
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
        userBean.createNewUser("A", null, User.CountryName.Albania, "abc@abc.com");
        userBean.createNewUser("B", null, User.CountryName.Albania, "abc@abc.com");
        long usersCount = userBean.countUsers();
        assertEquals(2, usersCount);
    }

    @Test
    public void testCreatingNewPost(){
        User user = userBean.createNewUser("B", null, User.CountryName.Albania, "abc@abc.com");
        userBean.createNewPost(user, "Title", "Text text");
        long postCount = userBean.countPosts();
        assertEquals(1, postCount);
    }

    @Test
    public void testCreatingNewComments(){
        User user = userBean.createNewUser("B", null, User.CountryName.Albania, "abc@abc.com");
        Post post = userBean.createNewPost(user, "Title", "Text text");

        Comment comment = userBean.createNewCommentOnPost(user, post, "Comment!");
        userBean.createNewCommentOnComment(user, comment, "comment 2");

        long commentCount = userBean.countComments();
        assertEquals(2, userBean.getAllUsers().get(0).getComments().size());
        assertEquals(2, commentCount);
    }

    @Test
    public void testGetRepresentedCountries(){
        userBean.createNewUser("A", null, User.CountryName.Albania, "abc@abc.com");
        userBean.createNewUser("B", null, User.CountryName.Albania, "abc@abc.com");
        userBean.createNewUser("C", null, User.CountryName.China, "abc@abc.com");
        userBean.createNewUser("D", null, User.CountryName.Norway, "abc@abc.com");
        List<User.CountryName> countries= userBean.getRepresentedCountries();

        assertEquals(3, countries.size());
        assertNotNull(countries.stream().anyMatch(c -> c.equals(User.CountryName.Albania)));
    }

    @Test
    public void testCountingByCountry(){
        User user1 = userBean.createNewUser("A", null, User.CountryName.Albania, "abc@abc.com");
        User user2 = userBean.createNewUser("B", null, User.CountryName.Albania, "abc@abc.com");
        User user3 = userBean.createNewUser("C", null, User.CountryName.China, "abc@abc.com");
        User user4 = userBean.createNewUser("D", null, User.CountryName.Norway, "abc@abc.com");

        assertEquals(2, userBean.countOfUsersByCountry(User.CountryName.Albania));
        assertEquals(1, userBean.countOfUsersByCountry(User.CountryName.Norway));
        assertEquals(0, userBean.countOfUsersByCountry(User.CountryName.Brasil));

        userBean.createNewPost(user1, "Title", "Text text");
        userBean.createNewPost(user2, "Title", "Text text");
        userBean.createNewPost(user3, "Title", "Text text");
        userBean.createNewPost(user4, "Title", "Text text");
        userBean.createNewPost(user4, "Title", "Text text");
        userBean.createNewPost(user1, "Title", "Text text");

        assertEquals(3, userBean.countOfPostsByCountry(User.CountryName.Albania));
        assertEquals(2, userBean.countOfPostsByCountry(User.CountryName.Norway));
        assertEquals(0, userBean.countOfPostsByCountry(User.CountryName.Brasil));
    }

    @Test
    public void testGetMostActiveUsers(){
        User user1 = userBean.createNewUser("A", null, User.CountryName.Albania, "abc@abc.com");
        User user2 = userBean.createNewUser("B", null, User.CountryName.Albania, "abc@abc.com");
        User user3 = userBean.createNewUser("C", null, User.CountryName.China, "abc@abc.com");
        User user4 = userBean.createNewUser("D", null, User.CountryName.Norway, "abc@abc.com");

        Post post = userBean.createNewPost(user1, "Title", "Text text");
        userBean.createNewPost(user2, "Title", "Text text");
        userBean.createNewPost(user3, "Title", "Text text");
        userBean.createNewPost(user4, "Title", "Text text");
        userBean.createNewPost(user4, "Title", "Text text");
        userBean.createNewPost(user1, "Title", "Text text");

        userBean.createNewCommentOnPost(user1, post, "Comment!");
        userBean.createNewCommentOnPost(user1, post, "Comment!");
        userBean.createNewCommentOnPost(user3, post, "Comment!");

        List<User> users = userBean.getMostActiveUsers();
        assertTrue(users.get(0).getId() == user1.getId());
        assertTrue(users.get(3).getId() == user2.getId());
    }
}
