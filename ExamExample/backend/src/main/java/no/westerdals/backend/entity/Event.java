package no.westerdals.backend.entity;

import no.westerdals.backend.enums.CountryName;
import no.westerdals.backend.validation.Country;
import no.westerdals.backend.validation.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = Event.GET_ALL_EVENTS, query =
                "select e " +
                        "from Event e"),
        @NamedQuery(name = Event.GET_EVENTS_BY_COUNTRY, query =
                "select e " +
                        "from Event e " +
                        "where e.country = :country"),
        @NamedQuery(name = Event.COUNT_ALL_EVENTS, query =
                "select count(*) " +
                        "from Event e"),
        @NamedQuery(name = Event.DELETE_EVENT, query =
                "delete " +
                        "from Event e " +
                        "where e.id = :id")
})

@Entity
public class Event {
    public static final String GET_ALL_EVENTS = "GET_ALL_EVENTS";
    public static final String GET_EVENTS_BY_COUNTRY = "GET_EVENTS_BY_COUNTRY";
    public static final String COUNT_ALL_EVENTS = "COUNT_ALL_EVENTS";
    public static final String DELETE_EVENT = "DELETE_EVENT";

    @Id
    @GeneratedValue
    private long id;

    @NotEmpty
    @Size(min = 1, max = 64)
    private String title;

    @Country
    @Enumerated(EnumType.STRING)
    private CountryName country;

    @NotEmpty
    @Size(min = 1, max = 128)
    private String location;

    @NotEmpty
    @Size(min = 2, max = 512)
    private String description;

    @NotEmpty
    @Size(min = 1, max = 32)
    private String author;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> attendingUsers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CountryName getCountry() {
        return country;
    }

    public void setCountry(CountryName country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<User> getAttendingUsers() {
        if(attendingUsers == null){
            attendingUsers = new ArrayList<>();
        }
        return attendingUsers;
    }

    public void setAttendingUsers(List<User> attendingUsers) {
        this.attendingUsers = attendingUsers;
    }
}
