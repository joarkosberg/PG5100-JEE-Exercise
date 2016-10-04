package controller;

import entity.User;
import other.Statistics;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.TreeMap;

@Named
@RequestScoped
public class StatisticsController {

    @EJB
    private Statistics statistics;

    public Long getNumberOfUsers() {
        return statistics.getNumberOfUsers();
    }

    public Long getNumberOfPosts() {
        return statistics.getNumberOfPosts();
    }

    public Long getNumberOfComments() {
        return statistics.getNumberOfComments();
    }

    public TreeMap<User.CountryName, Long> getCountriesByUsers() {
        return statistics.getCountriesByUsers();
    }
}
