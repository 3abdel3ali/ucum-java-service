/*
License

This project is licensed under the Apache License 2.0 with an additional attribution clause.

Any use of this code must provide explicit attribution to:
- The original author: Abdelali Boussadi, PhD
- The original repository: https://github.com/3abdel3ali/ucum-java-service

See the LICENSE file for full details.
*/
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
     * @return the property value
     * @throws IllegalArgumentException if property is not found
     */
    public static String getProperties(final String propertyName) {
        if (properties == null) {
            loadProperties();
        }

        String value = properties.getProperty(propertyName);
        if (value == null || value.trim().isEmpty()) {
            logger.error("Required property '{}' not found in configuration file", propertyName);
            throw new IllegalArgumentException("Required property '" + propertyName + "' is missing from configuration.properties");
        }
        logger.debug("Loaded property '{}' with value: {}", propertyName, value);
        return value.trim();
    }

    /**
     * Load properties from the configuration.properties file
     * @throws RuntimeException if configuration file cannot be loaded
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
            String errorMsg = "Configuration file not found at: " + configPath + ". This file is required to run the application.";
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg, e);
        } catch (final IOException e) {
            String errorMsg = "Error reading configuration file at: " + configPath;
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
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

}