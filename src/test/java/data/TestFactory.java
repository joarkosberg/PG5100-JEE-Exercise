package data;

import java.util.Date;

public class TestFactory {
    protected User getNewUser(String name, String email){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    protected Post getNewPost(String title, String text, Date created){
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        post.setCreated(created);
        return post;
    }

    protected Comment getNewComment(String text, Date created, Date updated){
        Comment comment = new Comment();
        comment.setText(text);
        comment.setCreated(created);
        comment.setUpdated(updated);
        return comment;
    }
}
