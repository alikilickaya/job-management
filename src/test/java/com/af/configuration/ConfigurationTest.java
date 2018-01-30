package com.af.configuration;

import com.af.constants.TimeConstants;
import com.af.consumers.JobConsumer;
import com.af.models.JobInfo;
import com.af.models.JobState;
import com.af.models.JobType;
import com.af.models.PriorityLevel;
import com.af.queues.JobQueue;
import com.af.utils.DateUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class ConfigurationTest {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    private int jobQuantity;
    private long jobDelay;
    private Date scheduledDate;
    private Date currentTime;

    @Before
    public void setUp() throws ParseException {
        jobQuantity = Configuration.getJobQuantity();
        jobDelay = Configuration.getJobDelay();
        scheduledDate = dateFormat.parse(Configuration.getScheduledDate());
        currentTime = new Date();
    }

    @Test
    public void testGetJobQuantity() {
        assertFalse(jobQuantity <= 0);
    }

    @Test
    public void testGetJobDelay() {
        assertFalse(jobDelay < 0);
    }

    /**
     * Tests delayed job.
     * Delay is being read from configuration.
     *
     * @throws InterruptedException
     */
    @Test
    public void testScheduledJobUsingDelay() throws InterruptedException {
        JobQueue jobQueue = new JobQueue();
        JobInfo<String> jobInfo = new JobInfo<>("low", JobType.SEND_EMAIL, PriorityLevel.LOW, jobDelay);
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

    @Test
    @Ignore
    public void testGetScheduledDate() throws ParseException {
        assertFalse(currentTime.before(scheduledDate));
    }

    /**
     * Tests scheduled job.
     * Schedule date is being read from configuration and it should
     * be 1 minute later of current time.
     * Example if current time "22/01/2018 20:40:00" then scheduled_date on the configuration file should be "22/01/2018 20:41:00"
     *
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testScheduledJobUsingDate() throws InterruptedException {
        long delay = DateUtils.calculateDuration(currentTime, scheduledDate);
        assertFalse(delay <= 0);

        JobQueue jobQueue = new JobQueue();
        JobInfo<String> jobInfo = new JobInfo<>("low", JobType.SEND_EMAIL, PriorityLevel.LOW, TimeConstants.THREE_SECONDS);
        jobQueue.addByPriority(jobInfo);

        JobConsumer consumer = new JobConsumer(jobQueue);

        executorService.execute(consumer);

        assertNotEquals(JobState.SUCCESS, jobInfo.getJobState());
        Thread.sleep(delay + 10);
        assertEquals(JobState.SUCCESS, jobInfo.getJobState());

        Thread.sleep(100);
    }
}