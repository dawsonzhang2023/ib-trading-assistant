package org.gui.frame;

import javax.swing.*;

public class JTableExamples {

    // frame
    JFrame f;
    // Table
    JTable j;

    // Constructor
    JTableExamples()
    {
        // Frame initialization
        f = new JFrame();

        // Frame Title
        f.setTitle("JTable Example");

        // Data to be displayed in the JTable
        String[][] data = {
                { "Kundan Kumar Jha", "4031", "CSE" , "Kundan Kumar Jha", "4031", "CSE", "Kundan Kumar Jha", "4031", "CSE" ,"Kundan Kumar Jha", "4031", "CSE",  "CSE"}
        };

        // Column Names
        String[]  columnNames = new String[]{
                "证劵代码",
                "数据类型",
                "最新价格",
                "当前持仓",
                "当前市场价值",
                "当前市占比",
                "当前持仓",
                "当前市场价值",
                "当前市占比",
                "账户净值",
                "向上触发价格",
                "向下触发价格",
                "规划金额"
        };


        // Initializing the JTable
        j = new JTable(data, columnNames);
       // j.setBounds(30, 40, 200, 300);

        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(j);
        JPanel  jp = new JPanel();
        jp.add(sp);
        f.add(jp);
        // Frame Size
        f.setSize(500, 200);
        // Frame Visible = true
        f.setVisible(true);
    }

    // Driver  method
    public static void main(String[] args)
    {
        new JTableExamples();
    }

}
