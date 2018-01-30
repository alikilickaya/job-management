package com.af.queues;

import com.af.models.JobInfo;
import com.af.models.PriorityLevel;

public class JobQueue extends AbstractJobDequeue<String> {
    /**
     * Adds job to queue based on priority.
     * If the job has {@link PriorityLevel#HIGH}, it will add it to the head of the queue, otherwise it will add to the end of the queue.
     *
     * @param jobInfo jobInfo which will be added to the queue
     * @return true if the addition process is successful, otherwise returns false.
     */
    public boolean addByPriority(JobInfo<String> jobInfo) {
        if (PriorityLevel.HIGH == jobInfo.getPriorityLevel()) {
            return offerFirst(jobInfo);
        }

        return offerLast(jobInfo);
    }

}
