package data;

import org.junit.*;
import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserTest {
    private EntityManagerFactory factory;
    private EntityManager em;

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
        em = factory.createEntityManager();
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

    //TODO Use UserBean for more clarity
    private void addTestData(){
        //Users
        User a = new User();
        a.setName("A");
        a.setEmail("abc@abc.com");
        a.setCountry(CountryName.China);
        User b = new User();
        b.setName("B");
        b.setEmail("abc@abc.com");
        b.setCountry(CountryName.Albania);
        User c = new User();
        c.setName("C");
        c.setEmail("abc@abc.com");
        c.setCountry(CountryName.Norway);
        User d = new User();
        d.setName("D");
        d.setEmail("abc@abc.com");
        d.setCountry(CountryName.Norway);
        User e = new User();
        e.setName("E");
        e.setEmail("abc@abc.com");
        e.setCountry(CountryName.Norway);
        User f = new User();
        f.setName("F");
        f.setEmail("abc@abc.com");
        f.setCountry(CountryName.Albania);
        assertTrue(persistInATransaction(a,b,c,d,e,f));

        //Posts
        Post p1 = new Post();
        p1.setTitle("title");
        p1.setText("texttext");
        p1.setCreated(new Date());
        a.getPosts().add(p1);
        Post p2 = new Post();
        p2.setTitle("title");
        p2.setText("texttext");
        p2.setCreated(new Date());
        b.getPosts().add(p2);
        Post p3 = new Post();
        p3.setTitle("title");
        p3.setText("texttext");
        p3.setCreated(new Date());
        d.getPosts().add(p3);
        Post p4 = new Post();
        p4.setTitle("title");
        p4.setText("texttext");
        p4.setCreated(new Date());
        d.getPosts().add(p4);
        Post p5 = new Post();
        p5.setTitle("title");
        p5.setText("texttext");
        p5.setCreated(new Date());
        d.getPosts().add(p5);
        Post p6 = new Post();
        p6.setTitle("title");
        p6.setText("texttext");
        p6.setCreated(new Date());
        f.getPosts().add(p6);
        Post p7 = new Post();
        p7.setTitle("title");
        p7.setText("texttext");
        p7.setCreated(new Date());
        f.getPosts().add(p7);
        assertTrue(persistInATransaction(p1,p2,p3,p4,p5,p6,p7));

        //Comments
        Comment c1 = new Comment();
        c1.setText("text");
        c1.setCreated(new Date());
        c1.setUpdated(new Date());
        p1.getComments().add(c1);
        d.getComments().add(c1);
        Comment c2 = new Comment();
        c2.setText("text");
        c2.setCreated(new Date());
        c2.setUpdated(new Date());
        p4.getComments().add(c2);
        c.getComments().add(c2);
        Comment c3 = new Comment();
        c3.setText("text");
        c3.setCreated(new Date());
        c3.setUpdated(new Date());
        p3.getComments().add(c3);
        b.getComments().add(c3);
        assertTrue(persistInATransaction(c1,c2,c3));
    }

    @Test
    public void testUserIsPersisted(){
        User user = new User();
        user.setEmail("abc@abc.com");
        user.setName("Hei");
        assertTrue(persistInATransaction(user));
    }

    @Test
    public void testUserWithPost(){
        Post post = new Post();

        assertTrue(persistInATransaction(post));

        User user = new User();
        assertEquals(0, user.getPosts().size());

        user.setPosts(new ArrayList<>());
        user.getPosts().add(post);

        assertTrue(persistInATransaction(user));
        assertEquals(1, user.getPosts().stream().count());
    }

    @Test
    public void testUserWithComment(){
        Post post = new Post();
        assertTrue(persistInATransaction(post));

        User user = new User();
        assertEquals(0, user.getPosts().size());
        user.setPosts(new ArrayList<>());
        user.getPosts().add(post);

        assertTrue(persistInATransaction(user));
        assertEquals(1, user.getPosts().stream().count());

        Comment comment = new Comment();
        post.setComments(new ArrayList<>());
        post.getComments().add(comment);

        assertTrue(persistInATransaction(post, comment));
        assertEquals(1, post.getComments().stream().count());
    }

    @Test
    public void testJPQLActiveCountries(){
        addTestData();
        Query query = em.createNamedQuery(User.GET_COUNTRIES);

        List<CountryName> countries = query.getResultList();

        assertEquals(3, countries.size());
        assertTrue(countries.stream().anyMatch(c -> c.equals(CountryName.Norway)));
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
        query.setParameter("country", CountryName.Albania);

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
        query.setParameter("country", CountryName.Albania);

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
