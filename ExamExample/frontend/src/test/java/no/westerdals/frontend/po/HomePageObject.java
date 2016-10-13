package no.westerdals.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

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

    public LoginPageObject toLoginPage(){
        getDriver().findElement(By.id("loginButton")).click();
        waitForPageToLoad();
        return new LoginPageObject(getDriver());
    }

    public CreateEventPageObject toCreateEventPage(){
        getDriver().findElement(By.id("eventButton")).click();
        waitForPageToLoad();
        return new CreateEventPageObject(getDriver());
    }

    public void toggleOnlyMyCountry(){
        getDriver().findElement(By.id("showOnlyOwnCountryForm:showOnlyOwnCountry")).click();
        waitForPageToLoad();
    }

    public int getCountOfEvents(){
        return getDriver().findElements(By.xpath("//table[@id='eventTable']/tbody/tr")).size();
    }

    public int getNumberOfAttendees(String title){
        List<WebElement> elements = getDriver().findElements(
                By.xpath("//table[@id='eventTable']//tbody" +
                        "//tr[contains(td[2], '" + title + "')]" +
                        "/td[4]"));
        if(!elements.isEmpty())
            return Integer.parseInt(elements.get(0).getText());

        return -1;
    }

    public void toggleAttendEvent(String title){
        List<WebElement> elements = getDriver().findElements(
                By.xpath("//table[@id='eventTable']//tbody" +
                        "//tr[contains(td[2], '" + title + "')]" +
                        "/td[5]/form/input[@type='checkbox']"));

        if(elements.isEmpty())
            return;

        elements.get(0).click();
        waitForPageToLoad();
    }

    public boolean isAttendingEvent(String title){
        List<WebElement> elements = getDriver().findElements(
                By.xpath("//table[@id='eventTable']//tbody" +
                        "//tr[contains(td[2], '" + title + "')]" +
                        "/td[5]/form/input[@type='checkbox' and @checked='checked']"));
        return !elements.isEmpty();
    }
}
