package controller;

import ejb.CommentEJB;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class CommentController implements Serializable {

    @Inject
    private CommentEJB commentEJB;

    private String formText;


}
