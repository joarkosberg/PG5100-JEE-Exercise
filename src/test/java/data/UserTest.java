package data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.*;

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

    @Test
    public void testEmptyUser(){
        User user = new User();
        assertTrue(persistInATransaction(user));
    }




}
