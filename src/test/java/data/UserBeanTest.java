package data;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class UserBeanTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(User.class, Post.class, Comment.class, UserBean.class)
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserBean userBean;

    @Test
    public void testWith(){
        userBean.createNewUser("A", "su", User.CountryName.Albania, "abc@abc.com");
        userBean.createNewUser("B", "su", User.CountryName.Albania, "abc@abc.com");
        userBean.createNewUser("C", "su", User.CountryName.Albania, "abc@abc.com");

        long usersCount = userBean.countUsers();
        assertEquals(3, usersCount);
    }
}
