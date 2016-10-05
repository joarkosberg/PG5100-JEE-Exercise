package entity;

import enums.CountryName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class User {

    @Id
    @Size(min = 2, max = 30)
    private String username;

    @NotNull
    @Size(min = 1, max = 28)
    private String first_name;

    @Size(min = 1, max = 28)
    private String middle_name;

    @NotNull
    @Size(min = 1, max = 28)
    private String last_name;

    @NotNull
    private String hash;

    @NotNull
    @Size(max = 26)
    private String salt;

    @NotNull
    private CountryName country;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poster")
    private List<Event> events;

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
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
