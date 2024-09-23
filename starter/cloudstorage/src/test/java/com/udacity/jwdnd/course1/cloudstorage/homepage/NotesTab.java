package com.udacity.jwdnd.course1.cloudstorage.homepage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class NotesTab {
    // WebDriver instance
    private final WebDriver driver;

    // Notes tab
    @FindBy(css = "#nav-notes-tab")
    private WebElement navNotesTab;

    @FindBy(css = "#nav-notes .btn.btn-info")
    private WebElement addNewNoteButton;

    @FindBy(id = "noteModal")
    private WebElement noteModal;

    @FindBy(id = "note-title")
    private WebElement noteTitleField;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionField;

    @FindBy(id = "noteSubmit")
    private WebElement noteSubmitButton;

    @FindBy(id = "userTable")
    private WebElement notesTable;


    public NotesTab(WebDriver webDriver) {
        this.driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    // Navigate to Notes tab
    public void navigateToNotesTab() {
        waitForVisibility(navNotesTab);
        navNotesTab.click();
    }

    // Click Add New Note button
    public void clickAddNewNote() {
        waitForVisibility(addNewNoteButton);
        addNewNoteButton.click();
    }

    // Create a new note
    public void createNote(String title, String description) {
        clickAddNewNote();
        waitForVisibility(noteModal);
        noteTitleField.clear();
        noteTitleField.sendKeys(title);
        noteDescriptionField.clear();
        noteDescriptionField.sendKeys(description);
        noteModal.findElement(By.xpath("//button[text()='Save changes']")).click();
    }

    // Edit an existing note
    public void editNote(String existingTitle, String newTitle, String newDescription) {
        WebElement editButton = getNoteEditButton(existingTitle);
        if (editButton != null) {
            editButton.click();
            waitForVisibility(noteModal);
            noteTitleField.clear();
            noteTitleField.sendKeys(newTitle);
            noteDescriptionField.clear();
            noteDescriptionField.sendKeys(newDescription);
            noteModal.findElement(By.xpath("//button[text()='Save changes']")).click();
        }
    }

    // Delete a note
    public void deleteNote(String title) {
        WebElement deleteButton = getNoteDeleteButton(title);
        if (deleteButton != null) {
            deleteButton.click();
        }
    }

    // Check if a note is displayed
    public boolean isNoteDisplayed(String title, String description) {
        waitForVisibility(notesTable);
        List<WebElement> rows = notesTable.findElements(By.cssSelector("tbody tr"));
        for (WebElement row : rows) {
            String rowTitle = row.findElement(By.tagName("th")).getText();
            String rowDescription = row.findElement(By.xpath("td[2]")).getText();
            if (rowTitle.equals(title) && rowDescription.equals(description)) {
                return true;
            }
        }
        return false;
    }

    // Helper method to find the Edit button for a note
    private WebElement getNoteEditButton(String title) {
        waitForVisibility(notesTable);
        List<WebElement> rows = notesTable.findElements(By.cssSelector("tbody tr"));
        for (WebElement row : rows) {
            String rowTitle = row.findElement(By.tagName("th")).getText();
            if (rowTitle.equals(title)) {
                return row.findElement(By.cssSelector(".btn.btn-success"));
            }
        }
        return null;
    }

    // Helper method to find the Delete button for a note
    private WebElement getNoteDeleteButton(String title) {
        waitForVisibility(notesTable);
        List<WebElement> rows = notesTable.findElements(By.cssSelector("tbody tr"));
        for (WebElement row : rows) {
            String rowTitle = row.findElement(By.tagName("th")).getText();
            if (rowTitle.equals(title)) {
                return row.findElement(By.cssSelector(".btn.btn-danger"));
            }
        }
        return null;
    }

    // Wait for an element to be visible
    private void waitForVisibility(WebElement element) {
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(element));
    }
}
