package data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PostTest {
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

    @Test
    public void testPostIsPersisted(){
        Post post = new Post();
        PostWithUserLink postWithUserLink = new PostWithUserLink();

        //being an entity, and not an embedded element, can directly save to database
        assertTrue(persistInATransaction(post));
        assertTrue(persistInATransaction(postWithUserLink));

        assertNull(postWithUserLink.getPoster());
    }
}
