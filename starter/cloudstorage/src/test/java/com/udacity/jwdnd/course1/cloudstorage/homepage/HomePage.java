package com.udacity.jwdnd.course1.cloudstorage.homepage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    // WebDriver instance
    private final WebDriver driver;

    @FindBy(css = "#logout-button")
    private WebElement logoutButton;

    public HomePage(WebDriver webDriver) {
        this.driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void logOut() {
        waitForVisibility(logoutButton);
        logoutButton.click();
    }

    // Wait for an element to be visible
    private void waitForVisibility(WebElement element) {
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(element));
    }
}
