package com.af.executers;

import com.af.models.JobInfo;
import com.af.models.JobState;
import com.af.models.JobType;
import com.af.services.IJobService;
import com.af.services.JobService;
import org.slf4j.LoggerFactory;

/**
 * Executes the job based on its type.
 */
public class JobExecutor implements Runnable {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(JobExecutor.class);

    private JobInfo<String> jobInfo;
    private IJobService jobService = new JobService();

    public JobExecutor(JobInfo<String> jobInfo) {
        this.jobInfo = jobInfo;
    }

    /**
     * Performing action of job based on job type
     */
    @Override
    public void run() {
        try {
            logger.info("job {} is being executed", jobInfo.getData());
            boolean jobActionSuccessful = false;

            if (JobType.SEND_EMAIL == jobInfo.getJobType()) {
                jobActionSuccessful = jobService.sendEmail(jobInfo.getData());
            } else if (JobType.LOAD_DATA == jobInfo.getJobType()) {
                jobActionSuccessful = jobService.loadData(jobInfo.getData());
            } else if (JobType.INDEX_FILES == jobInfo.getJobType()) {
                jobActionSuccessful = jobService.indexFiles(jobInfo.getData());
            }

            setJobState(jobActionSuccessful);
            logger.info("job {} is executed", jobInfo.getData());
        } catch (Exception e) {
            logger.error("unexpected exception is occured, " + e.getMessage());
        }
    }

    private void setJobState(boolean jobActionSuccessful) {
        if (jobActionSuccessful) {
            jobInfo.setJobState(JobState.SUCCESS);
        } else {
            jobInfo.setJobState(JobState.FAILED);
        }
    }
}
