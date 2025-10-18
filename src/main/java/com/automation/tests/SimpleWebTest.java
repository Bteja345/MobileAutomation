package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.utils.SimpleScreenshotUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Simple test example
 * Clean and easy to understand for beginners
 */
public class SimpleWebTest extends BaseTest {

    @Test
    public void testGoogleSearch() {
        try {
            // Navigate to Google
            System.out.println("Opening Google homepage...");
            getDriver().get("https://www.google.com");
            
            // Find search box and enter text
            System.out.println("Searching for 'Selenium automation'...");
            WebElement searchBox = getDriver().findElement(By.name("q"));
            searchBox.sendKeys("Selenium automation");
            searchBox.submit();
            
            // Wait a moment for results to load
            Thread.sleep(2000);
            
            // Verify results page title contains search term
            String pageTitle = getDriver().getTitle();
            Assert.assertTrue(pageTitle.contains("Selenium automation"), 
                             "Search results page title should contain search term");
            
            System.out.println("Test completed successfully! Page title: " + pageTitle);
            
        } catch (Exception e) {
            // Take screenshot on failure
            SimpleScreenshotUtils.takeFailureScreenshot(getDriver(), "testGoogleSearch");
            System.out.println("Test failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGoogleHomepage() {
        try {
            // Navigate to Google
            System.out.println("Testing Google homepage elements...");
            getDriver().get("https://www.google.com");
            
            // Verify Google logo is present
            WebElement logo = getDriver().findElement(By.xpath("//img[@alt='Google']"));
            Assert.assertTrue(logo.isDisplayed(), "Google logo should be visible");
            
            // Verify search box is present
            WebElement searchBox = getDriver().findElement(By.name("q"));
            Assert.assertTrue(searchBox.isDisplayed(), "Search box should be visible");
            
            System.out.println("Homepage elements test passed!");
            
        } catch (Exception e) {
            // Take screenshot on failure
            SimpleScreenshotUtils.takeFailureScreenshot(getDriver(), "testGoogleHomepage");
            System.out.println("Test failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}