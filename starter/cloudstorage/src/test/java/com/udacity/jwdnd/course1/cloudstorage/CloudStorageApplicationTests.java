package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.homepage.CredentialsTab;
import com.udacity.jwdnd.course1.cloudstorage.homepage.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.homepage.NotesTab;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private String baseUrl;

    private NotesTab notesTab;

    private CredentialsTab credentialsTab;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().browserVersion("129.0.6668.59").setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    private void signUpAndLogin() {
        driver.get(baseUrl + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signUp("Hiep", "Nguyen", "user_test", "password_test");

        driver.get(baseUrl + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("user_test", "password_test");
    }

    /**
     * TestCase that verifies that an unauthorized user can only access the login and signup pages.
     **/
    @Test
    public void testUnauthorized() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get(baseUrl + "/login");
        webDriverWait.until(ExpectedConditions.titleContains("Login"));
        assertEquals("Login", driver.getTitle());

        driver.get(baseUrl + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
        assertEquals("Sign Up", driver.getTitle());

        driver.get(baseUrl + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Login"));
        assertEquals("Login", driver.getTitle());
    }

    /**
     * TestCase that signs up a new user, logs in, verifies that the home page is accessible, logs out, and verifies that the home page is no longer accessible.
     **/
    @Test
    public void testAuthorized() {
        HomePage homePage = new HomePage(driver);
        signUpAndLogin();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get(baseUrl + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));
        assertEquals("Home", driver.getTitle());

        //logout
        homePage.logOut();
        driver.get(baseUrl + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Login"));
        assertEquals("Login", driver.getTitle());
    }

    /**
     * TestCase that creates a note, and verifies it is displayed.
     **/
    @Test
    public void testCreateNote() {
        notesTab = new NotesTab(driver);

        signUpAndLogin();
        notesTab.navigateToNotesTab();
        notesTab.createNote("Test Note", "This is a test note.");

        // Redirect to home page after action
        driver.get(baseUrl + "/home");
        notesTab.navigateToNotesTab();

        Assertions.assertTrue(notesTab.isNoteDisplayed("Test Note", "This is a test note."));
    }

    /**
     * TestCase that edits an existing note and verifies that the changes are displayed.
     **/
    @Test
    public void testEditNote() {
        notesTab = new NotesTab(driver);

        signUpAndLogin();
        notesTab.navigateToNotesTab();
        notesTab.createNote("Original Note", "Original Description");

        // Redirect to home page after action
        driver.get(baseUrl + "/home");
        notesTab.navigateToNotesTab();

        notesTab.editNote("Original Note", "Edited Note", "Edited Description");

        // Redirect to home page after action
        driver.get(baseUrl + "/home");
        notesTab.navigateToNotesTab();

        Assertions.assertFalse(notesTab.isNoteDisplayed("Original Note", "Original Description"));
        Assertions.assertTrue(notesTab.isNoteDisplayed("Edited Note", "Edited Description"));
    }

    /**
     * TestCase that deletes a note and verifies that the note is no longer displayed.
     **/
    @Test
    public void testDeleteNote() {
        notesTab = new NotesTab(driver);

        signUpAndLogin();
        notesTab.navigateToNotesTab();
        notesTab.createNote("Note to Delete", "This note will be deleted.");

        // Redirect to home page after action
        driver.get(baseUrl + "/home");
        notesTab.navigateToNotesTab();

        notesTab.deleteNote("Note to Delete");

        // Redirect to home page after action
        driver.get(baseUrl + "/home");
        notesTab.navigateToNotesTab();

        Assertions.assertFalse(notesTab.isNoteDisplayed("Note to Delete", "This note will be deleted."));
    }

    /**
     * TestCase that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.
     */
    @Test
    public void testCreateCredential() {
        credentialsTab = new CredentialsTab(driver);

        signUpAndLogin();
        credentialsTab.navigateToCredentialsTab();
        credentialsTab.createCredential("http://example.com", "user1", "password1");

        WebElement lastCredentialRow = credentialsTab.getLastCredentialRow();
        String urlText = credentialsTab.getCredentialUrl(lastCredentialRow);
        String usernameText = credentialsTab.getCredentialColumnText(lastCredentialRow, 2);
        String passwordCryptText = credentialsTab.getCredentialColumnText(lastCredentialRow, 3);

        assertEquals("http://example.com", urlText);
        assertEquals("user1", usernameText);
        //check password crypt
        assertNotEquals("password1", passwordCryptText);
        //check password unencrypt
        assertTrue(credentialsTab.isPasswordUnencrypted(lastCredentialRow, "password1"));
    }

    /**
     * Testcase that views an existing set of credentials, verifies that the viewable password is unencrypted, edits the credentials, and verifies that the changes are displayed.
     */
    @Test
    public void testEditCredential() {
        credentialsTab = new CredentialsTab(driver);

        signUpAndLogin();
        credentialsTab.navigateToCredentialsTab();
        credentialsTab.createCredential("http://example.com", "user1", "password1");

        WebElement lastCredentialRow = credentialsTab.getLastCredentialRow();

        // edit
        credentialsTab.updateCredential(lastCredentialRow, "http://example.org", "user2", "password2");

        lastCredentialRow = credentialsTab.getLastCredentialRow();
        String urlText = credentialsTab.getCredentialUrl(lastCredentialRow);
        String usernameText = credentialsTab.getCredentialColumnText(lastCredentialRow, 2);
        String passwordCryptText = credentialsTab.getCredentialColumnText(lastCredentialRow, 3);

        assertEquals("http://example.org", urlText);
        assertEquals("user2", usernameText);
        //check password encrypt
        assertNotEquals("password2", passwordCryptText);
        //check password unencrypt
        assertTrue(credentialsTab.isPasswordUnencrypted(lastCredentialRow, "password2"));
    }

    /**
     * Testcase that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
     */
    @Test
    public void testDeleteCredential() {
        credentialsTab = new CredentialsTab(driver);

        signUpAndLogin();
        credentialsTab.navigateToCredentialsTab();
        credentialsTab.createCredential("http://example_delete.com", "user_test_delete", "password_test_delete");

        WebElement lastCredentialRow = credentialsTab.getLastCredentialRow();
        String urlText = credentialsTab.getCredentialUrl(lastCredentialRow);
        String usernameText = credentialsTab.getCredentialColumnText(lastCredentialRow, 2);
        String passwordCryptText = credentialsTab.getCredentialColumnText(lastCredentialRow, 3);

        // confirm before delete
        assertEquals("http://example_delete.com", urlText);
        assertEquals("user_test_delete", usernameText);
        //check password crypt
        assertNotEquals("password_test_delete", passwordCryptText);

        // delete
        credentialsTab.deleteCredential(lastCredentialRow);

        WebElement lastCredentialRowAfter = credentialsTab.getLastCredentialRow();

        if(lastCredentialRowAfter != null) {
            String urlTextAfter = credentialsTab.getCredentialUrl(lastCredentialRowAfter);
            String usernameTextAfter = credentialsTab.getCredentialColumnText(lastCredentialRowAfter, 2);
            String passwordCryptTextAfter = credentialsTab.getCredentialColumnText(lastCredentialRowAfter, 3);

            // confirm after delete
            assertNotEquals("http://example_delete.com", urlTextAfter);
            assertNotEquals("user_test_delete", usernameTextAfter);
            assertNotEquals("password_test_delete", passwordCryptTextAfter);
        }
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the log in page.
        assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }


}
