package no.westerdals.backend.entity;

import no.westerdals.backend.enums.CountryName;
import no.westerdals.backend.validation.Country;
import no.westerdals.backend.validation.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = User.GET_ALL_USERS, query =
                "select u " +
                        "from User u"),
        @NamedQuery(name = User.COUNT_ALL_USERS, query =
                "select count(*) " +
                        "from User u")
})

@Entity
public class User {
    public static final String GET_ALL_USERS = "GET_ALL_USERS";
    public static final String COUNT_ALL_USERS = "COUNT_ALL_USERS";

    @Id
    @Pattern(regexp = "[A-Za-z0-9]{2,32}")
    private String username;

    @NotEmpty
    @Size(min = 1, max = 28)
    private String first_name;

    @Size(max = 28)
    private String middle_name;

    @NotEmpty
    @Size(min = 1, max = 28)
    private String last_name;

    @NotEmpty
    private String hash;

    @NotEmpty
    @Size(max = 26)
    private String salt;

    @Country
    @Enumerated(EnumType.STRING)
    private CountryName country;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "attendingUsers")
    private List<Event> events;

    public User (){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public CountryName getCountry() {
        return country;
    }

    public void setCountry(CountryName country) {
        this.country = country;
    }

    public List<Event> getEvents() {
        if (events == null){
            events = new ArrayList<>();
        }
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
