package com.quantitymeasurement.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * ApplicationConfig
 *
 * Singleton class for loading and managing application configuration.
 * Supports environment-based configuration (development, test, production).
 */
public class ApplicationConfig {

    private static final Logger logger = Logger.getLogger(
        ApplicationConfig.class.getName()
    );

    /** Singleton instance */
    private static ApplicationConfig instance;

    /** Loaded properties */
    private Properties properties;

    /** Active environment */
    private Environment environment;

    /**
     * Supported environments
     */
    public enum Environment {
        DEVELOPMENT, TEST, PRODUCTION
    }

    /**
     * Config keys (type-safe)
     */
    public enum ConfigKey {
        REPOSITORY_TYPE("repository.type"),
        DB_DRIVER_CLASS("db.driver"),
        DB_URL("db.url"),
        DB_USERNAME("db.username"),
        DB_PASSWORD("db.password"),
        DB_POOL_SIZE("db.pool-size"),
        HIKARI_MAX_POOL_SIZE("db.hikari.maximum-pool-size"),
        HIKARI_MIN_IDLE("db.hikari.minimum-idle"),
        HIKARI_CONNECTION_TIMEOUT("db.hikari.connection-timeout"),
        HIKARI_IDLE_TIMEOUT("db.hikari.idle-timeout"),
        HIKARI_MAX_LIFETIME("db.hikari.max-lifetime"),
        HIKARI_POOL_NAME("db.hikari.pool-name"),
        HIKARI_CONNECTION_TEST_QUERY("db.hikari.connection-test-query");

        private final String key;

        ConfigKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    /**
     * Private constructor
     */
    private ApplicationConfig() {
        loadConfiguration();
    }

    /**
     * Singleton access
     */
    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }

    /**
     * 🔥 IMPORTANT — Reset for test isolation
     */
    public static void reset() {
        instance = null;
    }

    /**
     * Load configuration from classpath
     */
    private void loadConfiguration() {
        properties = new Properties();

        try {
            // Step 1: Check system/env override
            String env = System.getProperty("app.env");
            if (env == null || env.isEmpty()) {
                env = System.getenv("APP_ENV");
            }

            // Step 2: Load properties file (classpath-safe)
            String configFile = "application.properties";
            InputStream input = ApplicationConfig.class
                    .getClassLoader()
                    .getResourceAsStream(configFile);

            if (input != null) {
                properties.load(input);
                logger.info("Configuration loaded from " + configFile);

                // Step 3: If env not set, read from properties
                if (env == null || env.isEmpty()) {
                    env = properties.getProperty("app.env", "development");
                }

                // Step 4: SAFE enum parsing
                try {
                    this.environment = Environment.valueOf(env.trim().toUpperCase());
                    logger.info("Active Environment: " + this.environment);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid app.env value: " + env);
                }

            } else {
                logger.warning("Configuration file not found, using defaults");
                loadDefaults();
            }

        } catch (Exception e) {
            logger.severe("Error loading configuration: " + e.getMessage());
            loadDefaults();
        }
    }

    /**
     * Default fallback configuration (H2 local DB)
     */
    private void loadDefaults() {
        properties.setProperty("repository.type", "database");
        properties.setProperty("app.env", "development");
        properties.setProperty("db.driver", "org.h2.Driver");
        properties.setProperty("db.url",
                "jdbc:h2:./data/quantitymeasurementdb;AUTO_SERVER=TRUE");
        properties.setProperty("db.username", "sa");
        properties.setProperty("db.password", "");
        properties.setProperty("db.pool-size", "5");
        properties.setProperty("db.hikari.maximum-pool-size", "10");
        properties.setProperty("db.hikari.minimum-idle", "2");
        properties.setProperty("db.hikari.connection-test-query", "SELECT 1");

        this.environment = Environment.DEVELOPMENT;
        logger.info("Default H2 configuration loaded");
    }

    /**
     * Get property
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get property with default
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get int property
     */
    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(
                properties.getProperty(key, String.valueOf(defaultValue))
            );
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Get environment name
     */
    public String getEnvironment() {
        return environment.name();
    }

    /**
     * Validate config key
     */
    public boolean isConfigKey(String key) {
        for (ConfigKey ck : ConfigKey.values()) {
            if (ck.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Debug print
     */
    public void printAllProperties() {
        logger.info("=== ApplicationConfig Properties ===");
        logger.info("Environment: " + environment.name());

        properties.forEach((key, value) -> {
            if (!key.toString().contains("password")) {
                logger.info(key + " = " + value);
            }
        });

        logger.info("====================================");
    }

    public static void main(String[] args) {
        ApplicationConfig config = ApplicationConfig.getInstance();
        config.printAllProperties();
    }
}