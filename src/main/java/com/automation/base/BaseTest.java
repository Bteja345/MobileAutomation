package com.automation.base;

import com.automation.core.DriverManager;
import com.automation.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

/**
 * BaseTest - The foundation for all test classes.
 * It handles the setup and teardown of drivers for each test method,
 * using the DriverManager to ensure thread safety for parallel execution.
 */
@Listeners(com.automation.listeners.TestListener.class) // Optional: Add a test listener for reporting
public class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeMethod
    public void setUp() {
        logger.info("Initializing driver from BaseTest...");
        // Initialize the driver using the platform specified in the config file
        String platform = ConfigReader.getProperty("platform");
        DriverManager.initializeDriver(platform);
        logger.info("Driver initialization complete for platform: {}", platform);
    }

    @AfterMethod
    public void tearDown() {
        logger.info("Tearing down driver...");
        DriverManager.quitDriver();
        logger.info("Driver teardown complete.");
    }

    /**
     * Provides easy access to the driver instance for test classes.
     *
     * @return The WebDriver instance for the current thread.
     */
    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}

