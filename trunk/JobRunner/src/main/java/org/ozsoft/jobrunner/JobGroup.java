package org.ozsoft.jobrunner;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public abstract class JobGroup extends Job {

    private static final long serialVersionUID = -2002900150196398116L;
    
    private static final Logger LOG = Logger.getLogger(JobGroup.class);
    
    private JobRunner jobRunner;
    
    protected final JobRunner getJobRunner() {
        return jobRunner;
    }
    
    /* package */ void setJobRunner(JobRunner jobRunner) {
        this.jobRunner = jobRunner;
    }

    protected abstract void split(Set<Job> jobs) throws JobException;
    
    protected abstract void aggregate(Set<Job> jobs) throws JobException;
    
    @Override
    protected final void execute() throws JobException {
        LOG.debug(this);
        try {
            final Set<Job> jobs = new HashSet<Job>();
            split(jobs);
            if (jobs.size() == 0) {
                throw new JobException("Splitting resulting in empty set of jobs");
            }
            for (Job job : jobs) {
                job.execute();
            }
            aggregate(jobs);
            state = JobState.COMPLETED;
        } catch (JobException e) {
            state = JobState.FAILED;
            setErrorCause(e);
        }
        LOG.debug(this);
    }

}
