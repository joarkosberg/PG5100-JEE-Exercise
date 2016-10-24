package no.joarkosberg.exam.frontend.po;

import org.openqa.selenium.WebDriver;

public class StatisticPageObject extends PageObject {

    public StatisticPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Statistic");
    }
}
