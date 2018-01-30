package com.af.queues;

import com.af.models.JobInfo;
import com.af.models.JobType;
import com.af.models.PriorityLevel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AbstractJobDequeueTest {

    /**
     * Test take(), offerLast() and offerFirst() methods
     *
     * @throws InterruptedException
     */
    @Test
    public void testTakeAndOfferFirstAndOfferLast() throws InterruptedException {
        JobQueue jobQueue = new JobQueue();
        JobInfo<String> lowJobInfo = new JobInfo<>("low", JobType.SEND_EMAIL, PriorityLevel.LOW);
        JobInfo<String> highJobInfo = new JobInfo<>("high", JobType.SEND_EMAIL, PriorityLevel.HIGH);

        jobQueue.offerLast(lowJobInfo);
        jobQueue.offerFirst(highJobInfo);

        assertEquals(2, jobQueue.size());

        assertEquals("high", jobQueue.take().getData());
        assertEquals(1, jobQueue.size());

        assertEquals("low", jobQueue.take().getData());
        assertFalse(!jobQueue.isEmpty());
    }
}
