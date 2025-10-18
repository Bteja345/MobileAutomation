package com.automation.core;

import com.automation.utils.AppiumServerManager;
import com.automation.utils.ConfigReader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.net.URL;
import java.time.Duration;

/**
 * DriverManager - Manages WebDriver and AppiumDriver instances
 * Thread-safe for parallel execution
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Initialize driver based on platform
     * @param platform Platform to run tests (android, ios, web)
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
                    throw new RuntimeException("Unsupported platform: " + platform);
            }
            
            driverThreadLocal.set(driver);
            System.out.println("Driver initialized for platform: " + platform);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize driver: " + e.getMessage());
        }
    }

    /**
     * Get current thread's driver
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new RuntimeException("Driver not initialized. Call initializeDriver() first.");
        }
        return driver;
    }

    /**
     * Quit driver and clean up
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            System.out.println("Driver quit successfully");
        }
    }

    // Create Android driver
    private static AppiumDriver createAndroidDriver() throws Exception {
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

    // Create iOS driver
    private static AppiumDriver createIOSDriver() throws Exception {
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

    // Create Web driver
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
                throw new RuntimeException("Browser not supported: " + browserName);
        }

        // Set timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return driver;
    }
}