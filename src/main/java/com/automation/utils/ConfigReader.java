package com.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - A utility class to read configuration properties from a file.
 */
public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    static {
        try {
            String configPath = "src/main/resources/config.properties";
            FileInputStream ip = new FileInputStream(configPath);
            properties.load(ip);
            logger.info("Configuration properties loaded successfully from {}", configPath);
        } catch (IOException e) {
            logger.error("Failed to load configuration properties.", e);
            throw new RuntimeException("Could not read config.properties file", e);
        }
    }

    /**
     * Gets a property value by its key.
     *
     * @param key The key of the property.
     * @return The value of the property.
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property with key '{}' not found in config file.", key);
        }
        return value;
    }

    /**
     * Gets a property value by its key, returning a default value if not found.
     *
     * @param key          The key of the property.
     * @param defaultValue The default value to return if the key is not found.
     * @return The value of the property or the default value.
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
