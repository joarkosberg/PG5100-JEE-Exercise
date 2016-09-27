package data;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.Date;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@Ignore
public class PostTest {
    private EntityManagerFactory factory;
    private EntityManager em;
    private TestFactory testFactory;

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

    @Test
    public void testPostIsPersisted(){
        Post post = testFactory.getNewPost("title", "text", new Date());
        assertTrue(persistInATransaction(post));
    }
}
