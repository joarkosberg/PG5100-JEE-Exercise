package com.joarkosberg.exercise.backend.other;

import com.joarkosberg.exercise.backend.ejb.CommentEJB;
import com.joarkosberg.exercise.backend.ejb.PostEJB;
import com.joarkosberg.exercise.backend.ejb.UserEJB;
import com.joarkosberg.exercise.backend.entity.User;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.List;
import java.util.TreeMap;

@Singleton
@Startup
public class Statistics {
    private Long numberOfUsers;
    private Long numberOfPosts;
    private Long numberOfComments;
    private TreeMap<User.CountryName, Long> countriesByUsers = new TreeMap<>();

    @EJB
    private UserEJB userEJB;
    @EJB
    private PostEJB postEJB;
    @EJB
    private CommentEJB commentEJB;

    @Schedule(second = "*/10", minute="*", hour="*", persistent=false)
    public void doSomeComputation(){
        //Get count of all tables
        numberOfUsers = userEJB.countUsers();
        numberOfPosts = postEJB.countPosts();
        numberOfComments = commentEJB.countComments();

        //Get number of users based on country
        List<User.CountryName> countries = userEJB.getRepresentedCountries();
        for(User.CountryName country : countries) {
            countriesByUsers.put(country, userEJB.countOfUsersByCountry(country));
        }
    }


    public Long getNumberOfUsers() {
        return numberOfUsers;
    }

    public Long getNumberOfPosts() {
        return numberOfPosts;
    }

    public Long getNumberOfComments() {
        return numberOfComments;
    }

    public TreeMap<User.CountryName, Long> getCountriesByUsers() {
        return countriesByUsers;
    }
}
