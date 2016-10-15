package com.joarkosberg.exercise.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

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

    public boolean isLoggedIn(){
        List<WebElement> logout = getDriver().findElements(By.id("logoutForm"));
        return !logout.isEmpty();
    }

    public boolean isLoggedIn(String username){
        if(!isLoggedIn()){
            return false;
        }
        return getDriver().findElement(By.id("logoutForm")).getText().contains(username);
    }

    public void logout(){
        List<WebElement> logout = getDriver().findElements(By.id("logoutForm:logoutButton"));
        if(! logout.isEmpty()){
            logout.get(0).click();
            waitForPageToLoad();
        }
    }
}
