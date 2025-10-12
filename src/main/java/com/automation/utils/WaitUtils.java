package com.automation.utils;

import com.automation.base.BaseTest;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * WaitUtils - Utility class for various wait strategies
 * Supports both web and mobile platforms
 */
public class WaitUtils {

    private static final Logger logger = LogManager.getLogger(WaitUtils.class);
    private static final int DEFAULT_TIMEOUT = 30;
    private static final int DEFAULT_POLL_FREQUENCY = 2;

    /**
     * Get WebDriverWait instance with default timeout
     * @return WebDriverWait instance
     */
    public static WebDriverWait getWebDriverWait() {
        return getWebDriverWait(DEFAULT_TIMEOUT);
    }

    /**
     * Get WebDriverWait instance with custom timeout
     * @param timeoutInSeconds Timeout in seconds
     * @return WebDriverWait instance
     */
    public static WebDriverWait getWebDriverWait(int timeoutInSeconds) {
        WebDriver driver = getCurrentDriver();
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    /**
     * Get FluentWait instance with default settings
     * @return FluentWait instance
     */
    public static FluentWait<WebDriver> getFluentWait() {
        return getFluentWait(DEFAULT_TIMEOUT, DEFAULT_POLL_FREQUENCY);
    }

    /**
     * Get FluentWait instance with custom settings
     * @param timeoutInSeconds Timeout in seconds
     * @param pollFrequencyInSeconds Polling frequency in seconds
     * @return FluentWait instance
     */
    public static FluentWait<WebDriver> getFluentWait(int timeoutInSeconds, int pollFrequencyInSeconds) {
        WebDriver driver = getCurrentDriver();
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofSeconds(pollFrequencyInSeconds))
                .ignoring(NoSuchElementException.class);
    }

    /**
     * Wait for element to be visible
     * @param locator Element locator
     * @return WebElement when visible
     */
    public static WebElement waitForElementVisible(By locator) {
        return waitForElementVisible(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be visible with custom timeout
     * @param locator Element locator
     * @param timeoutInSeconds Timeout in seconds
     * @return WebElement when visible
     */
    public static WebElement waitForElementVisible(By locator, int timeoutInSeconds) {
        try {
            logger.debug("Waiting for element to be visible: " + locator);
            return getWebDriverWait(timeoutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            logger.error("Element not visible within timeout: " + locator, e);
            throw e;
        }
    }

    /**
     * Wait for element to be clickable
     * @param locator Element locator
     * @return WebElement when clickable
     */
    public static WebElement waitForElementClickable(By locator) {
        return waitForElementClickable(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be clickable with custom timeout
     * @param locator Element locator
     * @param timeoutInSeconds Timeout in seconds
     * @return WebElement when clickable
     */
    public static WebElement waitForElementClickable(By locator, int timeoutInSeconds) {
        try {
            logger.debug("Waiting for element to be clickable: " + locator);
            return getWebDriverWait(timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            logger.error("Element not clickable within timeout: " + locator, e);
            throw e;
        }
    }

    /**
     * Wait for element to be present in DOM
     * @param locator Element locator
     * @return WebElement when present
     */
    public static WebElement waitForElementPresent(By locator) {
        return waitForElementPresent(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be present in DOM with custom timeout
     * @param locator Element locator
     * @param timeoutInSeconds Timeout in seconds
     * @return WebElement when present
     */
    public static WebElement waitForElementPresent(By locator, int timeoutInSeconds) {
        try {
            logger.debug("Waiting for element to be present: " + locator);
            return getWebDriverWait(timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            logger.error("Element not present within timeout: " + locator, e);
            throw e;
        }
    }

    /**
     * Wait for element to disappear
     * @param locator Element locator
     * @return true when element is no longer visible
     */
    public static boolean waitForElementInvisible(By locator) {
        return waitForElementInvisible(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to disappear with custom timeout
     * @param locator Element locator
     * @param timeoutInSeconds Timeout in seconds
     * @return true when element is no longer visible
     */
    public static boolean waitForElementInvisible(By locator, int timeoutInSeconds) {
        try {
            logger.debug("Waiting for element to be invisible: " + locator);
            return getWebDriverWait(timeoutInSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            logger.error("Element still visible after timeout: " + locator, e);
            return false;
        }
    }

    /**
     * Wait for text to be present in element
     * @param locator Element locator
     * @param text Text to wait for
     * @return true when text is present
     */
    public static boolean waitForTextInElement(By locator, String text) {
        return waitForTextInElement(locator, text, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for text to be present in element with custom timeout
     * @param locator Element locator
     * @param text Text to wait for
     * @param timeoutInSeconds Timeout in seconds
     * @return true when text is present
     */
    public static boolean waitForTextInElement(By locator, String text, int timeoutInSeconds) {
        try {
            logger.debug("Waiting for text '" + text + "' in element: " + locator);
            return getWebDriverWait(timeoutInSeconds).until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (Exception e) {
            logger.error("Text not found in element within timeout: " + text, e);
            return false;
        }
    }

    /**
     * Wait for all elements to be visible
     * @param locator Element locator
     * @return List of WebElements when all are visible
     */
    public static List<WebElement> waitForAllElementsVisible(By locator) {
        return waitForAllElementsVisible(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for all elements to be visible with custom timeout
     * @param locator Element locator
     * @param timeoutInSeconds Timeout in seconds
     * @return List of WebElements when all are visible
     */
    public static List<WebElement> waitForAllElementsVisible(By locator, int timeoutInSeconds) {
        try {
            logger.debug("Waiting for all elements to be visible: " + locator);
            return getWebDriverWait(timeoutInSeconds).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (Exception e) {
            logger.error("Not all elements visible within timeout: " + locator, e);
            throw e;
        }
    }

    /**
     * Wait for custom condition using FluentWait
     * @param condition Custom condition function
     * @param <T> Return type of the condition
     * @return Result of the condition
     */
    public static <T> T waitForCustomCondition(Function<WebDriver, T> condition) {
        return waitForCustomCondition(condition, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for custom condition using FluentWait with custom timeout
     * @param condition Custom condition function
     * @param timeoutInSeconds Timeout in seconds
     * @param <T> Return type of the condition
     * @return Result of the condition
     */
    public static <T> T waitForCustomCondition(Function<WebDriver, T> condition, int timeoutInSeconds) {
        try {
            logger.debug("Waiting for custom condition");
            return getFluentWait(timeoutInSeconds, 1).until(condition);
        } catch (Exception e) {
            logger.error("Custom condition not met within timeout", e);
            throw e;
        }
    }

    /**
     * Simple sleep utility
     * @param milliseconds Time to sleep in milliseconds
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted: " + e.getMessage());
        }
    }

    /**
     * Get current active driver (mobile or web)
     * @return WebDriver instance
     */
    private static WebDriver getCurrentDriver() {
        AppiumDriver mobileDriver = BaseTest.getMobileDriver();
        if (mobileDriver != null) {
            return mobileDriver;
        }

        WebDriver webDriver = BaseTest.getWebDriver();
        if (webDriver != null) {
            return webDriver;
        }

        throw new RuntimeException("No active driver found");
    }

    /**
     * Check if element is present without waiting
     * @param locator Element locator
     * @return true if element is present, false otherwise
     */
    public static boolean isElementPresent(By locator) {
        try {
            getCurrentDriver().findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Check if element is displayed without waiting
     * @param locator Element locator
     * @return true if element is displayed, false otherwise
     */
    public static boolean isElementDisplayed(By locator) {
        try {
            return getCurrentDriver().findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}