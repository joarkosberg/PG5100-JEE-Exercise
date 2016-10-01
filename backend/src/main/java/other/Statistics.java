package other;

import ejb.UserEJB;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
@Startup
public class Statistics {
    private final AtomicInteger counter = new AtomicInteger(0);

    @EJB
    private UserEJB userEJB;

    @Schedule(second = "10", minute="*", hour="*", persistent=false)
    public void doSomeComputation(){
        //TODO computation here

        /*
            Create a @Singleton with a time service that does query the database (eg every 10 seconds)
            for overall statistics, and save them internally in the bean. (This is done to avoid having
            100k users doing such expensive operation at the same time, ie we can consider this
            singleton as a type of cache.)
         */

    }
}
