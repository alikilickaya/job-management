package com.af.producers;

import com.af.models.JobInfo;
import com.af.models.JobState;
import com.af.models.JobType;
import com.af.models.PriorityLevel;
import com.af.queues.JobQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JobProducer is producing jobs and adding them to the queue based on priority.
 * If the job has {@link PriorityLevel#HIGH}, it will add it to the head of the queue, otherwise it will add to the end of the queue.
 */
public class JobProducer implements Runnable {
    private final int numberOfJobsToProduce;
    private final JobQueue jobQueue;
    private final PriorityLevel messagePriorityLevel;
    private final long delay;
    private Logger logger = LoggerFactory.getLogger(JobProducer.class);

    /**
     * produces jobs which will be executed immediately
     *
     * @param numberOfJobsToProduce
     * @param jobQueue
     * @param messagePriorityLevel
     */
    public JobProducer(int numberOfJobsToProduce, JobQueue jobQueue, PriorityLevel messagePriorityLevel) {
        this.numberOfJobsToProduce = numberOfJobsToProduce;
        this.jobQueue = jobQueue;
        this.messagePriorityLevel = messagePriorityLevel;
        this.delay = 0;
    }

    /**
     * produces jobs which will be executed base on delay
     *
     * @param numberOfJobsToProduce
     * @param jobQueue
     * @param messagePriorityLevel
     * @param delay
     */
    public JobProducer(int numberOfJobsToProduce, JobQueue jobQueue, PriorityLevel messagePriorityLevel, long delay) {
        this.numberOfJobsToProduce = numberOfJobsToProduce;
        this.jobQueue = jobQueue;
        this.messagePriorityLevel = messagePriorityLevel;
        this.delay = delay;

        logger.info("A producer is submitted");
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfJobsToProduce; i++) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }

            addJobToQueue(new JobInfo<>("data_" + (i + 1), JobType.SEND_EMAIL, messagePriorityLevel, delay));

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("thread interrupted, " + e.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error("unexpected exception is occured, " + e.getMessage());
            }
        }

        logger.info("job exit is  being added to queue");
        addJobToQueue(new JobInfo<>("exit", JobType.EXIT, PriorityLevel.LOW, delay));
    }

    private void addJobToQueue(JobInfo<String> jobInfo) {
        if (jobQueue.addByPriority(jobInfo)) {
            jobInfo.setJobState(JobState.QUEUED);
        } else {
            jobInfo.setJobState(JobState.FAILED);
        }
    }
}
