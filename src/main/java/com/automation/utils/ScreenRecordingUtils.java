package com.automation.utils;

import com.automation.base.BaseTest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenRecordingUtils - Utility class for screen recording
 * Supports mobile platforms (Android/iOS)
 */
public class ScreenRecordingUtils {

    private static final Logger logger = LogManager.getLogger(ScreenRecordingUtils.class);
    private static final String RECORDING_DIR = "recordings";
    private static boolean isRecording = false;

    static {
        // Create recordings directory if it doesn't exist
        File recordingDir = new File(RECORDING_DIR);
        if (!recordingDir.exists()) {
            recordingDir.mkdirs();
        }
    }

    /**
     * Start screen recording
     * @param testName Name of the test for recording naming
     */
    public static void startRecording(String testName) {
        try {
            AppiumDriver mobileDriver = BaseTest.getMobileDriver();
            if (mobileDriver == null) {
                logger.warn("Mobile driver not available. Screen recording is only supported for mobile platforms.");
                return;
            }

            if (!(mobileDriver instanceof CanRecordScreen)) {
                logger.warn("Current driver does not support screen recording");
                return;
            }

            if (isRecording) {
                logger.warn("Screen recording is already in progress");
                return;
            }

            ((CanRecordScreen) mobileDriver).startRecordingScreen();
            isRecording = true;
            logger.info("Screen recording started for test: " + testName);

        } catch (Exception e) {
            logger.error("Failed to start screen recording: " + e.getMessage(), e);
        }
    }

    /**
     * Stop screen recording and save to file
     * @param testName Name of the test for recording naming
     * @return Path to the recording file
     */
    public static String stopRecording(String testName) {
        try {
            AppiumDriver mobileDriver = BaseTest.getMobileDriver();
            if (mobileDriver == null) {
                logger.warn("Mobile driver not available");
                return null;
            }

            if (!(mobileDriver instanceof CanRecordScreen)) {
                logger.warn("Current driver does not support screen recording");
                return null;
            }

            if (!isRecording) {
                logger.warn("No screen recording in progress");
                return null;
            }

            String base64Video = ((CanRecordScreen) mobileDriver).stopRecordingScreen();
            isRecording = false;

            // Save the recording to file
            String fileName = generateFileName(testName);
            String filePath = RECORDING_DIR + File.separator + fileName;

            byte[] videoBytes = Base64.decodeBase64(base64Video);
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(videoBytes);
            }

            logger.info("Screen recording saved: " + filePath);
            return filePath;

        } catch (Exception e) {
            logger.error("Failed to stop screen recording: " + e.getMessage(), e);
            isRecording = false;
            return null;
        }
    }

    /**
     * Stop recording if in progress (cleanup method)
     */
    public static void stopRecordingIfInProgress() {
        if (isRecording) {
            try {
                AppiumDriver mobileDriver = BaseTest.getMobileDriver();
                if (mobileDriver != null && mobileDriver instanceof CanRecordScreen) {
                    ((CanRecordScreen) mobileDriver).stopRecordingScreen();
                }
            } catch (Exception e) {
                logger.error("Error stopping recording during cleanup: " + e.getMessage(), e);
            } finally {
                isRecording = false;
            }
        }
    }

    /**
     * Check if recording is currently in progress
     * @return true if recording, false otherwise
     */
    public static boolean isRecordingInProgress() {
        return isRecording;
    }

    /**
     * Start recording with custom options for Android
     * @param testName Name of the test
     * @param timeLimit Time limit for recording in seconds
     * @param bitRate Bit rate for video quality
     */
    public static void startRecordingWithOptions(String testName, int timeLimit, int bitRate) {
        try {
            AppiumDriver mobileDriver = BaseTest.getMobileDriver();
            if (mobileDriver == null) {
                logger.warn("Mobile driver not available");
                return;
            }

            if (!(mobileDriver instanceof CanRecordScreen)) {
                logger.warn("Current driver does not support screen recording");
                return;
            }

            if (isRecording) {
                logger.warn("Screen recording is already in progress");
                return;
            }

            // Platform-specific recording options can be added here
            // For Android: AndroidStartScreenRecordingOptions
            // For iOS: IOSStartScreenRecordingOptions

            ((CanRecordScreen) mobileDriver).startRecordingScreen();
            isRecording = true;
            logger.info("Screen recording started with custom options for test: " + testName);

        } catch (Exception e) {
            logger.error("Failed to start screen recording with options: " + e.getMessage(), e);
        }
    }

    /**
     * Generate unique filename for recording
     * @param testName Name of the test
     * @return Generated filename
     */
    private static String generateFileName(String testName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        return testName + "_" + timestamp + ".mp4";
    }

    /**
     * Clean up old recordings (older than specified days)
     * @param daysToKeep Number of days to keep recordings
     */
    public static void cleanupOldRecordings(int daysToKeep) {
        File recordingDir = new File(RECORDING_DIR);
        if (!recordingDir.exists()) {
            return;
        }

        long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24L * 60L * 60L * 1000L);

        File[] files = recordingDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".mp4") && file.lastModified() < cutoffTime) {
                    if (file.delete()) {
                        logger.info("Deleted old recording: " + file.getName());
                    }
                }
            }
        }
    }
}