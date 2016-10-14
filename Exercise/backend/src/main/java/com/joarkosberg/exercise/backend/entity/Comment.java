package com.joarkosberg.exercise.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Comment {

    @Id @GeneratedValue
    private long id;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date created;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date updated;

    @NotNull
    @Size(min = 1, max = 1024)
    private String text;

    private int upVotes;
    private int downVotes;

    @ManyToOne
    private User commenter;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;

    public List<Comment> getComments() {
        if(comments == null){
            comments = new ArrayList<>();
        }
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User user) {
        this.commenter = user;
    }

    public User getCommenter(Comment comment){
        return comment.getCommenter();
    }
}
