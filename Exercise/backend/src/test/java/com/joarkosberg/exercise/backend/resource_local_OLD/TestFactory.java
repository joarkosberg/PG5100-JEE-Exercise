package com.joarkosberg.exercise.backend.resource_local_OLD;

import com.joarkosberg.exercise.backend.entity.Comment;
import com.joarkosberg.exercise.backend.entity.Post;
import com.joarkosberg.exercise.backend.entity.User;

import java.util.Date;

public class TestFactory {
    protected User getNewUser(String name, String email, User.CountryName country){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setCountry(country);
        return user;
    }

    protected Post getNewPost(String title, String text, Date created){
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        post.setCreated(created);
        return post;
    }

    protected Comment getNewComment(String text, Date created, Date updated){
        Comment comment = new Comment();
        comment.setText(text);
        comment.setCreated(created);
        comment.setUpdated(updated);
        return comment;
    }
}
