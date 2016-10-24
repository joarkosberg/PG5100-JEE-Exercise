package no.joarkosberg.exam.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class HomePageObject extends PageObject{

    public HomePageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Home");
    }

    public void toStartingPage() {
        getDriver().get(getBaseUrl() + "/mynews/home.jsf");
        waitForPageToLoad();
    }

    public LoginPageObject toLoginPage(){
        getDriver().findElement(By.id("loginButton")).click();
        waitForPageToLoad();
        return new LoginPageObject(getDriver());
    }


    public int getCountOfPostsByUser(String username) {
        return getDriver().findElements(
                By.xpath("//table[@id='postTable']//tbody" +
                        "/tr[contains(td[2], '" + username + "')]")).size();
    }

    public void createNewPost (String text){
        setText("newPostForm:text", text);
        WebElement weButton = getDriver().findElement(By.id("newPostForm:create"));
        weButton.click();
        waitForPageToLoad();
    }

    public UserPageObject toUserPage(String postText){
        List<WebElement> elements = getDriver().findElements(
                By.xpath("//table[@id='postTable']//tbody" +
                        "/tr[contains(td[3], '" + postText + "')]/td[2]"));

        if(elements.isEmpty())
            return null;

        elements.get(0).click();
        waitForPageToLoad();

        if(isOnPage())
            return null;
        else
            return new UserPageObject(getDriver());
    }

    public PostPageObject toPostPage(String postText){
        List<WebElement> elements = getDriver().findElements(
                By.xpath("//table[@id='postTable']//tbody" +
                        "/tr[contains(td[3], '" + postText + "')]/td[3]"));

        if(elements.isEmpty())
            return null;

        elements.get(0).click();
        waitForPageToLoad();

        if(isOnPage())
            return null;
        else
            return new PostPageObject(getDriver());
    }

    public boolean canVote(){
        List<WebElement> elements = getDriver().findElements(
                By.xpath("//table[@id='postTable']//tbody" +
                        "/tr[contains(td[5], '1')]"));
        return elements.size() > 0;
    }

    public void sortByTime (boolean sort){
        List<WebElement> elements = getDriver().findElements(By.id("selectSortingForm:selectSorting"));
        Select select = new Select(elements.get(0));

        WebElement option = select.getFirstSelectedOption();
        String selected = option.getText();

        if(sort && !selected.equals("Time"))
            select.selectByVisibleText("Time");
        if(!sort && !selected.equals("Score"))
            select.selectByVisibleText("Score");
    }

    public int getFirstPostScore(){
        List<WebElement> elements = getDriver().findElements(
                By.xpath("//table[@id='postTable']//tbody" +
                        "/tr/td[4]"));

        return Integer.parseInt(elements.get(0).getText());
    }

    public String getFirstPostText(){
        List<WebElement> elements = getDriver().findElements(
                By.xpath("//table[@id='postTable']//tbody" +
                        "/tr/td[3]"));

        return elements.get(0).getText();
    }

    public int getPostScore(String postText){
        List<WebElement> elements = getDriver().findElements(
                By.xpath("//table[@id='postTable']//tbody" +
                        "/tr[contains(td[3], '" + postText + "')]/td[4]"));

        return Integer.parseInt(elements.get(0).getText());
    }

    public void voteForPost(String postText, int value){
        List<WebElement> radios = getDriver().findElements(
                By.xpath("//table[@id='postTable']//tbody" +
                        "/tr[contains(td[3], '" + postText + "')]/td[5]//input[@type='radio']"));
        radios.get(value + 1).click();
    }

    public Boolean isPostSortedHigher(String p1, String p2){
        List<WebElement> elements = getDriver().findElements(
                By.xpath("//table[@id='postTable']//tbody" +
                        "/tr/td[3]"));

        for(WebElement e : elements){
            if(e.getText().equals(p1))
                return true;
            else if(e.getText().equals(p2))
                return false;
        }
        return null;
    }
}
