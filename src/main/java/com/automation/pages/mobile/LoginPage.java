package com.automation.pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.html.HTMLInputElement;

import java.time.Duration;

public class LoginPage {

    private final WebDriverWait wait;
    // Driver instance
    AndroidDriver driver;

    // Constructor to initialize the driver and elements
    public LoginPage(WebDriverWait wait, AndroidDriver driver) {
        this.wait = wait;
        this.driver = driver;
        // Initialize elements using Appium's PageFactory
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // Define UI Elements using @AndroidFindBy
    // Note: Replace "accessibility-id" with the actual locator strategy and value for your app.
    // Common locators: id, accessibilityId, xpath, className

    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"com.kaverisoft.servicemanager:id/businessCode\"]")
    private WebElement code;

    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id=\"com.kaverisoft.servicemanager:id/activateButton\"]")
    private WebElement activatebutton;

    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id=\"com.kaverisoft.servicemanager:id/positiveButton\"]")
    private WebElement okbutton;

    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"com.kaverisoft.servicemanager:id/login\"]")
    private WebElement login;

    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"com.kaverisoft.servicemanager:id/password\"]")
    private WebElement password;

    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id=\"com.kaverisoft.servicemanager:id/loginButton\"]")
    private WebElement loginbutton;

    // --- Actions ---

    public LoginPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        // IMPORTANT: wire up Appium annotations
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }
    /**
     * Enters the username into the username field.
     * @param activationcode The username to enter.
     */
    public void entercode (String activationcode) {
        code.sendKeys(activationcode);
    }
    public void clickactivateButton() {
        okbutton.click();
    }

    public void clickokbutton() {
        activatebutton.click();
    }

    public void enterlogin(String logind) {
        login.sendKeys(logind);
    }

    public void enterPassword(String pass) {
        password.sendKeys(pass);
    }

    public void clickLoginButton() {
        loginbutton.click();
    }


}