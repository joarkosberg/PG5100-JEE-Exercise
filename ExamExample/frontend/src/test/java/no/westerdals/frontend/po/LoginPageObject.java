package no.westerdals.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPageObject extends PageObject {
    public LoginPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Login");
    }

    public HomePageObject login(String username, String password){
        setText("loginForm:username", username);
        setText("loginForm:password", password);

        WebElement weButton = getDriver().findElement(By.id("loginForm:login"));
        weButton.click();
        waitForPageToLoad();

        if(isOnPage())
            return null;
        else
            return new HomePageObject(getDriver());
    }
}
