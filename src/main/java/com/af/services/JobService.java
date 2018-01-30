package com.af.services;

import com.af.constants.TimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobService implements IJobService {
    private Logger logger = LoggerFactory.getLogger(JobService.class);

    @Override
    public boolean sendEmail(String data) {
        logger.info("email is being sent");
        try {
            simulateJobAction();
            logger.info("email is sent");
            return true;
        } catch (InterruptedException e) {
            logger.error("email sending is failed, exception: " + e.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean loadData(String data) {
        logger.info("date is being loaded");
        try {
            simulateJobAction();
            logger.info("data is loaded");
            return true;
        } catch (InterruptedException e) {
            logger.error("data loading is failed, exception: " + e.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean indexFiles(String data) {
        logger.info("files are being indexed");
        try {
            simulateJobAction();
            logger.info("files are indexed");
            return true;
        } catch (InterruptedException e) {
            logger.error("file indexing is failed, exception: " + e.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private void simulateJobAction() throws InterruptedException {
        Thread.sleep(TimeConstants.HALF_SECOND);
    }
}
