package data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = User.GET_COUNTRIES, query =
                "select u.country " +
                        "from User u " +
                        "group by u.country"),
        @NamedQuery(name = User.GET_COUNT_OF_ALL_POSTS, query =
                "select count(*) " +
                        "from Post p"),
        @NamedQuery(name = User.GET_COUNT_OF_POSTS_BY_COUNTRY, query =
                "select sum(u.posts.size) " +
                        "from User u " +
                        "where u.country = :country"),
        @NamedQuery(name = User.GET_COUNT_OF_ALL_USERS, query =
                "select count(*)" +
                        "from User u"),
        @NamedQuery(name = User.GET_COUNT_OF_USERS_BY_COUNTRY, query =
                "select count(*)" +
                        "from User u " +
                        "where u.country = :country"),
        @NamedQuery(name = User.GET_MOST_ACTIVE_USERS, query =
                "select u " +
                        "from User u " +
                        "order by (u.posts.size+u.comments.size) desc")
})

@Entity
public class User {
    //Enum for countries
    public enum CountryName {
        Albania, Sweden, Norway, Denmark, Germany, France,
        Hungary, China, Brasil, Poland, Spain, Japan}

    //Queries
    public static final String GET_COUNTRIES = "GET_COUNTRIES";
    public static final String GET_COUNT_OF_ALL_POSTS = "GET_COUNT_OF_ALL_POSTS";
    public static final String GET_COUNT_OF_POSTS_BY_COUNTRY = "GET_COUNT_OF_POSTS_BY_COUNTRY";
    public static final String GET_COUNT_OF_ALL_USERS = "GET_COUNT_OF_ALL_USERS";
    public static final String GET_COUNT_OF_USERS_BY_COUNTRY = "GET_COUNT_OF_USERS_BY_COUNTRY";
    public static final String GET_MOST_ACTIVE_USERS = "GET_MOST_ACTIVE_USERS";

    @Id @GeneratedValue
    private long id;

    @NotNull
    @Size(min = 1, max = 64)
    private String name;

    @Size(min = 2, max = 64)
    private String surname;

    @Size(max = 128)
    private String adress;

    @NotNull
    @Pattern(regexp =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    @Enumerated(EnumType.STRING)
    private CountryName country;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments;

    public User(){
    }

    public List<Post> getPosts() {
        if(posts == null){
            posts = new ArrayList<>();
        }
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        if(comments == null){
            comments = new ArrayList<>();
        }
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CountryName getCountry() {
        return country;
    }

    public void setCountry(CountryName country) {
        this.country = country;
    }
}
