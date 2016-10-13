package no.westerdals.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class CreateEventPageObject extends PageObject {
    public CreateEventPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Create New Event");
    }

    public HomePageObject createNewEvent (String title, String country){
        setText("newEventForm:title", title);
        setText("newEventForm:location", "Location");
        setText("newEventForm:description", "Description");

        //Select Country
        try {
            new Select(getDriver().findElement(By.id("newEventForm:countryName"))).selectByVisibleText(country);
        } catch (Exception e) {
            return null;
        }

        WebElement weButton = getDriver().findElement(By.id("newEventForm:create"));
        weButton.click();
        waitForPageToLoad();

        if(isOnPage())
            return null;
        else
            return new HomePageObject(getDriver());
    }
}