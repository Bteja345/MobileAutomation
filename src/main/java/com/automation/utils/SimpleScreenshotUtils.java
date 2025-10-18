package com.automation.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple Screenshot Utility
 * Takes screenshots without complex logging
 */
public class SimpleScreenshotUtils {

    private static final String SCREENSHOT_DIR = "screenshots";

    /**
     * Take a simple screenshot
     * @param driver WebDriver instance
     * @param testName Name for the screenshot file
     * @return Path to screenshot file
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        try {
            // Create directory if not exists
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Generate filename with timestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = dateFormat.format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + File.separator + fileName;

            // Take screenshot
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshotFile, new File(filePath));

            System.out.println("Screenshot saved: " + filePath);
            return filePath;

        } catch (Exception e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Take screenshot on test failure
     * @param driver WebDriver instance
     * @param testName Name of the failed test
     * @return Path to screenshot file
     */
    public static String takeFailureScreenshot(WebDriver driver, String testName) {
        return takeScreenshot(driver, testName + "_FAILED");
    }
}