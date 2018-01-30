package com.af.queues;

import com.af.constants.TimeConstants;
import com.af.consumers.JobConsumer;
import com.af.models.JobInfo;
import com.af.models.JobState;
import com.af.models.JobType;
import com.af.models.PriorityLevel;
import com.af.producers.JobProducer;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class JobQueueTest {
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    /**
     * Tests job priority
     *
     * @throws InterruptedException
     */
    @Test
    public void testAddByPriority() throws InterruptedException {
        JobQueue jobQueue = new JobQueue();
        JobInfo<String> lowJobInfo = new JobInfo<>("low", JobType.SEND_EMAIL, PriorityLevel.LOW);
        JobInfo<String> highJobInfo = new JobInfo<>("high", JobType.SEND_EMAIL, PriorityLevel.HIGH);

        jobQueue.addByPriority(lowJobInfo);
        jobQueue.addByPriority(highJobInfo);
        jobQueue.addByPriority(lowJobInfo);
        jobQueue.addByPriority(highJobInfo);

        assertEquals(4, jobQueue.size());

        assertEquals(PriorityLevel.HIGH, jobQueue.take().getPriorityLevel());
        assertEquals(3, jobQueue.size());

        assertEquals(PriorityLevel.HIGH, jobQueue.take().getPriorityLevel());
        assertEquals(2, jobQueue.size());

        assertEquals(PriorityLevel.LOW, jobQueue.take().getPriorityLevel());
        assertEquals(1, jobQueue.size());

        assertEquals(PriorityLevel.LOW, jobQueue.take().getPriorityLevel());
        assertFalse(!jobQueue.isEmpty());
    }

    @Test
    public void testAddMultipleJobInfo() {
        JobQueue jobQueue = new JobQueue();
        for (int i = 0; i < 10; i++) {
            JobInfo<String> info = new JobInfo<>("hello " + i, JobType.SEND_EMAIL, PriorityLevel.LOW);
            jobQueue.addByPriority(info);
        }

        assertEquals(10, jobQueue.size());
    }


    /**
     * Tests JobActions are executed immediately
     *
     * @throws InterruptedException
     */
    @Test
    public void testImmediateJob() throws InterruptedException {
        JobQueue jobQueue = new JobQueue();

        JobConsumer consumer = new JobConsumer(jobQueue);
        JobProducer producer = new JobProducer(5, jobQueue, PriorityLevel.LOW);

        executorService.execute(producer);
        executorService.execute(consumer);

        Thread.sleep(TimeConstants.THREE_SECONDS);
    }

    /**
     * Tests scheduled job.
     * Produces a job which is scheduled to 3 seconds later of current time.
     * After the job will be executed, state of job will be {@link JobState.SUCCESS}
     *
     * @throws InterruptedException
     */
    @Test
    public void testScheduledJob() throws InterruptedException {
        JobQueue jobQueue = new JobQueue();
        JobInfo<String> jobInfo = new JobInfo<>("low", JobType.SEND_EMAIL, PriorityLevel.LOW, TimeConstants.THREE_SECONDS);
        jobQueue.addByPriority(jobInfo);

        JobConsumer consumer = new JobConsumer(jobQueue);

        executorService.execute(consumer);

        /**
         * Immediate time: state of the job should be {@link JobState.WAITING}
         */
        assertEquals(JobState.WAITING, jobInfo.getJobState());
        Thread.sleep(TimeConstants.ONE_SECOND);

        /**
         * 1 second later: state of the job should be {@link JobState.RUNNING}
         */
        assertEquals(JobState.RUNNING, jobInfo.getJobState());
        Thread.sleep(TimeConstants.ONE_SECOND);

        /**
         * 2 seconds later: state of the job should be {@link JobState.RUNNING}
         */
        assertEquals(JobState.RUNNING, jobInfo.getJobState());
        Thread.sleep(TimeConstants.ONE_SECOND + TimeConstants.HALF_SECOND + TimeConstants.PROCESS_TIME);

        /**
         * 3,5 seconds later: state of the job should be {@link JobState.SUCCESS}
         * (the delay is 3 seconds, job simulation takes half a second,
         * all other processess takes 20 miliseconds)
         */
        assertEquals(JobState.SUCCESS, jobInfo.getJobState());


        Thread.sleep(TimeConstants.PROCESS_TIME);
    }
}