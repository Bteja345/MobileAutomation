package com.automation.base;

import com.automation.core.DriverManager;
import com.automation.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * BaseTest - Simple foundation for all test classes
 * Handles setup and cleanup of drivers for each test
 */
public class BaseTest {

    @BeforeMethod
    public void setUp() {
        // Get platform from config and initialize driver
        String platform = ConfigReader.getProperty("platform");
        DriverManager.initializeDriver(platform);
    }

    @AfterMethod
    public void tearDown() {
        // Clean up driver after each test
        DriverManager.quitDriver();
    }

    /**
     * Get the current driver instance
     * @return WebDriver instance
     */
    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}