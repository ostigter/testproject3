package org.ozsoft.jdiff;

import org.junit.Test;

public class JDiffTest {
    
    @Test
    public void test() {
        String[] source = new String[] {"A", "B", "B", "C"};
        String[] dest   = new String[] {"A", "D", "B", "C"};
        
        JDiff diff = new JDiff(source, dest);
        String[] diffLines = diff.getDiffLines();
        for (String line : diffLines) {
            System.out.println(line);
        }
    }

}
