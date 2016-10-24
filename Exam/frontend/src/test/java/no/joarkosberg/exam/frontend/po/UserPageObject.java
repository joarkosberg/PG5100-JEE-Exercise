package no.joarkosberg.exam.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class UserPageObject extends PageObject {

    public UserPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("User");
    }

    public boolean containsUserId(String userId){
        List<WebElement> list = getDriver().findElements(By.xpath("//*[text()[contains(.,'" + userId + "')]]"));
        return list.size() > 0;
    }

    public int getKarma(){
        List<WebElement> elements = getDriver().findElements(By.id("karma"));
        return Integer.parseInt(elements.get(0).getText());
    }
}
