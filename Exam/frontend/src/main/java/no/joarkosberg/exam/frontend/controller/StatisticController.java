package no.joarkosberg.exam.frontend.controller;

import no.joarkosberg.exam.backend.ejb.Statistic;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class StatisticController {

    @EJB
    private Statistic statistic;

    public int getNumberOfUsers() {
        return statistic.getNumberOfUsers();
    }

    public int getNumberOfPosts() {
        return statistic.getNumberOfPosts();
    }

    public int getNumberOfComments() {
        return statistic.getNumberOfComments();
    }
}
