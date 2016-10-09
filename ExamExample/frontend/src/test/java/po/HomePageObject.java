package po;

import org.openqa.selenium.WebDriver;

public class HomePageObject extends PageObject {
    public HomePageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Event List Home Page");
    }

    public void toStartingPage() {
        getDriver().get(getBaseUrl());
        waitForPageToLoad();
    }

    public void logout(){

    }
}
