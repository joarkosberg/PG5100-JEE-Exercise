package com.joarkosberg.exercise.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LandingPageObject extends PageObject {
    public LandingPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Landing page");
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
}
