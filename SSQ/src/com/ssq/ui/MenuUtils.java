package com.ssq.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;
import org.jfree.chart.JFreeChart;

import com.ssq.HistorySeqs;

/**
 * jmenu class
 * @author fullpanic
 *
 */
public class MenuUtils {
    
    public static MenuItem addChart(final JFreeChart chart, String name) {
        MenuItem item = new MenuItem(name);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UI.frame.getChartPanel().setChart(chart);
                    }
                });
                thread.start();
            }
        });
        return item;
    }
    
    public static MenuItem addRetrieve() {
        MenuItem item2 = new MenuItem("Retrieve");
        item2.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String size =
                            JOptionPane.showInputDialog(UI.frame,
                                "Input the Page Num(20*size):",
                                "Size",
                                JOptionPane.PLAIN_MESSAGE);
                        if (StringUtils.isNumeric(size)) {
                            HistorySeqs.main(new String[] {size});
                            JOptionPane.showMessageDialog(UI.frame,
                                "Retrieve OK",
                                "OK",
                                JOptionPane.INFORMATION_MESSAGE);
                            showDetails();
                        }
                    }
                });
                thread.start();
            }
        });
        return item2;
    }
    
    private static void showDetails() {
        List<int[]> list = UI.loadText();
        Collections.reverse(list);
        Object[][] data = new Object[list.size()][7];
        for (int i = 0; i < list.size(); i++) {
            Object[] t = new Object[7];
            for (int k = 0; k < list.get(i).length; k++) {
                t[k] = list.get(i)[k];
            }
            data[i] = t;
        }
        JTable table = new JTable(data, new String[] {"", "", "", "", "", "", ""});
        JDialog frame = new JDialog(UI.frame, "Details");
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
        contentPane.add(panel);
        frame.pack();
        frame.setVisible(true);
        //
        Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize(); // 获得显示器大小对象  
        Dimension frameSize = frame.getSize(); // 获得窗口大小对象  
        if (frameSize.width > displaySize.width)
            frameSize.width = displaySize.width; // 窗口的宽度不能大于显示器的宽度  
        if (frameSize.height > displaySize.height)
            frameSize.height = displaySize.height; // 窗口的高度不能大于显示器的高度  
        frame.setLocation((displaySize.width - frameSize.width) / 2, (displaySize.height - frameSize.height) / 2);
    }
    
    public static MenuItem showLines() {
        MenuItem item2 = new MenuItem("ShowDetails");
        item2.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        showDetails();
                    }
                });
                thread.start();
            }
        });
        return item2;
    }
}
