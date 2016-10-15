package com.joarkosberg.exercise.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public abstract class PageObject {
    private final WebDriver driver;
    public abstract boolean isOnPage();

    public PageObject(WebDriver driver) {
        this.driver = driver;
    }

    protected WebDriver getDriver(){
        return driver;
    }

    protected String getBaseUrl(){
        return "http://localhost:8080/redditclone/";
    }

    protected Boolean waitForPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        return wait.until((ExpectedCondition<Boolean>) input -> (
                (JavascriptExecutor) input).executeScript("return document.readyState").equals("complete"));
    }

    public void setText(String id, String text){
        WebElement element = driver.findElement(By.id(id));
        element.clear();
        element.sendKeys(text);
    }
}
