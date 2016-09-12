package data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class CommentWithPostLink extends Comment {

    @ManyToOne
    private User commenter;

    public CommentWithPostLink(){}

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }
}
