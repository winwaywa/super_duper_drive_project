package com.udacity.jwdnd.course1.cloudstorage.homepage;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CredentialsTab {
    // WebDriver instance
    private final WebDriver driver;

    @FindBy(css = "#nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(css = "#credentialTable")
    private WebElement credentialTable;

    @FindBy(css = "#credentialModal")
    private WebElement credentialModal;

    @FindBy(css = "#credential-url")
    private WebElement credentialUrlInput;

    @FindBy(css = "#credential-username")
    private WebElement credentialUsernameInput;

    @FindBy(css = "#credential-password")
    private WebElement credentialPasswordInput;

    @FindBy(css = "#credentialSubmit")
    private WebElement credentialSubmitButton;

    @FindBy(css = "#credentialTable tbody tr")
    private List<WebElement> credentialRows;

    @FindBy(css = "#nav-credentials .btn.btn-info")
    private WebElement addCredentialButton;

    public CredentialsTab(WebDriver webDriver) {
        this.driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void navigateToCredentialsTab() {
        waitForVisibility(credentialsTab);
        credentialsTab.click();
    }

    public void openCredentialModal() {
        waitForVisibility(addCredentialButton);
        addCredentialButton.click();
    }

    public void createCredential(String url, String username, String password) {
        openCredentialModal();
        waitForVisibility(credentialModal);
        credentialUrlInput.clear();
        credentialUrlInput.sendKeys(url);
        credentialUsernameInput.clear();
        credentialUsernameInput.sendKeys(username);
        credentialPasswordInput.clear();
        credentialPasswordInput.sendKeys(password);
        credentialModal.findElement(By.cssSelector(".modal-footer > button.btn.btn-primary")).click();
    }

    public void updateCredential(WebElement credentialRow, String url, String username, String password) {
        clickEditButton(credentialRow);
        waitForVisibility(credentialModal);
        credentialUrlInput.clear();
        credentialUrlInput.sendKeys(url);
        credentialUsernameInput.clear();
        credentialUsernameInput.sendKeys(username);
        credentialPasswordInput.clear();
        credentialPasswordInput.sendKeys(password);
        credentialModal.findElement(By.cssSelector(".modal-footer > button.btn.btn-primary")).click();
    }

    public WebElement getLastCredentialRow() {
        if (!credentialRows.isEmpty()) {
            return credentialRows.get(credentialRows.size() - 1);
        }
        return null;
    }

    public void clickEditButton(WebElement credentialRow) {
        WebElement editButton = credentialRow.findElement(By.cssSelector("button.btn-success"));
        editButton.click();
    }

    public void deleteCredential(WebElement credentialRow) {
        WebElement deleteButton = credentialRow.findElement(By.cssSelector("button.btn-danger"));
        deleteButton.click();
    }

    public String getCredentialColumnText(WebElement credentialRow, int columnIndex) {
        return credentialRow.findElement(By.xpath("td[" + columnIndex + "]")).getText();
    }

    public String getCredentialUrl(WebElement credentialRow) {
        return credentialRow.findElement(By.xpath("th")).getText();
    }

    public boolean isPasswordUnencrypted(WebElement credentialRow, String password) {
        clickEditButton(credentialRow);
        return credentialPasswordInput.getAttribute("value").equals(password);
    }

    // Wait for an element to be visible
    private void waitForVisibility(WebElement element) {
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(element));
    }
}