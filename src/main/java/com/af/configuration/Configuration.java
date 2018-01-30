package com.af.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static final Properties props = new Properties();
    private static Logger logger = LoggerFactory.getLogger(Configuration.class);

    static {
        try (InputStream inputStream = Configuration.class.getResourceAsStream("/config.properties")) {
            props.load(inputStream);
        } catch (IOException e) {
            logger.error("config.properties could not be read. error: " + e.getMessage());
        }
    }

    public static int getJobQuantity() {
        return Integer.parseInt(props.getProperty("job_quantity"));
    }

    public static long getJobDelay() {
        return Long.parseLong(props.getProperty("job_delay"));
    }

    public static String getScheduledDate() {
        return props.getProperty("scheduled_date");
    }
}
