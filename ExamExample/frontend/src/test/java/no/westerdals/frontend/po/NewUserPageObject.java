package no.westerdals.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class NewUserPageObject extends PageObject {
    public NewUserPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Create User");
    }

    public HomePageObject createNewUser (String username, String password, String confirmPassword){
        setText("registerForm:username", username);
        setText("registerForm:password", password);
        setText("registerForm:confirmPassword", confirmPassword);
        setText("registerForm:firstName", "First");
        setText("registerForm:middleName", "Middle");
        setText("registerForm:lastName", "Last");

        //Select Country
        try {
            new Select(getDriver().findElement(By.id("registerForm:countryName"))).selectByVisibleText(COUNTRY);
        } catch (Exception e){
            return null;
        }

        WebElement weButton = getDriver().findElement(By.id("registerForm:create"));
        weButton.click();
        waitForPageToLoad();

        if(isOnPage())
            return null;
        else
            return new HomePageObject(getDriver());
    }
}
