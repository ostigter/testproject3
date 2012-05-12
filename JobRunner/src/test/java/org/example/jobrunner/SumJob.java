package org.example.jobrunner;

import org.ozsoft.jobrunner.Job;
import org.ozsoft.jobrunner.JobException;

public class SumJob extends Job {

    private static final long serialVersionUID = 2663822834736208845L;

    private int[] values;
    
    private int sum = 0;
    
    public void setValues(int[] values) {
        this.values = values;
    }
    
    public int getSum() {
        return sum;
    }

    @Override
    protected void execute() throws JobException {
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
    }

}
