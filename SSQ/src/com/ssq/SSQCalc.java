package com.ssq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ssq filter
 * @author fullpanic
 *
 */
public class SSQCalc {
    /**
     * get max continue step num by arr, like as '1 3 4 10 15 20' return '3 4' as 2
     * @param arr sorted num array
     * @return max step
     */
    public static int getMaxSeqs(int[] arr) {
        int step = 1;
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i + 1] == arr[i] + 1) {
                step++;
            }
        }
        return step;
    }
    
    /**
     * get num distribution areas as[1-11],[12-22],[23,33]
     * @param arr array
     * @return areas num
     */
    public static int[] getAreas(int[] arr) {
        int[] areas = new int[] {0, 0, 0};
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] <= 11) {
                areas[0]++;
            }
            if (arr[i] > 11 && arr[i] <= 22) {
                areas[1]++;
            }
            if (arr[i] > 22) {
                areas[2]++;
            }
        }
        return areas;
    }
    
    /**
     * get ac value from array
     * @param arr array
     * @return ac
     */
    public static int getACVal(int[] arr) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                map.put(arr[j] - arr[i], 0);
            }
        }
        return map.size() - arr.length + 1;
    }
    
    /**
     * get odd num 
     * @param arr array
     * @return num
     */
    public static int getOddNum(int[] arr) {
        int num = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] % 2 != 0) {
                num++;
            }
        }
        return num;
    }
    
    /**
     * get sum by array
     * @param arr array
     * @return sum
     */
    public static int getSum(int[] arr) {
        int num = 0;
        for (int i = 0; i < arr.length; i++) {
            num += arr[i];
        }
        return num;
    }
    
    /**
     * statistic all seqs
     * @param redSeqs
     */
    public static void statisticHistory(List<int[]> redSeqs) {
        Map<Integer, Integer> acMap = new HashMap<Integer, Integer>();
        Map<Integer, Integer> stepMap = new HashMap<Integer, Integer>();
        Map<Integer, Integer> sumMap = new HashMap<Integer, Integer>();
        Map<Integer, Integer> oddMap = new HashMap<Integer, Integer>();
        Map<int[], Integer> areaMap = new HashMap<int[], Integer>();
        Map<Integer, Integer> minMap = new HashMap<Integer, Integer>();
        Map<Integer, Integer> maxMap = new HashMap<Integer, Integer>();
        for (int[] arr : redSeqs) {
            put(acMap, SSQCalc.getACVal(arr));
            put(stepMap, SSQCalc.getMaxSeqs(arr));
            put(sumMap, SSQCalc.getSum(arr));
            put(oddMap, SSQCalc.getOddNum(arr));
            put(areaMap, SSQCalc.getAreas(arr));
            put(minMap, arr[0]);
            put(maxMap, arr[arr.length - 1]);
        }
        //print
        //        print(acMap, "ac", false);
        //        print(stepMap, "step", false);
        //        print(sumMap, "sum", false);
        //        print(oddMap, "odd", false);
        //        printArr(areaMap, "area", false);
        print(minMap, "minMap", false);
        print(maxMap, "maxMap", false);
    }
    
    public static void print(Map<Integer, Integer> map, String name, boolean first) {
        System.out.println("==================" + name + "====================");
        List<Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
        
        Collections.sort(list, new Comparator<Entry<Integer, Integer>>() {
            
            @Override
            public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
                return Float.compare(o2.getValue(), o1.getValue());
            }
        });
        for (Entry<Integer, Integer> en : list) {
            System.out.println(en.getKey() + ":" + en.getValue());
            if (first) {
                break;
            }
        }
    }
    
    public static void printArr(Map<int[], Integer> map, String name, boolean first) {
        System.out.println("==================" + name + "====================");
        List<Entry<int[], Integer>> list = new ArrayList<Map.Entry<int[], Integer>>(map.entrySet());
        
        Collections.sort(list, new Comparator<Entry<int[], Integer>>() {
            
            @Override
            public int compare(Entry<int[], Integer> o1, Entry<int[], Integer> o2) {
                return Float.compare(o2.getValue(), o1.getValue());
            }
        });
        for (Entry<int[], Integer> en : list) {
            for (int i : en.getKey()) {
                System.out.print(i + " ");
            }
            System.out.println(":" + en.getValue());
            if (first) {
                break;
            }
        }
    }
    
    private static void put(Map<Integer, Integer> map, int key) {
        int count = 0;
        if (map.containsKey(key)) {
            count = map.get(key);
        }
        map.put(key, count + 1);
    }
    
    private static void put(Map<int[], Integer> map, int[] key) {
        int count = 0;
        boolean flag = false;
        for (Entry<int[], Integer> entry : map.entrySet()) {
            int[] t = entry.getKey();
            flag = true;
            for (int i = 0; i < t.length; i++) {
                if (t[i] != key[i]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                count = entry.getValue();
                map.put(entry.getKey(), count + 1);
                break;
            }
        }
        if (!flag) {
            map.put(key, 1);
        }
    }
    
    public static boolean isLegal(int[] arr, int ac, int step, int[] sum, int odd, int[][] area, int[] min, int[] max) {
        if (getACVal(arr) != ac) {
            return false;
        }
        if (getMaxSeqs(arr) != step) {
            return false;
        }
        int tsum = getSum(arr);
        if (tsum > sum[1] || tsum < sum[0]) {
            return false;
        }
        if (getOddNum(arr) != odd) {
            return false;
        }
        boolean status = true;
        int[] t = getAreas(arr);
        for (int[] ta : area) {
            status = true;
            for (int i = 0; i < t.length; i++) {
                if (t[i] != ta[i]) {
                    status = false;
                    break;
                }
            }
            if (status) {
                break;
            }
        }
        if (!status) {
            return status;
        }
        if (arr[0] > min[1]) {
            return false;
        }
        if (arr[arr.length - 1] < max[0]) {
            return false;
        }
        return status;
    }
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        int[] arr = SSQ.convert2arr("11 16 17 19 24 28");
        System.out.println(getMaxSeqs(arr));
        SSQ.print(getAreas(arr));
        System.out.println(getACVal(arr));
    }
}
