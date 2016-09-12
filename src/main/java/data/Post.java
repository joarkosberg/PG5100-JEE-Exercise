package data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue
    private long id;

    private Date created;
    private String title;
    private String text;
    private int upVotes;
    private int downVotes;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "commenter", cascade = CascadeType.ALL)
    private List<CommentWithPostLink> commentsWithPostLink;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<CommentWithPostLink> getCommentsWithPostLink() {
        return commentsWithPostLink;
    }

    public void setCommentsWithPostLink(List<CommentWithPostLink> commentsWithPostLink) {
        this.commentsWithPostLink = commentsWithPostLink;
    }
}
