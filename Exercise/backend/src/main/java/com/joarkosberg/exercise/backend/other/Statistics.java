package com.joarkosberg.exercise.backend.other;

import com.joarkosberg.exercise.backend.ejb.CommentEJB;
import com.joarkosberg.exercise.backend.ejb.PostEJB;
import com.joarkosberg.exercise.backend.ejb.UserEJB;
import com.joarkosberg.exercise.backend.entity.Comment;
import com.joarkosberg.exercise.backend.entity.Post;
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
    private Long numberOfUpvotes;
    private Long numberOfDownvotes;
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

        //Count votes
        numberOfUpvotes = Long.valueOf(0);
        numberOfDownvotes = Long.valueOf(0);


        List<Post> posts = postEJB.getAllPosts();
        List<Comment> comments = commentEJB.getAllComments();

        for(Post post : posts){
            numberOfUpvotes += post.getUpVotes();
            numberOfDownvotes += post.getDownVotes();
        }
        for(Comment comment : comments){
            numberOfUpvotes += comment.getUpVotes();
            numberOfDownvotes += comment.getDownVotes();
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

    public Long getNumberOfUpvotes() {
        return numberOfUpvotes;
    }

    public Long getNumberOfDownvotes() {
        return numberOfDownvotes;
    }

    public TreeMap<User.CountryName, Long> getCountriesByUsers() {
        return countriesByUsers;
    }
}
