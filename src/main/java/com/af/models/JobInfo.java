package com.af.models;

import java.io.Serializable;
import java.util.Objects;

public class JobInfo<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 8256726251492277771L;

    private T data;
    private transient JobType jobType;
    private transient PriorityLevel priorityLevel;
    private transient JobState jobState;
    private transient long delay;

    public JobInfo(T data, JobType jobType, PriorityLevel priorityLevel) {
        this.data = data;
        this.jobType = jobType;
        this.priorityLevel = priorityLevel;
        this.jobState = JobState.WAITING;
        this.delay = 0;
    }

    public JobInfo(T data, JobType jobType, PriorityLevel priorityLevel, long delay) {
        this.data = data;
        this.jobType = jobType;
        this.priorityLevel = priorityLevel;
        this.jobState = JobState.WAITING;
        this.delay = delay;
    }

    public T getData() {
        return data;
    }

    public JobType getJobType() {
        return jobType;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public long getDelay() {
        return delay;
    }

    public JobState getJobState() {
        return jobState;
    }

    public void setJobState(JobState jobState) {
        this.jobState = jobState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobInfo<?> jobInfo = (JobInfo<?>) o;

        if (delay != jobInfo.delay) return false;
        if (!data.equals(jobInfo.data)) return false;
        if (jobType != jobInfo.jobType) return false;
        if (priorityLevel != jobInfo.priorityLevel) return false;
        return jobState == jobInfo.jobState;
    }

    @Override
    public int hashCode() {

        return Objects.hash(data, jobType, priorityLevel, jobState, delay);
    }

    @Override
    public String toString() {
        return "JobInfo{" +
                "data=" + data +
                ", jobType=" + jobType +
                ", priorityLevel=" + priorityLevel +
                ", jobState=" + jobState +
                ", delay=" + delay +
                '}';
    }
}
