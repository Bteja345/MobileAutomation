package com.automation.utils;

import com.automation.base.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * TestListener - TestNG listener for handling test events
 * Provides logging and cleanup functionality
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ISuite suite) {
        logger.info("Starting test suite: " + suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        logger.info("Finished test suite: " + suite.getName());

        // Stop Appium server when suite finishes
        try {
            BaseTest.stopAppiumServer();
        } catch (Exception e) {
            logger.warn("Error stopping Appium server: " + e.getMessage());
        }

        // Cleanup old screenshots and recordings
        try {
            ScreenshotUtils.cleanupOldScreenshots(7); // Keep for 7 days
            ScreenRecordingUtils.cleanupOldRecordings(7); // Keep for 7 days
        } catch (Exception e) {
            logger.warn("Error during cleanup: " + e.getMessage());
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        logger.info("Starting test: " + className + "." + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        logger.info("Test PASSED: " + className + "." + testName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        Throwable throwable = result.getThrowable();

        logger.error("Test FAILED: " + className + "." + testName);
        if (throwable != null) {
            logger.error("Failure reason: " + throwable.getMessage(), throwable);
        }

        // Take screenshot on failure
        try {
            String screenshotPath = ScreenshotUtils.takeScreenshot(testName + "_FAILED");
            if (screenshotPath != null) {
                logger.info("Screenshot saved: " + screenshotPath);
            }
        } catch (Exception e) {
            logger.warn("Failed to take screenshot on test failure: " + e.getMessage());
        }

        // Stop recording if in progress
        try {
            if (ScreenRecordingUtils.isRecordingInProgress()) {
                String recordingPath = ScreenRecordingUtils.stopRecording(testName + "_FAILED");
                if (recordingPath != null) {
                    logger.info("Recording saved: " + recordingPath);
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to stop recording on test failure: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        logger.warn("Test SKIPPED: " + className + "." + testName);

        if (result.getThrowable() != null) {
            logger.warn("Skip reason: " + result.getThrowable().getMessage());
        }
    }
}