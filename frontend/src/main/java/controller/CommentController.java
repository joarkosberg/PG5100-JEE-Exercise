package controller;

import ejb.CommentEJB;
import entity.Comment;
import entity.Post;
import entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class CommentController implements Serializable{

    @Inject
    private CommentEJB commentEJB;

    private String formText;

    public String commentOnPost(User user, Post post){
        Comment commented = commentEJB.createNewCommentOnPost(user, post, formText);
        if(commented != null){
            formText = "";
        }
        return "landingPage.jsf";
    }

    /* Not in use
    public String commentOnComment(User user, Comment comment){
        Comment commented = commentEJB.createNewCommentOnComment(user, comment, formText);
        if(commented != null){
            formText = "";
        }
        return "landingPage.jsf";
    }
    */

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }

    public String deleteComment(long id){
        commentEJB.deleteComment(id);
        return "landingPage.jsf";
    }

    public List<Comment> getAllComments(){
        return commentEJB.getAllComments();
    }
}
