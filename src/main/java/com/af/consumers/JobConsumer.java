package com.af.consumers;

import com.af.executers.JobExecutor;
import com.af.models.JobInfo;
import com.af.models.JobState;
import com.af.models.JobType;
import com.af.queues.AbstractJobDequeue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * JobConsumer is retrieves and removes the head of the queue (in other words, the first element of this deque),
 * waiting if necessary until an element becomes available.
 * Then it schedules the taken job and pass it to the JobExecutor.
 */
public class JobConsumer implements Runnable {
    private Logger logger = LoggerFactory.getLogger(JobConsumer.class);
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    private AbstractJobDequeue<String> jobQueue;

    public JobConsumer(AbstractJobDequeue<String> jobQueue) {
        this.jobQueue = jobQueue;
        logger.info("A consumer is submitted");

    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                JobInfo<String> takenJobInfo = jobQueue.take();
                if (takenJobInfo.getJobType() == JobType.EXIT) {
                    logger.info("exiting from Job Consumer");
                    takenJobInfo.setJobState(JobState.SUCCESS);
                    break;
                }

                runJobExecutor(takenJobInfo);
            }
        } catch (InterruptedException e) {
            logger.error("exception: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("unexpected exception is occured, " + e.getMessage());
        }
    }

    private void runJobExecutor(final JobInfo<String> takenJobInfo) {
        takenJobInfo.setJobState(JobState.RUNNING);
        JobExecutor jobExecutor = new JobExecutor(takenJobInfo);
        executorService.schedule(jobExecutor, takenJobInfo.getDelay(), TimeUnit.MILLISECONDS);
    }
}
