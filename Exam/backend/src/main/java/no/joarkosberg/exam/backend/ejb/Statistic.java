package no.joarkosberg.exam.backend.ejb;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class Statistic {
    private int numberOfUsers;
    private int numberOfPosts;
    private int numberOfComments;

    @EJB
    private UserEJB userEJB;
    @EJB
    private PostEJB postEJB;

    @Schedule(second = "*/10", minute="*", hour="*", persistent=false)
    public void doSomeComputation(){
        numberOfUsers = (int) userEJB.countAllUsers();
        numberOfPosts = postEJB.getPostsSortedByTime().size();
        numberOfComments = postEJB.getAllComments().size();
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public int getNumberOfPosts() {
        return numberOfPosts;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }
}
