package entity;

import enums.CountryName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NamedQueries({
        @NamedQuery(name = Event.GET_ALL_EVENTS, query =
                "select e " +
                        "from Event e"),
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
    public static final String COUNT_ALL_EVENTS = "COUNT_ALL_EVENTS";
    public static final String DELETE_EVENT = "DELETE_EVENT";


    @Id @GeneratedValue
    private long id;

    @Size(min = 0, max = 50)
    private String title;

    @NotNull
    private CountryName country;

    @Size(min = 0, max = 80)
    private String location;

    @Size(min = 0, max = 256)
    private String description;

    @NotNull
    @ManyToOne
    private User poster;

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

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }
}
