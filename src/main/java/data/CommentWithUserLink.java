package data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class CommentWithUserLink extends Comment {

    @ManyToOne
    private User commenter;

    public CommentWithUserLink(){}

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }
}