package data;

import org.junit.*;
import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class UserTest {
    private EntityManagerFactory factory;
    private EntityManager em;
    private TestFactory testFactory;
    private UserBean userBean;

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("TestDB-Normal");
        em = factory.createEntityManager();
        testFactory = new TestFactory();
    }

    @After
    public void tearDown() {
        em.close();
        factory.close();
    }

    private boolean persistInATransaction(Object... obj) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for(Object o : obj) {
                em.persist(o);
            }
            tx.commit();
        } catch (Exception e) {
            System.out.println("FAILED TRANSACTION: " + e.toString());
            tx.rollback();
            return false;
        }

        return true;
    }

    private void addTestData(){
        //Users
        User a = testFactory.getNewUser("A", "abc@abc.com", User.CountryName.China);
        User b = testFactory.getNewUser("B", "abc@abc.com", User.CountryName.Albania);
        User c = testFactory.getNewUser("C", "abc@abc.com", User.CountryName.Norway);
        User d = testFactory.getNewUser("D", "abc@abc.com", User.CountryName.Norway);
        User e = testFactory.getNewUser("E", "abc@abc.com", User.CountryName.Norway);
        User f = testFactory.getNewUser("F", "abc@abc.com", User.CountryName.Albania);
        assertTrue(persistInATransaction(a,b,c,d,e,f));

        //Posts
        Post p1 = testFactory.getNewPost("title", "texttext", new Date());
        a.getPosts().add(p1);
        Post p2 = testFactory.getNewPost("title", "texttext", new Date());
        b.getPosts().add(p2);
        Post p3 = testFactory.getNewPost("title", "texttext", new Date());
        d.getPosts().add(p3);
        Post p4 = testFactory.getNewPost("title", "texttext", new Date());
        d.getPosts().add(p4);
        Post p5 = testFactory.getNewPost("title", "texttext", new Date());
        d.getPosts().add(p5);
        Post p6 = testFactory.getNewPost("title", "texttext", new Date());
        f.getPosts().add(p6);
        Post p7 = testFactory.getNewPost("title", "texttext", new Date());
        f.getPosts().add(p7);
        assertTrue(persistInATransaction(p1,p2,p3,p4,p5,p6,p7));

        //Comments
        Comment c1 = testFactory.getNewComment("test", new Date(), new Date());
        p1.getComments().add(c1);
        d.getComments().add(c1);
        Comment c2 = testFactory.getNewComment("test", new Date(), new Date());
        p4.getComments().add(c2);
        c.getComments().add(c2);
        Comment c3 = testFactory.getNewComment("test", new Date(), new Date());
        p3.getComments().add(c3);
        b.getComments().add(c3);
        assertTrue(persistInATransaction(c1,c2,c3));
    }

    @Test
    public void testUserIsPersisted(){
        User user = testFactory.getNewUser("name", "email@em.em", User.CountryName.China);
        assertTrue(persistInATransaction(user));
    }

    @Test
    public void testUserWithPost(){
        Post post = testFactory.getNewPost("title", "text", new Date());
        assertTrue(persistInATransaction(post));

        User user = testFactory.getNewUser("Name", "abc@abc.com", User.CountryName.China);
        assertEquals(0, user.getPosts().size());

        user.setPosts(new ArrayList<>());
        user.getPosts().add(post);

        assertTrue(persistInATransaction(user));
        assertEquals(1, user.getPosts().stream().count());
    }

    @Test
    public void testUserWithComment(){
        Post post = testFactory.getNewPost("Text", "text", new Date());
        assertTrue(persistInATransaction(post));

        User user = testFactory.getNewUser("Name", "em@em.em", User.CountryName.China);
        assertEquals(0, user.getPosts().size());
        user.setPosts(new ArrayList<>());
        user.getPosts().add(post);

        assertTrue(persistInATransaction(user));
        assertEquals(1, user.getPosts().stream().count());

        Comment comment = testFactory.getNewComment("text", new Date(), new Date());
        post.setComments(new ArrayList<>());
        post.getComments().add(comment);

        assertTrue(persistInATransaction(post, comment));
        assertEquals(1, post.getComments().stream().count());
    }

    @Test
    public void testJPQLActiveCountries(){
        addTestData();
        Query query = em.createNamedQuery(User.GET_COUNTRIES);

        List<User.CountryName> countries = query.getResultList();

        assertEquals(3, countries.size());
        assertTrue(countries.stream().anyMatch(c -> c.equals(User.CountryName.Norway)));
    }

    @Test
    public void testJPQLNumberOfAllPosts(){
        addTestData();
        Query query = em.createNamedQuery(User.GET_COUNT_OF_ALL_POSTS);

        List<Long> posts = query.getResultList();

        assertEquals(Long.valueOf(7), posts.get(0));
    }

    @Test
    public void testJPQLNumberOfPostsByCountry(){
        addTestData();
        Query query = em.createNamedQuery(User.GET_COUNT_OF_POSTS_BY_COUNTRY);
        query.setParameter("country", User.CountryName.Albania);

        List<Long> postsPerCountry = query.getResultList();

        assertEquals(Long.valueOf(3), postsPerCountry.get(0));
    }

    @Test
    public void testJPQLNumberOfAllUsers(){
        addTestData();
        Query query = em.createNamedQuery(User.GET_COUNT_OF_ALL_USERS);

        List<Long> users = query.getResultList();

        assertEquals(Long.valueOf(6), users.get(0));
    }

    @Test
    public void testJPQLNumberOfUsersByCountry(){
        addTestData();
        Query query = em.createNamedQuery(User.GET_COUNT_OF_USERS_BY_COUNTRY);
        query.setParameter("country", User.CountryName.Albania);

        List<Long> usersByCountry = query.getResultList();

        assertEquals(Long.valueOf(2), usersByCountry.get(0));
    }

    @Test
    public void testJPQLGetMostActiveUsers(){
        addTestData();
        Query query = em.createNamedQuery(User.GET_MOST_ACTIVE_USERS);

        List<User> topUsers = query.getResultList();

        assertEquals("D", topUsers.get(0).getName());
    }
}
