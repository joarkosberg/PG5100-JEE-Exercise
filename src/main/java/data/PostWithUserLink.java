package data;

import javax.persistence.*;

@Entity
public class PostWithUserLink extends Post{

    @ManyToOne
    private User poster;

    public PostWithUserLink(){}

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }
}
