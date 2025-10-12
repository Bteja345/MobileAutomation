package com.automation.core;

import com.automation.utils.AppiumServerManager;
import com.automation.utils.ConfigReader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * DriverManager - Manages the lifecycle of WebDriver and AppiumDriver instances.
 * It uses ThreadLocal to ensure thread-safe parallel test execution.
 */
public class DriverManager {

    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Initializes the driver based on the specified platform.
     *
     * @param platform The platform to run tests on (e.g., "android", "ios", "web").
     */
    public static void initializeDriver(String platform) {
        WebDriver driver = null;
        try {
            switch (platform.toLowerCase()) {
                case "android":
                    driver = createAndroidDriver();
                    break;
                case "ios":
                    driver = createIOSDriver();
                    break;
                case "web":
                    driver = createWebDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported platform: " + platform);
            }
            driverThreadLocal.set(driver);
            logger.info("{} driver initialized successfully.", platform);
        } catch (MalformedURLException e) {
            logger.error("Failed to initialize driver due to invalid URL.", e);
            throw new RuntimeException("Driver initialization failed.", e);
        }
    }

    /**
     * Retrieves the driver for the current thread.
     *
     * @return The WebDriver instance.
     */
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            throw new IllegalStateException("Driver has not been initialized. Call initializeDriver() first.");
        }
        return driverThreadLocal.get();
    }

    /**
     * Quits the driver and removes it from the ThreadLocal container.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            logger.info("Driver quit successfully.");
        }
    }

    private static AppiumDriver createAndroidDriver() throws MalformedURLException {
        AppiumServerManager.startServer();
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(ConfigReader.getProperty("android.platformName"))
                .setPlatformVersion(ConfigReader.getProperty("android.platformVersion"))
                .setDeviceName(ConfigReader.getProperty("android.deviceName"))
                .setAutomationName("UiAutomator2")
                .setApp(System.getProperty("user.dir") + ConfigReader.getProperty("android.appPath"))
                .setAppPackage(ConfigReader.getProperty("android.appPackage"))
                .setAppActivity(ConfigReader.getProperty("android.appActivity"))
                .setNoReset(false)
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(120));

        return new AndroidDriver(new URL(AppiumServerManager.getServerUrl()), options);
    }

    private static AppiumDriver createIOSDriver() throws MalformedURLException {
        AppiumServerManager.startServer();
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName(ConfigReader.getProperty("ios.platformName"))
                .setPlatformVersion(ConfigReader.getProperty("ios.platformVersion"))
                .setDeviceName(ConfigReader.getProperty("ios.deviceName"))
                .setAutomationName("XCUITest")
                .setApp(System.getProperty("user.dir") + ConfigReader.getProperty("ios.appPath"))
                .setBundleId(ConfigReader.getProperty("ios.bundleId"))
                .setNoReset(false)
                .setUdid(ConfigReader.getProperty("ios.udid"))
                .setWdaLaunchTimeout(Duration.ofSeconds(60))
                .setNewCommandTimeout(Duration.ofSeconds(120));

        return new IOSDriver(new URL(AppiumServerManager.getServerUrl()), options);
    }

    private static WebDriver createWebDriver() {
        String browserName = ConfigReader.getProperty("browserName");
        WebDriver driver;

        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driver = new FirefoxDriver(firefoxOptions);
                break;
            default:
                throw new IllegalArgumentException("Browser not supported: " + browserName);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return driver;
    }
}
