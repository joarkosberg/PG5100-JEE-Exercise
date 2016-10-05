package po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginObject extends PageObject {


    public LoginObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Login / Create New User");
    }


    public void toStartingPage(){
        getDriver().get(getBaseUrl());
        waitForPageToLoad();
    }


    public void changeData(String value){
        WebElement text = getDriver().findElement(By.id("form:text"));
        WebElement button = getDriver().findElement(By.id("form:modify"));

        text.clear();
        text.sendKeys(value);
        button.click();
        waitForPageToLoad();
    }
}
