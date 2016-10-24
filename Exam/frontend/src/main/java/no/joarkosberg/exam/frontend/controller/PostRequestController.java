package no.joarkosberg.exam.frontend.controller;

import no.joarkosberg.exam.backend.ejb.PostEJB;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIInput;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class PostRequestController implements Serializable{
    private final int[] possibleValues = {-1, 0, 1};

    @EJB
    private PostEJB postEJB;
    @Inject
    private LoggingController loggingController;
    @Inject
    private PostPageController postPageController;

    private String formText;

    public PostRequestController(){
    }

    public String createNewPost(){
        try {
            postEJB.createNewPost(loggingController.getActiveUser(), formText);
        } catch(Exception e){}
        formText = "";
        return "home.jsf";
    }

    public String createNewComment(){
        try {
            postEJB.createNewComment(loggingController.getActiveUser(),
                    postPageController.getPostProfile().getId(), formText);
        } catch(Exception e){}
        formText = "";
        return "post.jsf";
    }

    public void radioButtonClicked(ValueChangeEvent valueChangeEvent) {
        long id = (Long) ((UIInput) valueChangeEvent.getSource()).getAttributes().get("inputId");
        int value = Integer.parseInt((String) valueChangeEvent.getNewValue());
        updateVote(id, value);
    }

    public void updateVote(long id, int value){
        postEJB.vote(id, loggingController.getActiveUser(), value);
    }

    public int getScore(long id){
        return postEJB.getScore(id);
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }

    public int[] getPossibleValues() {
        return possibleValues;
    }
}
