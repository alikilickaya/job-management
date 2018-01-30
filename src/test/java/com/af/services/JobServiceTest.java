package com.af.services;

import com.af.constants.TimeConstants;
import com.af.consumers.JobConsumer;
import com.af.models.JobInfo;
import com.af.models.JobState;
import com.af.models.JobType;
import com.af.models.PriorityLevel;
import com.af.queues.JobQueue;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class JobServiceTest {
    ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Test
    public void testSendEmail() throws InterruptedException {
        JobQueue jobQueue = new JobQueue();
        JobInfo<String> sendEmailJob = new JobInfo<>("email_text", JobType.SEND_EMAIL, PriorityLevel.LOW);
        jobQueue.addByPriority(sendEmailJob);

        JobConsumer consumer = new JobConsumer(jobQueue);

        executorService.execute(consumer);

        waitForSimulationOfJob();
        assertEquals(JobState.SUCCESS, sendEmailJob.getJobState());
    }

    @Test
    public void testLoadData() throws InterruptedException {
        JobQueue jobQueue = new JobQueue();
        JobInfo<String> loadDataJob = new JobInfo<>("data_load", JobType.LOAD_DATA, PriorityLevel.LOW);
        jobQueue.addByPriority(loadDataJob);

        JobConsumer consumer = new JobConsumer(jobQueue);

        executorService.execute(consumer);

        waitForSimulationOfJob();
        assertEquals(JobState.SUCCESS, loadDataJob.getJobState());
    }

    @Test
    public void testIndexFiles() throws InterruptedException {
        JobQueue jobQueue = new JobQueue();
        JobInfo<String> fileIndexJob = new JobInfo<>("file index", JobType.INDEX_FILES, PriorityLevel.LOW);
        jobQueue.addByPriority(fileIndexJob);

        JobConsumer consumer = new JobConsumer(jobQueue);

        executorService.execute(consumer);

        waitForSimulationOfJob();
        assertEquals(JobState.SUCCESS, fileIndexJob.getJobState());
    }

    private void waitForSimulationOfJob() throws InterruptedException {
        Thread.sleep(TimeConstants.HALF_SECOND + TimeConstants.PROCESS_TIME);
    }
}

