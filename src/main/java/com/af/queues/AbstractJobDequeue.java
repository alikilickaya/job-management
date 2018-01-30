package com.af.queues;

import com.af.models.JobInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class AbstractJobDequeue<T extends Serializable> extends LinkedBlockingDeque<JobInfo<T>> {
    protected Logger logger = LoggerFactory.getLogger(AbstractJobDequeue.class);

    @Override
    public JobInfo<T> take() throws InterruptedException {
        JobInfo<T> takenJobInfo = super.takeFirst();
        logger.info("job " + takenJobInfo.getData() + " is taken from head of the queue");

        return takenJobInfo;
    }

    @Override
    public boolean offerFirst(JobInfo<T> jobInfo) {
        if (jobInfo == null) {
            logger.warn("null cannot be added to the queue");
            return false;
        }

        if (super.offerFirst(jobInfo)) {
            logger.info("job {} is added to the queue", jobInfo.getData());
            return true;
        } else {
            logger.info("job {} is not added to the queue", jobInfo.getData());
            return false;
        }
    }

    @Override
    public boolean offerLast(JobInfo<T> jobInfo) {
        if (jobInfo == null) {
            logger.warn("null cannot be added to the queue");
            return false;
        }

        if (super.offerLast(jobInfo)) {
            logger.info("job {} is added to the queue", jobInfo.getData());
            return true;
        } else {
            logger.info("job {} is not added to the queue", jobInfo.getData());
            return false;
        }
    }
}
