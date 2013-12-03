package com.ssq.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.io.FileUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.ssq.SSQ;
import com.ssq.SSQCalc;

/**
 * @author fullpanic
 *
 */
public class UI {
    
    private static final String TITLE_STRING = "SSQ";
    
    private static final int WIDTH = 1200;
    
    private static final int HEIGHT = 600;
    
    public static ChartFrame frame = null;
    
    private static final String PATH = "./testcase/res.txt";
    
    private static final int RECENT = 50;
    
    /**
     * init frame
     */
    public UI() {
        frame = new ChartFrame(TITLE_STRING, null);
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getChartPanel().setBackground(Color.WHITE);
        
        //center
        //set center pos
        Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize(); // 获得显示器大小对象  
        Dimension frameSize = frame.getSize(); // 获得窗口大小对象  
        if (frameSize.width > displaySize.width)
            frameSize.width = displaySize.width; // 窗口的宽度不能大于显示器的宽度  
        if (frameSize.height > displaySize.height)
            frameSize.height = displaySize.height; // 窗口的高度不能大于显示器的高度  
        frame.setLocation((displaySize.width - frameSize.width) / 2, (displaySize.height - frameSize.height) / 2);
        
        createMenu();
        
    }
    
    public JFreeChart createChart(XYSeriesCollection coll) {
        JFreeChart chart =
            ChartFactory.createXYLineChart("SSQ", "Time", "Number", coll, PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = chart.getXYPlot();
        NumberAxis axis = (NumberAxis)plot.getRangeAxis();
        axis.setAutoRangeIncludesZero(false);
        
        XYLineAndShapeRenderer lineandshaperenderer = (XYLineAndShapeRenderer)plot.getRenderer();
        lineandshaperenderer.setSeriesPaint(0, new Color(0, 0, 128));
        lineandshaperenderer.setBaseShapesVisible(true);
        lineandshaperenderer.setBaseItemLabelsVisible(true);
        return chart;
    }
    
    public XYSeriesCollection createDataSet(List<Integer> values, String name) {
        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        XYSeries series1 = new XYSeries(name);
        for (int i = 0; i < values.size(); i++) {
            series1.add(i + 1, values.get(i));
        }
        seriesCollection.addSeries(series1);
        
        return seriesCollection;
    }
    
    public XYSeriesCollection blueDataSet(List<int[]> list, String name) {
        List<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            values.add(list.get(i)[6]);
        }
        return createDataSet(values, name);
    }
    
    public XYSeriesCollection sumDataSet(List<int[]> list, String name) {
        List<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            int[] arr = new int[6];
            System.arraycopy(list.get(i), 0, arr, 0, 6);
            values.add(SSQCalc.getSum(arr));
        }
        return createDataSet(values, name);
    }
    
    public XYSeriesCollection acDataSet(List<int[]> list, String name) {
        List<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            int[] arr = new int[6];
            System.arraycopy(list.get(i), 0, arr, 0, 6);
            values.add(SSQCalc.getACVal(arr));
        }
        return createDataSet(values, name);
    }
    
    public XYSeriesCollection stepDataSet(List<int[]> list, String name) {
        List<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            int[] arr = new int[6];
            System.arraycopy(list.get(i), 0, arr, 0, 6);
            values.add(SSQCalc.getMaxSeqs(arr));
        }
        return createDataSet(values, name);
    }
    
    public XYSeriesCollection oddDataSet(List<int[]> list, String name) {
        List<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            int[] arr = new int[6];
            System.arraycopy(list.get(i), 0, arr, 0, 6);
            values.add(SSQCalc.getOddNum(arr));
        }
        return createDataSet(values, name);
    }
    
    public XYSeriesCollection maxDataSet(List<int[]> list, String name) {
        List<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            values.add(list.get(i)[5]);
        }
        return createDataSet(values, name);
    }
    
    public XYSeriesCollection minDataSet(List<int[]> list, String name) {
        List<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            values.add(list.get(i)[0]);
        }
        return createDataSet(values, name);
    }
    
    public static List<int[]> loadText() {
        List<int[]> rs = new ArrayList<int[]>();
        int len = RECENT;
        try {
            List<String> list = FileUtils.readLines(new File(PATH));
            for (String line : list) {
                int[] arr = SSQ.convert2arr(line);
                rs.add(arr);
                if (len-- == 0) {
                    break;
                }
            }
        }
        catch (Exception e) {
        }
        Collections.reverse(rs);
        return rs;
    }
    
    public void createMenu() {
        MenuBar menuBar = new MenuBar();
        java.awt.Menu menu = new java.awt.Menu("Data");
        //details
        menu.add(MenuUtils.showLines());
        //get data
        menu.add(MenuUtils.addRetrieve());
        menuBar.add(menu);
        //
        Menu chartMenu = new Menu("Chart");
        //list
        List<int[]> list = loadText();
        //sum
        String name = "Sum";
        JFreeChart chart = createChart(sumDataSet(list, name));
        chartMenu.add(MenuUtils.addChart(chart, name));
        //ac
        name = "AC";
        chart = createChart(acDataSet(list, name));
        chartMenu.add(MenuUtils.addChart(chart, name));
        //step
        name = "Step";
        chart = createChart(stepDataSet(list, name));
        chartMenu.add(MenuUtils.addChart(chart, name));
        //Odd
        name = "Odd";
        chart = createChart(oddDataSet(list, name));
        chartMenu.add(MenuUtils.addChart(chart, name));
        //Odd
        name = "Max";
        XYSeriesCollection collection = maxDataSet(list, name);
        collection.addSeries(minDataSet(list, "Min").getSeries(0));
        chart = createChart(collection);
        chartMenu.add(MenuUtils.addChart(chart, name));
        //blue
        name = "Blue";
        chart = createChart(blueDataSet(list, name));
        chartMenu.add(MenuUtils.addChart(chart, name));
        //add menubar
        menuBar.add(chartMenu);
        frame.setMenuBar(menuBar);
    }
    
    public static void main(String[] args) {
        new UI();
    }
}
