package data;

import org.junit.*;
import javax.persistence.*;
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

    private void addTestData(){
        //Users
        User a = new User();
        a.setName("A");
        a.setCountry(CountryName.China);
        User b = new User();
        b.setName("B");
        b.setCountry(CountryName.Albania);
        User c = new User();
        c.setName("C");
        c.setCountry(CountryName.Norway);
        User d = new User();
        d.setName("D");
        d.setCountry(CountryName.Norway);
        User e = new User();
        e.setName("E");
        e.setCountry(CountryName.Norway);
        User f = new User();
        f.setName("F");
        f.setCountry(CountryName.Albania);
        assertTrue(persistInATransaction(a,b,c,d,e,f));

        //Posts
        Post p1 = new Post();
        a.getPosts().add(p1);
        Post p2 = new Post();
        b.getPosts().add(p2);
        Post p3 = new Post();
        d.getPosts().add(p3);
        Post p4 = new Post();
        d.getPosts().add(p4);
        Post p5 = new Post();
        d.getPosts().add(p5);
        Post p6 = new Post();
        f.getPosts().add(p6);
        Post p7 = new Post();
        f.getPosts().add(p7);
        assertTrue(persistInATransaction(p1,p2,p3,p4,p5,p6,p7));

        //Comments
        Comment c1 = new Comment();
        p1.getComments().add(c1);
        d.getComments().add(c1);
        Comment c2 = new Comment();
        p4.getComments().add(c2);
        c.getComments().add(c2);
        Comment c3 = new Comment();
        p3.getComments().add(c3);
        b.getComments().add(c3);
        assertTrue(persistInATransaction(c1,c2,c3));
    }

    @Test
    public void testUserIsPersisted(){
        User user = new User();
        assertTrue(persistInATransaction(user));
    }

    @Test
    public void testUserWithPost(){
        Post post = new Post();
        PostWithUserLink postWithUserLink = new PostWithUserLink();

        assertTrue(persistInATransaction(post));
        assertTrue(persistInATransaction(postWithUserLink));
        assertNull(postWithUserLink.getPoster());

        User user = new User();
        assertEquals(0, user.getPosts().size());

        user.setPosts(new ArrayList<>());
        user.getPosts().add(post);
        postWithUserLink.setPoster(user);

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
