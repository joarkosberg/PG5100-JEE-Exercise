package com.joarkosberg.exercise.frontend.controller;

import com.joarkosberg.exercise.backend.entity.User;
import com.joarkosberg.exercise.backend.other.Statistics;

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

    public Long getNumberOfUpvotes(){
        return statistics.getNumberOfUpvotes();
    }

    public Long getNumberOfDownvotes(){
        return statistics.getNumberOfDownvotes();
    }

    public TreeMap<User.CountryName, Long> getCountriesByUsers() {
        return statistics.getCountriesByUsers();
    }
}
