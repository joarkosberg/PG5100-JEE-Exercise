package no.joarkosberg.exam.frontend.controller;

import no.joarkosberg.exam.backend.ejb.PostEJB;
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
public class HomePageController implements Serializable{

    @Inject
    private LoggingController loggingController;
    @EJB
    private PostEJB postEJB;

    private boolean sort;
    private Map<Long, Integer> postVotes;

    @PostConstruct
    public void init(){
        sort = true;
        postVotes = new HashMap<>();
    }

    public HomePageController(){
    }

    public List<Post> getPosts(){
        List<Post> list = postEJB.getPostsSortedByTime();
        if(loggingController.isLoggedIn()) { //If user is logged in, check vote status for posts.
            String userId = loggingController.getActiveUser();
            for (Post post : list) {
                if (post.getVotes().containsKey(userId)) {
                    postVotes.put(post.getId(), post.getVotes().get(userId));
                } else {
                    postVotes.put(post.getId(), 0);
                }
            }
        }
        if(sort) return list;
        else return postEJB.getPostsSortedByScore();
    }

    public String getTruncatedText(long postId){
        String text = postEJB.findPost(postId).getText();
        if(text.length() > 30){
            return text.substring(0, 26) + "...";
        } else {
            return text;
        }
    }

    public boolean isAnyPostsMade(){
        return postEJB.countPostsAndComments() > 0;
    }

    public boolean isSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public Map<Long, Integer> getPostVotes() {
        return postVotes;
    }

    public void setPostVotes(Map<Long, Integer> postVotes) {
        this.postVotes = postVotes;
    }
}
