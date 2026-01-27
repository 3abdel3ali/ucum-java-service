package com.abdelaliboussadi.terminology;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertiesUtil {

    private static final Logger logger = LogManager.getLogger(PropertiesUtil.class);
    private static Properties properties = null;

    /**
     * Get a property value by key from the configuration.properties file
     * @param propertyName the property key to retrieve
     * @return the property value, or null if not found or error occurs
     */
    public static String getProperties(final String propertyName) {
        if (properties == null) {
            loadProperties();
        }

        if (properties != null) {
            String value = properties.getProperty(propertyName);
            if (value != null) {
                logger.debug("Loaded property '{}' with value: {}", propertyName, value);
            } else {
                logger.warn("Property '{}' not found in configuration file", propertyName);
            }
            return value;
        }

        return null;
    }

    /**
     * Load properties from the configuration.properties file
     */
    private static void loadProperties() {
        properties = new Properties();
        FileInputStream file = null;

        // Try to load from current directory first
        String configPath = "configuration.properties";

        // Allow override via system property
        String customPath = System.getProperty("config.file");
        if (customPath != null && !customPath.trim().isEmpty()) {
            configPath = customPath.trim();
        }

        try {
            file = new FileInputStream(configPath);
            properties.load(file);
            logger.info("Successfully loaded configuration from: {}", configPath);
        } catch (final FileNotFoundException e) {
            logger.warn("Configuration file not found at: {}. Using default values.", configPath);
            properties = null;
        } catch (final IOException e) {
            logger.error("Error reading configuration file at: {}", configPath, e);
            properties = null;
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    logger.warn("Error closing configuration file", e);
                }
            }
        }
    }

    /**
     * Get a property value with a default fallback
     * @param propertyName the property key to retrieve
     * @param defaultValue the default value to return if property is not found
     * @return the property value or default value
     */
    public static String getProperties(final String propertyName, final String defaultValue) {
        String value = getProperties(propertyName);
        return (value != null) ? value : defaultValue;
    }
}