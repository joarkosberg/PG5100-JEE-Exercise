package no.joarkosberg.exam.backend.entity;

import no.joarkosberg.exam.backend.validation.NotEmpty;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@NamedQueries({
        @NamedQuery(name = Post.GET_ALL_POSTS_BY_TIME, query =
                "select p " +
                        "from Post p " +
                        "order by p.created desc"),
        @NamedQuery(name = Post.GET_ALL_POSTS, query =
                "select p " +
                        "from Post p"),
        @NamedQuery(name = Post.GET_POSTS_BY_USER_ID, query =
                "select p " +
                        "from Post p " +
                        "where p.author = :author"),
        @NamedQuery(name = Post.COUNT_ALL_POSTS, query =
                "select count(*) " +
                        "from Post p")
})

@Entity
public class Post {
    public static final String GET_ALL_POSTS_BY_TIME = "GET_ALL_POSTS_BY_TIME";
    public static final String GET_ALL_POSTS = "GET_ALL_POSTS";
    public static final String GET_POSTS_BY_USER_ID = "GET_POSTS_BY_USER_ID";
    public static final String COUNT_ALL_POSTS = "COUNT_ALL_POSTS";

    @Id
    @GeneratedValue
    private long id;

    @NotEmpty
    @Size(min = 2, max = 512)
    private String text;

    /*
    Could use @Past but often causes problem since it's validated right after insert of new Date()
    Which often leads to constraint violation, because the check isn't that "exact".
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @NotEmpty
    @Size(min = 2, max = 32)
    private String author;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="VOTES")
    @MapKeyColumn(name="VOTER")
    private Map<String, Integer> votes;

    public Post(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Map<String, Integer> getVotes() {
        if(votes == null){
            votes = new HashMap<>();
        }
        return votes;
    }

    public void setVotes(Map<String, Integer> votes) {
        this.votes = votes;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
