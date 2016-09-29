package controller;

import ejb.UserEJB;
import entity.Post;
import entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class PostController implements Serializable{

    @Inject
    private UserEJB userEJB;

    private String formText;


    public String doPostText(User user){

        Post posted = userEJB.createNewPost(user, "Title", formText);

        if(posted != null){
            formText = "";
        }

        return "landingPage.jsf";
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }

    public String deletePost(long id){
        userEJB.deletePost(id);
        return "landingPage.jsf";
    }

    public List<Post> getAllPosts(){
        return userEJB.getAllPosts();
    }
}