package com.ssq;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Test {
    
    private static final String PATH = "./testcase/res.txt";
    
    public static void main(String[] args) {
        try {
            List<String> list = FileUtils.readLines(new File(PATH));
            int i = 0;
            for (String line : list) {
                int[] arr = SSQ.convert2arr(line);
                int[] t = new int[6];
                System.arraycopy(arr, 0, t, 0, 6);
                int v = SSQCalc.getACVal(t);
                int sunm = SSQCalc.getSum(t);
                int s = SSQCalc.getMaxSeqs(t);
                int a = SSQCalc.getOddNum(t);
                int[] tt = SSQCalc.getAreas(t);
                System.err.println(i++ + ":" + Arrays.toString(tt));
            }
        }
        catch (Exception e) {
            // TODO: handle exception
        }
    }
}
