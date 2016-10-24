package no.joarkosberg.exam.backend.ejb;

import no.joarkosberg.exam.backend.entity.Comment;
import no.joarkosberg.exam.backend.entity.Post;
import no.joarkosberg.exam.backend.entity.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class PostEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private UserEJB userEJB;

    public PostEJB (){
    }

    public synchronized long createNewPost(String userId, @NotNull String text){
        User u = userEJB.findUser(userId);
        if(u == null)
            throw new IllegalArgumentException("No user with username: " + userId);

        Post post = new Post();
        post.setAuthor(userId);
        post.setText(text);
        post.setCreated(new Date());
        em.persist(post);

        return post.getId();
    }


    public synchronized long createNewComment(String userId, long postId, @NotNull String text){
        User u = userEJB.findUser(userId);
        Post p = findPost(postId);

        if(u == null)
            throw new IllegalArgumentException("No user with username: " + userId);
        if(p == null)
            throw new IllegalArgumentException("No post with id: " + postId);

        Comment comment = new Comment();
        comment.setAuthor(userId);
        comment.setPostId(postId);
        comment.setText(text);
        comment.setModerated(false);
        comment.setCreated(new Date());
        em.persist(comment);

        return comment.getId();
    }

    public List<Post> getPostsSortedByTime(){
        Query query = em.createNamedQuery(Post.GET_ALL_POSTS_BY_TIME);
        return filterOutComments(query.getResultList());
    }

    public List<Post> getPostsSortedByScore(){
        Query query = em.createNamedQuery(Post.GET_ALL_POSTS);
        List<Post> posts = filterOutComments(query.getResultList());
        List<Post> sortedPosts = posts.stream()
                .sorted((p1, p2) -> Integer.compare(
                        p2.getVotes().values().stream().mapToInt(Integer::intValue).sum(),
                        p1.getVotes().values().stream().mapToInt(Integer::intValue).sum()))
                .collect(Collectors.toList());
        return sortedPosts;
    }

    public List<Post> getPostsByUserId(String userId){
        Query query = em.createNamedQuery(Post.GET_POSTS_BY_USER_ID);
        query.setParameter("author", userId);
        return filterOutComments(query.getResultList());
    }

    /*
    The method below is used since Comment extends Post and they are in a single table.
    Comment is a subclass of post so if i ask for posts i get comments also.
    Therefore i filter out comments from the posts.
     */
    public List<Post> filterOutComments(List<Post> posts){
        List<Long> commentIds = getAllComments().stream().map(Comment::getId).collect(Collectors.toList());
        return posts.stream().filter(p -> !commentIds.contains(p.getId())).collect(Collectors.toList());
    }

    public List<Comment> getAllComments(){
        Query query = em.createNamedQuery(Comment.GET_ALL_COMMENTS);
        return query.getResultList();
    }

    public List<Comment> getCommentsByUserId(String userId){
        Query query = em.createNamedQuery(Comment.GET_COMMENTS_BY_USER_ID);
        query.setParameter("author", userId);
        return query.getResultList();
    }

    public List<Comment> getCommentsByPostId(long postId){
        Query query = em.createNamedQuery(Comment.GET_COMMENTS_BY_POST_ID);
        query.setParameter("id", postId);
        return query.getResultList();
    }

    public int getScore(long id){
        Post post = findPost(id);
        if(post instanceof  Comment){
            Comment c = (Comment) post;
            if (c.isModerated())
                return -10;
        }
        return post.getVotes().values().stream().mapToInt(Integer::intValue).sum();
    }

    public void vote(long id, String userId, int value){
        if(userEJB.findUser(userId) == null)
            return;
        if(findPost(id) == null)
            return;

        if(value == 1 || value == 0 || value == -1){
            findPost(id).getVotes().put(userId, value);
        }
    }

    public long countPostsAndComments(){
        Query query = em.createNamedQuery(Post.COUNT_ALL_POSTS);
        return (long) query.getSingleResult();
    }

    public long countCommentsByPostId(long postId){
        Query query = em.createNamedQuery(Comment.COUNT_COMMENTS_BY_POST_ID);
        query.setParameter("id", postId);
        return (long) query.getSingleResult();
    }

    public void moderateComment(String userId, long commentId, boolean moderate){
        Comment comment = findComment(commentId);
        if(findPost(comment.getPostId()).getAuthor().equals(userId)){
            comment.setModerated(moderate);
        }
    }

    public Post findPost(long id){
        Post post = em.find(Post.class, id);
        return post;
    }

    public Comment findComment(long id){
        Comment comment = em.find(Comment.class, id);
        return comment;
    }
}
