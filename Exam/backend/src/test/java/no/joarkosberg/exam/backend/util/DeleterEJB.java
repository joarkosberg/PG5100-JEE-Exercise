package no.joarkosberg.exam.backend.util;

import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class DeleterEJB {

    @PersistenceContext
    private EntityManager em;

    public void deleteEntities(Class<?> entity){
        if(entity == null || entity.getAnnotation(Entity.class) == null){
            throw new IllegalArgumentException("Invalid non-entity class");
        }

        /*
        Needed to change this part to work with @ElementCollection
        (where cascade only works when deleting with EntityManager and not with JPQL which the previous version used
         */
        String name = entity.getSimpleName();
        Query query = em.createQuery("select a from " + name + " a ");
        query.getResultList().forEach(e -> em.remove(e));
    }
}
