package org.ozsoft.jobrunner;

import junit.framework.Assert;

import org.example.jobrunner.SumJob;
import org.example.jobrunner.SumJobGroup;
import org.junit.Test;

public class JobRunnerTest {
    
    @Test
    public void sumJob() throws JobException {
        JobRunner jobRunner = new JobRunnerImpl();
        
        SumJob sumJob = jobRunner.createJob(SumJob.class);
        Assert.assertEquals(JobState.CREATED, sumJob.getState());
        sumJob.setValues(new int[] {1, 2, 3, 4, 5});
        jobRunner.scheduleJob(sumJob);
        
        Assert.assertEquals(JobState.COMPLETED, sumJob.getState());
        Assert.assertEquals(15, sumJob.getSum());
    }

    @Test
    public void sumJobGroup() throws JobException {
        JobRunner jobRunner = new JobRunnerImpl();
        
        SumJobGroup sumJobGroup = jobRunner.createJob(SumJobGroup.class);
        Assert.assertEquals(JobState.CREATED, sumJobGroup.getState());
        sumJobGroup.setValues(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9});
        jobRunner.scheduleJob(sumJobGroup);
        
        Assert.assertEquals(JobState.COMPLETED, sumJobGroup.getState());
        Assert.assertEquals(45, sumJobGroup.getSum());
    }

}
