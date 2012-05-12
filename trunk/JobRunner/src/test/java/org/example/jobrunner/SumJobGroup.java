package org.example.jobrunner;

import java.util.Set;

import org.ozsoft.jobrunner.Job;
import org.ozsoft.jobrunner.JobException;
import org.ozsoft.jobrunner.JobGroup;

public class SumJobGroup extends JobGroup {
    
    private static final int JOB_COUNT = 3;

    private static final long serialVersionUID = 2242161589344120121L;
    
    private int[] values;
    
    private int sum = 0;
    
    public void setValues(int[] values) {
        this.values = values;
    }
    
    public int getSum() {
        return sum;
    }

    @Override
    protected void split(Set<Job> jobs) throws JobException {
        int noOfValuesPerJob = values.length / JOB_COUNT;
        for (int i = 0; i < JOB_COUNT; i++) {
            SumJob sumJob = getJobRunner().createJob(SumJob.class);
            int[] subvalues = new int[noOfValuesPerJob];
            for (int j = 0; j < noOfValuesPerJob; j++) {
                subvalues[j] = values[(i * noOfValuesPerJob + j)];
            }
            sumJob.setValues(subvalues);
            jobs.add(sumJob);
        }
    }

    @Override
    protected void aggregate(Set<Job> jobs) {
        for (Job job : jobs) {
            sum += ((SumJob) job).getSum();
        }
    }

}
