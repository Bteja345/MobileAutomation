package com.automation.utils;

import com.automation.base.BaseTest;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtils - Utility class for taking screenshots
 * Supports both web and mobile platforms
 */
public class ScreenshotUtils {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "screenshots";

    static {
        // Create screenshots directory if it doesn't exist
        File screenshotDir = new File(SCREENSHOT_DIR);
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
    }

    /**
     * Take screenshot of current page/screen
     * @param testName Name of the test for screenshot naming
     * @return Path to the screenshot file
     */
    public static String takeScreenshot(String testName) {
        String fileName = generateFileName(testName);
        String filePath = SCREENSHOT_DIR + File.separator + fileName;

        try {
            File screenshotFile = null;

            // Try mobile driver first
            AppiumDriver mobileDriver = BaseTest.getMobileDriver();
            if (mobileDriver != null) {
                screenshotFile = ((TakesScreenshot) mobileDriver).getScreenshotAs(OutputType.FILE);
            } else {
                // Use web driver
                WebDriver webDriver = BaseTest.getWebDriver();
                if (webDriver != null) {
                    screenshotFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
                }
            }

            if (screenshotFile != null) {
                FileUtils.copyFile(screenshotFile, new File(filePath));
                logger.info("Screenshot saved: " + filePath);
                return filePath;
            } else {
                logger.error("No active driver found for screenshot");
                return null;
            }

        } catch (IOException e) {
            logger.error("Failed to take screenshot: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Take screenshot of specific element (web only)
     * @param element WebElement to capture
     * @param testName Name of the test for screenshot naming
     * @return Path to the screenshot file
     */
    public static String takeElementScreenshot(WebElement element, String testName) {
        String fileName = generateFileName(testName + "_element");
        String filePath = SCREENSHOT_DIR + File.separator + fileName;

        try {
            WebDriver webDriver = BaseTest.getWebDriver();
            if (webDriver == null) {
                logger.error("Web driver not available for element screenshot");
                return null;
            }

            // Take full page screenshot first
            File fullScreenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            BufferedImage fullImage = ImageIO.read(fullScreenshot);

            // Get element location and size
            org.openqa.selenium.Point location = element.getLocation();
            org.openqa.selenium.Dimension size = element.getSize();

            // Crop the image to element bounds
            BufferedImage elementImage = fullImage.getSubimage(
                location.getX(), 
                location.getY(), 
                size.getWidth(), 
                size.getHeight()
            );

            // Save cropped image
            ImageIO.write(elementImage, "png", new File(filePath));
            logger.info("Element screenshot saved: " + filePath);
            return filePath;

        } catch (Exception e) {
            logger.error("Failed to take element screenshot: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Take screenshot and return as base64 string
     * @return Base64 encoded screenshot
     */
    public static String takeScreenshotAsBase64() {
        try {
            // Try mobile driver first
            AppiumDriver mobileDriver = BaseTest.getMobileDriver();
            if (mobileDriver != null) {
                return ((TakesScreenshot) mobileDriver).getScreenshotAs(OutputType.BASE64);
            } else {
                // Use web driver
                WebDriver webDriver = BaseTest.getWebDriver();
                if (webDriver != null) {
                    return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BASE64);
                }
            }

            logger.error("No active driver found for screenshot");
            return null;

        } catch (Exception e) {
            logger.error("Failed to take screenshot as base64: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Generate unique filename for screenshot
     * @param testName Name of the test
     * @return Generated filename
     */
    private static String generateFileName(String testName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        return testName + "_" + timestamp + ".png";
    }

    /**
     * Clean up old screenshots (older than specified days)
     * @param daysToKeep Number of days to keep screenshots
     */
    public static void cleanupOldScreenshots(int daysToKeep) {
        File screenshotDir = new File(SCREENSHOT_DIR);
        if (!screenshotDir.exists()) {
            return;
        }

        long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24L * 60L * 60L * 1000L);

        File[] files = screenshotDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.lastModified() < cutoffTime) {
                    if (file.delete()) {
                        logger.info("Deleted old screenshot: " + file.getName());
                    }
                }
            }
        }
    }
}