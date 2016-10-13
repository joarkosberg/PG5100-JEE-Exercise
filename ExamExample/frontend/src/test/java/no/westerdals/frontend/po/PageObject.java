package no.westerdals.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
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
        return "http://localhost:8080/exam_example/";
    }

    protected Boolean waitForPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        return wait.until((ExpectedCondition<Boolean>) input -> (
                (JavascriptExecutor) input).executeScript("return document.readyState").equals("complete"));
    }

    public boolean isLoggedIn(){
        List<WebElement> logout = driver.findElements(By.id("logoutForm"));
        return !logout.isEmpty();
    }

    public boolean isLoggedIn(String username){
        if(!isLoggedIn()){
            return false;
        }
        return driver.findElement(By.id("logoutForm:welcomeMessage")).getText().contains(username);
    }

    public void logout(){
        List<WebElement> logout = driver.findElements(By.id("logoutForm:logoutButton"));
        if(! logout.isEmpty()){
            logout.get(0).click();
            waitForPageToLoad();
        }
    }

    public void setText(String id, String text){
        WebElement element = driver.findElement(By.id(id));
        element.clear();
        element.sendKeys(text);
    }
}
