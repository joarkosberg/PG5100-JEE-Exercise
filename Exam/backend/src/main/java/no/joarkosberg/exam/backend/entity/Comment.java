package no.joarkosberg.exam.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NamedQueries({
        @NamedQuery(name = Comment.GET_ALL_COMMENTS, query =
                "select c " +
                        "from Comment c"),
        @NamedQuery(name = Comment.GET_COMMENTS_BY_USER_ID, query =
                "select c " +
                        "from Comment c " +
                        "where c.author = :author"),
        @NamedQuery(name = Comment.GET_COMMENTS_BY_POST_ID, query =
                "select c " +
                        "from Comment c " +
                        "where c.postId = :id"),
        @NamedQuery(name = Comment.COUNT_COMMENTS_BY_POST_ID, query =
                "select count(*) " +
                        "from Comment c " +
                        "where c.postId = :id")
})

@Entity
public class Comment extends Post{
    public static final String GET_ALL_COMMENTS = "GET_ALL_COMMENTS";
    public static final String GET_COMMENTS_BY_USER_ID = "GET_COMMENTS_BY_USER_ID";
    public static final String GET_COMMENTS_BY_POST_ID = "GET_COMMENTS_BY_POST_ID";
    public static final String COUNT_COMMENTS_BY_POST_ID = "COUNT_COMMENTS_BY_POST_ID";

    private boolean moderated;

    @NotNull
    private Long postId;

    public Comment(){
    }

    public boolean isModerated() {
        return moderated;
    }

    public void setModerated(boolean moderated) {
        this.moderated = moderated;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }
}
