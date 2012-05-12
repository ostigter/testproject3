package org.ozsoft.jobrunner;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class JobRunnerImpl implements JobRunner {
    
    private static final Logger LOG = Logger.getLogger(JobRunnerImpl.class);
    
    private final Map<Long, Job> jobs;
    
    private long nextId = 1;
    
    public JobRunnerImpl() {
        jobs = new HashMap<Long, Job>();
    }

    @Override
    public <T extends Job> T createJob(Class<T> clazz) throws JobException {
        if (clazz == null) {
            throw new IllegalArgumentException("Null clazz");
        }
        
        T job = null;
        try {
            job = clazz.newInstance();
            long id = nextId++;
            job.setId(id);
            if (job instanceof JobGroup) {
                ((JobGroup) job).setJobRunner(this);
            }
            jobs.put(id, job);
            LOG.debug(job);
        } catch (Exception e) {
            throw new JobException(String.format("Could not instantiate job type '%s'", clazz), e);
        }
        return job;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Job> T getJob(long id) throws JobException {
        Job job = jobs.get(id);
        if (job != null) {
            return (T) job;
        } else {
            throw new JobException(String.format("Job with ID %d not found", id));
        }
    }
    
    @Override
    public void scheduleJob(Job job) throws JobException {
        if (job == null) {
            throw new IllegalArgumentException("Null job");
        }
        
        if (job.getState() != JobState.CREATED) {
            throw new IllegalArgumentException("Job not in CREATED state");
        }
        
        job.setState(JobState.SCHEDULED);
        LOG.debug(job);
        jobs.put(job.getId(), job);
        //TODO: Execute queued jobs in background.
        executeJob(job);
    }
    
    private void executeJob(Job job) {
        job.setState(JobState.RUNNING);
        LOG.debug(job);
        try {
            job.execute();
            if (job.getState() != JobState.ABORTED) {
                job.setState(JobState.COMPLETED);
            }
        } catch (JobException e) {
            job.setState(JobState.FAILED);
        }
        LOG.debug(job);
        jobs.remove(job);
    }

    @Override
    public void abortJob(long id) {
        Job job = jobs.get(id);
        if (job != null) {
            job.abort();
        }
    }

}
