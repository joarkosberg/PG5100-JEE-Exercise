package ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private EventEJB eventEJB;



    

}
