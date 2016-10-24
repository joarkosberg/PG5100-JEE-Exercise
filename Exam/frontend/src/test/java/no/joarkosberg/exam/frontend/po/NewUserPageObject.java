package no.joarkosberg.exam.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class NewUserPageObject extends PageObject{

    public NewUserPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Create");
    }

    public HomePageObject createNewUser(String username, String password1, String password2){
        setText("registerForm:username", username);
        setText("registerForm:password", password1);
        setText("registerForm:confirmPassword", password2);
        setText("registerForm:firstName", "First");
        setText("registerForm:middleName", "Middle");
        setText("registerForm:lastName", "Last");

        WebElement weButton = getDriver().findElement(By.id("registerForm:create"));
        weButton.click();
        waitForPageToLoad();

        if(isOnPage())
            return null;
        else
            return new HomePageObject(getDriver());
    }
}
