package com.ssq;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;

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
        print(acMap, "ac", false);
        print(stepMap, "step", false);
        print(sumMap, "sum", false);
        print(oddMap, "odd", false);
        printArr(areaMap, "area", false);
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
    
    public static void getsub(int arr[], int b) {
        Set<Integer> set = new HashSet<Integer>();
        int[] raw = arr;
        double avg = 0;
        for (int i : raw) {
            set.add(Math.abs(i - b));
            avg += i;
        }
        avg = avg / raw.length;
        int f1 = (int)(0.618 * avg);
        int f2 = (int)(0.382 * avg);
        int f3 = (int)(0.236 * avg);
        Set<Integer> fset = new HashSet<Integer>();
        fset.add(f1);
        fset.add(f2);
        fset.add(f3);
        System.err.println(fset);
        //load all
        try {
            List<int[]> out = new ArrayList<int[]>();
            List<String> list = FileUtils.readLines(new File("testcase", "out.txt"));
            for (String line : list) {
                arr = SSQ.convert2arr(line);
                int count = 0, sub = 0;
                boolean flag = true;
                for (int i : arr) {
                    if (set.contains(i)) {
                        flag = false;
                        break;
                    }
                    if (fset.contains(i) || fset.contains(i - 1) || fset.contains(i + 1)) {
                        count++;
                    }
                    sub = i - sub;
                    if (sub >= 15) {
                        flag = false;
                        break;
                    }
                    sub = i;
                }
                flag &= (count < 3);
                //heri
                count = 0;
                for (int i : arr) {
                    for (int j : raw) {
                        if (i == j) {
                            count++;
                        }
                    }
                }
                flag &= (count == 1);
                //out
                if (flag) {
                    out.add(arr);
                }
            }
            //out
            List<int[]> rawlist = read("./testcase/res.txt");
            int t = 0;
            Iterator<int[]> iterator = out.iterator();
            for (; iterator.hasNext();) {
                int[] a = iterator.next();
                for (int[] bb : rawlist) {
                    t = 0;
                    for (int i = 0; i < 3; i++) {
                        if (a[i] == bb[i]) {
                            t++;
                        }
                    }
                    if (t > 2) {
                        iterator.remove();
                        break;
                    }
                }
                t = 0;
            }
            System.out.println(out.size());
            SSQ.printRes(out, new File("testcase", "new.txt"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static List<int[]> read(String path) {
        List<int[]> res = new ArrayList<int[]>();
        try {
            List<String> list = FileUtils.readLines(new File(path));
            for (String line : list) {
                res.add(SSQ.convert2arr(line));
            }
        }
        catch (Exception e) {
            
        }
        return res;
    }
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        //05 06 10 14 27 31 14
        int[] arr = SSQ.convert2arr("20 21 22 23 25 27");
        System.err.println("AC:" + getACVal(arr));
        getsub(arr, 8);
    }
}
