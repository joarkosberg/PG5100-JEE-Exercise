package no.joarkosberg.exam.frontend.controller;

import no.joarkosberg.exam.backend.ejb.PostEJB;
import no.joarkosberg.exam.backend.entity.Comment;
import no.joarkosberg.exam.backend.entity.Post;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class PostPageController implements Serializable{

    @Inject
    private LoggingController loggingController;
    @EJB
    private PostEJB postEJB;

    private Post postProfile;
    private Map<Long, Integer> commentVotes;

    @PostConstruct
    public void init(){
        commentVotes = new HashMap<>();
    }

    public PostPageController (){
    }

    public String goToPostPage(long postId) {
        postProfile = postEJB.findPost(postId);
        return "post.jsf";
    }

    public List<Comment> getComments(){
        List <Comment> comments = postEJB.getCommentsByPostId(postProfile.getId());
        if(loggingController.isLoggedIn()) { //If user is logged in, check vote status for comments.
            String userId = loggingController.getActiveUser();
            for (Comment comment : comments) {
                if (comment.getVotes().containsKey(userId)) {
                    commentVotes.put(comment.getId(), comment.getVotes().get(userId));
                } else {
                    commentVotes.put(comment.getId(), 0);
                }
            }
        }
        return comments;
    }

    public boolean isAnyCommentsMadeForPost(){
        return postEJB.countCommentsByPostId(postProfile.getId()) > 0;
    }

    public String getCheckedComment(long commentId){
        Comment comment = postEJB.findComment(commentId);
        if(comment.isModerated())
            return "This comment has been moderated";
        else
            return comment.getText();
    }

    public void moderateComment(long commentId, boolean moderate) {
        postEJB.moderateComment(loggingController.getActiveUser(), commentId, moderate);
    }

    public Post getPostProfile() {
        return postProfile;
    }

    public void setPostProfile(Post postProfile) {
        this.postProfile = postProfile;
    }

    public Map<Long, Integer> getCommentVotes() {
        return commentVotes;
    }

    public void setCommentVotes(Map<Long, Integer> commentVotes) {
        this.commentVotes = commentVotes;
    }
}
