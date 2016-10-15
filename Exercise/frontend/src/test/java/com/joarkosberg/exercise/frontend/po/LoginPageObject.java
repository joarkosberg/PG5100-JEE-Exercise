package com.joarkosberg.exercise.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPageObject extends PageObject {
    public LoginPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Login/Create");
    }

    public void toStartingPage(){
        getDriver().get(getBaseUrl());
        waitForPageToLoad();
    }

    public LandingPageObject createNewUser(String username, String password){
        setText("userForm:username", username);
        setText("userForm:password", password);

        WebElement button = getDriver().findElement(By.id("userForm:create"));
        button.click();
        waitForPageToLoad();

        if(isOnPage())
            return null;
        else
            return new LandingPageObject(getDriver());
    }

    public LandingPageObject login(String username, String password) {
        setText("userForm:username", username);
        setText("userForm:password", password);

        WebElement button = getDriver().findElement(By.id("userForm:login"));
        button.click();
        waitForPageToLoad();

        if(isOnPage())
            return null;
        else
            return new LandingPageObject(getDriver());
    }
}
