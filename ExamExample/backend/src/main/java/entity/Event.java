package entity;

import enums.CountryName;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Event {

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
