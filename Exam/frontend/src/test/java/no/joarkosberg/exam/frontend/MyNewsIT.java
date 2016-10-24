package no.joarkosberg.exam.frontend;

import no.joarkosberg.exam.frontend.po.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyNewsIT extends WebTestBase{
    private HomePageObject home;

    @Before
    public void initPage(){
        assertTrue(JBossUtil.isJBossUpAndRunning());
        home = new HomePageObject(getDriver());
        home.toStartingPage();
        home.logout();
        assertTrue(home.isOnPage());
    }

    @Test
    public void testCreateNews(){
        LoginPageObject login = home.toLoginPage();
        assertTrue(login.isOnPage());
        NewUserPageObject newUser = login.toNewUserPage();
        assertTrue(newUser.isOnPage());

        String username = getUniqueId();
        newUser.createNewUser(username, "pass", "pass");
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(username));
        assertEquals(0, home.getCountOfPostsByUser(username));

        home.createNewPost("text");
        assertEquals(1, home.getCountOfPostsByUser(username));

        home.createNewPost("text");
        assertEquals(2, home.getCountOfPostsByUser(username));
    }

    @Test
    public void testNewsAfterLogout(){
        String username = getUniqueId();
        createAndLoginNewUser(username, "pass", home);

        home.createNewPost("text");
        home.createNewPost("text");
        assertEquals(2, home.getCountOfPostsByUser(username));

        home.logout();
        assertEquals(2, home.getCountOfPostsByUser(username));
    }

    @Test
    public void testUserDetails(){
        String username = getUniqueId();
        createAndLoginNewUser(username, "pass", home);

        String postText = getUniqueText();
        home.createNewPost(postText);

        UserPageObject userPage = home.toUserPage(postText);
        assertNotNull(userPage);
        assertTrue(userPage.isOnPage());
        assertTrue(userPage.containsUserId(username));
    }

    @Test
    public void testCanVote(){
        String username = getUniqueId();
        String password = "pass";
        createAndLoginNewUser(username, password, home);

        String postText = getUniqueText();
        home.createNewPost(postText);
        assertTrue(home.canVote());

        home.logout();
        assertFalse(home.canVote());

        loginUser(username, password, home);
        assertTrue(home.canVote());
    }

    @Test
    public void testScore(){
        String username = getUniqueId();
        createAndLoginNewUser(username, "pass", home);

        String postText = getUniqueText();
        home.createNewPost(postText);

        home.sortByTime(true);
        assertEquals(0, home.getFirstPostScore());

        home.voteForPost(postText, 1);
        assertEquals(1, home.getFirstPostScore());

        home.voteForPost(postText, -1);
        assertEquals(-1, home.getFirstPostScore());

        home.voteForPost(postText, 0);
        assertEquals(0, home.getFirstPostScore());
    }

    @Test
    public void testScoreWithTwoUsers(){
        String username = getUniqueId();
        createAndLoginNewUser(username, "pass", home);

        String postText = getUniqueText();
        home.createNewPost(postText);

        home.sortByTime(true);
        home.voteForPost(postText, 1);
        assertEquals(1, home.getPostScore(postText));

        home.logout();
        String username1 = getUniqueId();
        createAndLoginNewUser(username1, "pass", home);

        home.voteForPost(postText, 1);
        assertEquals(2, home.getPostScore(postText));
    }

    @Test
    public void testLongText(){
        String username = getUniqueId();
        createAndLoginNewUser(username, "pass", home);

        String longText = "K46R9t5gkFOQjbKwa0d3THC44tsJKXfvIIHuXtR6TgMJ54";
        home.createNewPost(longText);
        home.sortByTime(true);

        //By the rules stated earlier only the 26 first chars should be there if length > 30
        assertEquals(longText.substring(0,26) + "...", home.getFirstPostText());
    }

    @Test
    public void testSorting(){
        String username = getUniqueId();
        createAndLoginNewUser(username, "pass", home);

        String postText1 = getUniqueText();
        home.createNewPost(postText1);

        home.sortByTime(true);
        home.voteForPost(postText1, 1);

        String postText2 = getUniqueText();
        home.createNewPost(postText2);
        assertEquals(postText2, home.getFirstPostText());
        assertEquals(0, home.getFirstPostScore());

        home.sortByTime(false);

        /*
        Can check for getFirstPostScore(), but we don't know if another test has created a post with higher score.
        So this method makes it not rely tests, by just checking which post is ranked highest in the list.
         */
        assertTrue(home.isPostSortedHigher(postText1, postText2));

        home.sortByTime(true);
        assertEquals(postText2, home.getFirstPostText());
        assertFalse(home.isPostSortedHigher(postText1, postText2));
    }

    @Test
    public void testCreateComment(){
        String username = getUniqueId();
        createAndLoginNewUser(username, "pass", home);

        String postText = getUniqueText();
        home.createNewPost(postText);

        PostPageObject post = home.toPostPage(postText);
        assertTrue(post.isOnPage());
        assertEquals(0, post.getCountOfComments());

        post.createNewComment(getUniqueText());
        post.createNewComment(getUniqueText());
        post.createNewComment(getUniqueText());
        assertEquals(3, post.getCountOfComments());
    }

    @Test
    public void testCanModerate(){
        String username = getUniqueId();
        String password = "pass";
        createAndLoginNewUser(username, password, home);

        String postText = getUniqueText();
        home.createNewPost(postText);

        PostPageObject post = home.toPostPage(postText);
        post.createNewComment(getUniqueText());
        assertTrue(post.canModerate());

        post.logout();
        home.toPostPage(postText);
        assertFalse(post.canModerate());

        post.toHomePage();
        String username1 = getUniqueId();
        createAndLoginNewUser(username1, "pass", home);
        home.toPostPage(postText);
        assertFalse(post.canModerate());

        post.logout();
        loginUser(username, password, home);
        home.toPostPage(postText);
        assertTrue(post.canModerate());
    }

    @Test
    public void testKarma(){
        String username = getUniqueId();
        createAndLoginNewUser(username, "pass", home);

        String postText = getUniqueText();
        home.createNewPost(postText);
        home.voteForPost(postText, 1);

        PostPageObject post = home.toPostPage(postText);
        String commentText1 = getUniqueText();
        String commentText2 = getUniqueText();

        post.createNewComment(commentText1);
        post.createNewComment(commentText2);

        post.moderateComment(commentText1);
        post.moderateComment(commentText2);

        post.toHomePage();
        UserPageObject user = home.toUserPage(postText);
        assertEquals(-19, user.getKarma());
    }

    /*
    Extra tests i have added
     */
    @Test
    public void testNavigateToStatistics(){
        StatisticPageObject statistic = home.toStatisticPage();
        assertTrue(statistic.isOnPage());
    }
}
