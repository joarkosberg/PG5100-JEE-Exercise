package com.joarkosberg.exercise.frontend.controller;

import com.joarkosberg.exercise.backend.entity.Post;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class PostSessionController implements Serializable {

    @Inject
    private PostController postController;

    private Post activePost;
    private boolean sortByPopularity;

    @PostConstruct
    public void init(){
        sortByPopularity = false;
    }

    public PostSessionController (){
    }

    public String showPost(Post post){
        activePost = postController.findPost(post.getId());
        return "postView.jsf";
    }

    public Post getActivePost() {
        return activePost;
    }

    public void setActivePost(Post activePost) {
        this.activePost = activePost;
    }

    public boolean isSortByPopularity() {
        return sortByPopularity;
    }

    public void setSortByPopularity(boolean sortByPopularity) {
        this.sortByPopularity = sortByPopularity;
    }
}
