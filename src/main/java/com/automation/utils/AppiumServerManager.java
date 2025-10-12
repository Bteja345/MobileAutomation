package com.automation.utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * AppiumServerManager - Handles programmatic starting and stopping of the Appium server.
 */
public class AppiumServerManager {

    private static final Logger logger = LogManager.getLogger(AppiumServerManager.class);
    private static final ThreadLocal<AppiumDriverLocalService> serviceThreadLocal = new ThreadLocal<>();

    /**
     * Starts the Appium server if it is not already running for the current thread.
     */
    public static void startServer() {
        if (serviceThreadLocal.get() != null && serviceThreadLocal.get().isRunning()) {
            logger.info("Appium server is already running.");
            return;
        }

        String ipAddress = com.automation.utils.ConfigReader.getProperty("appium.server.ip", "127.0.0.1");
        int port = Integer.parseInt(com.automation.utils.ConfigReader.getProperty("appium.server.port", "4723"));

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress(ipAddress)
                .usingPort(port)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                .withLogFile(new File("target/appium.log"));

        AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);
        service.start();
        serviceThreadLocal.set(service);
        logger.info("Appium server started successfully at: {}", service.getUrl());
    }

    /**
     * Stops the Appium server if it is running for the current thread.
     */
    public static void stopServer() {
        if (serviceThreadLocal.get() != null) {
            serviceThreadLocal.get().stop();
            serviceThreadLocal.remove();
            logger.info("Appium server stopped successfully.");
        }
    }

    /**
     * Gets the URL of the running Appium server.
     *
     * @return The server URL as a string.
     */
    public static String getServerUrl() {
        if (serviceThreadLocal.get() == null) {
            throw new IllegalStateException("Appium server is not running. Call startServer() first.");
        }
        return serviceThreadLocal.get().getUrl().toString();
    }
}
