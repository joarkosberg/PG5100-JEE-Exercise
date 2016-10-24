package no.joarkosberg.exam.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PostPageObject extends PageObject {

    public PostPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("News");
    }

    public int getCountOfComments(){
        // Divide by 2 since there is table element inside each row (radio buttons create own table)
        return getDriver().findElements(By.xpath("//table[@id='commentTable']//tbody/tr")).size() / 2;
    }

    public void createNewComment (String text){
        setText("newCommentForm:text", text);
        WebElement weButton = getDriver().findElement(By.id("newCommentForm:create"));
        weButton.click();
        waitForPageToLoad();
    }

    public boolean canModerate(){
        List<WebElement> elements = getDriver().findElements(
                By.xpath("//table[@id='commentTable']//tbody" +
                        "/tr/td[5]"));
        return elements.size() > 0;
    }

    public void moderateComment(String commentText){
        List<WebElement> elements = getDriver().findElements(
                By.xpath("//table[@id='commentTable']//tbody" +
                        "/tr[contains(td[2], '" + commentText + "')]/td[5]//input[@type='checkbox']"));
        elements.get(0).click();
        waitForPageToLoad();
    }
}
