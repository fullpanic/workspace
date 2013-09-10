package com.ssq;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * get all ssq arrays
 * @author fullpanic
 *
 */
public class SSQ {
    
    public static final int RED_NUM = 6;
    
    public static final int RED_MAX = 33;
    
    public static final int BLUE_NUM = 1;
    
    public static final int BLUE_MAX = 16;
    
    private static List<int[]> totalSeqs = new ArrayList<int[]>();
    
    private static List<int[]> redSeqs = new ArrayList<int[]>();
    
    private static List<Integer> blueSeqs = new ArrayList<Integer>();
    
    private static int[] temp = new int[RED_NUM];
    
    private static List<int[]> result = new ArrayList<int[]>();
    
    /**
     * get all seqs as array
     * @param max seq max num
     * @param index current index
     * @param start start index
     */
    private static void getAllRedSeqs(int max, int index, int start) {
        for (int i = start; i <= max; i++) {
            temp[RED_NUM - index] = i;
            if (index > 1) {
                getAllRedSeqs(max, index - 1, i + 1);
            }
            else {
                //                print(temp);
                int[] t = new int[RED_NUM];
                System.arraycopy(temp, 0, t, 0, RED_NUM);
                totalSeqs.add(t);
            }
        }
    }
    
    /**
     * format print
     * @param arr
     */
    public static void print(int[] arr) {
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
    
    /**
     * convert a line to int array
     * @param string num string 
     * @return array
     */
    public static int[] convert2arr(String string) {
        string = string.trim();
        String[] strings = string.split("\\s+");
        int[] arr = new int[strings.length];
        int count = 0;
        for (String t : strings) {
            if (StringUtils.isNumeric(t)) {
                arr[count++] = Integer.parseInt(t);
            }
        }
        return arr;
    }
    
    /**
     * split all history seqs as red/blue arrays
     * @param list
     */
    private static void splitHistorySeqs(String filename) {
        BufferedReader br = null;
        List<String> list = new ArrayList<String>();
        try {
            br = new BufferedReader(new FileReader(filename));
            String string = null;
            while ((string = br.readLine()) != null) {
                list.add(string.trim());
            }
        }
        catch (Exception e) {
            System.err.println(e);
        }
        finally {
            IOUtils.closeQuietly(br);
        }
        
        for (String line : list) {
            int[] arr = convert2arr(line);
            if (arr.length == (RED_NUM + BLUE_NUM)) {
                blueSeqs.add(arr[arr.length - 1]);
                int[] tmp = new int[RED_NUM];
                System.arraycopy(arr, 0, tmp, 0, RED_NUM);
                redSeqs.add(tmp);
            }
        }
    }
    
    private static void filter(List<int[]> list) {
        int ac = 8;
        int step = 2;
        int odd = 3;
        int[] sum = new int[] {89, 110};
        int[][] area = new int[][] { {2, 2, 2}, {1, 2, 3}, {1, 3, 2}};
        int[] min = new int[] {1, 6};
        int[] max = new int[] {28, 33};
        for (int[] arr : list) {
            boolean status = SSQCalc.isLegal(arr, ac, step, sum, odd, area, min, max);
            if (status) {
                result.add(arr);
            }
        }
        printRes();
        System.out.println(result.size());
    }
    
    public static void printRes() {
        for (int[] arr : result) {
            print(arr);
        }
    }
    
    /**
     * main method
     * @param args
     */
    public static void main(String[] args) {
        //        splitHistorySeqs("./testcase/res.txt");
        //        SSQCalc.statisticHistory(redSeqs);
        getAllRedSeqs(RED_MAX, RED_NUM, 1);
        filter(totalSeqs);
    }
}
