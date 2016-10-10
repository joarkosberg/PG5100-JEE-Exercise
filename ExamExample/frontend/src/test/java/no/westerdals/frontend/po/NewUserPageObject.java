package no.westerdals.frontend.po;

import org.openqa.selenium.WebDriver;

public class NewUserPageObject extends PageObject {
    public NewUserPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Create User");
    }
}
