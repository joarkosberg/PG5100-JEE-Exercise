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
        deleterEJB.deleteEntities(Comment.class);
        deleterEJB.deleteEntities(Post.class);
        deleterEJB.deleteEntities(User.class);
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
        userBean.createNewUser("B", null, User.CountryName.Albania, "abc@abc.com");
        User user = userBean.getAllUsers().get(0);
        userBean.createNewPost(user, "Title", "Text text");
        long postCount = userBean.countPosts();
        assertEquals(1, postCount);
    }

    @Test
    public void testCreatingNewComments(){
        userBean.createNewUser("B", null, User.CountryName.Albania, "abc@abc.com");
        User user = userBean.getAllUsers().get(0);

        userBean.createNewPost(user, "Title", "Text text");
        Post post = userBean.getAllPosts().get(0);

        userBean.createNewCommentOnPost(user, post, "Comment!");
        Comment comment = userBean.getAllComments().get(0);

        userBean.createNewCommentOnComment(user, comment, "comment 2");

        long commentCount = userBean.countComments();
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

}
