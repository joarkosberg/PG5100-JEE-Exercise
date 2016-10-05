package controller;

import ejb.EventEJB;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class EventController implements Serializable {

    @Inject
    private EventEJB eventEJB;


}
