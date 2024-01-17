package org.gui.frame;

import javax.swing.*;
import java.awt.*;

public class GridLayoutFrame extends JFrame {

    public GridLayoutFrame() throws HeadlessException {
        // setLayout(null);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout( new GridLayout( 3, 3));

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JPanel panel5 = new JPanel();
        JPanel panel6 = new JPanel();
        JPanel panel7 = new JPanel();
        JPanel panel8 = new JPanel();
        JPanel panel9 = new JPanel();

        panel1.setBackground(Color.white);
        panel2.setBackground(Color.blue);
        panel3.setBackground(Color.green);
        panel4.setBackground(Color.gray);
        panel5.setBackground(Color.pink);
        panel6.setBackground(Color.blue);
        panel7.setBackground(Color.green);
        panel8.setBackground(Color.gray);
        panel9.setBackground(Color.pink);

        panel1.setPreferredSize(new Dimension(100, 100));
        panel2.setPreferredSize(new Dimension(100, 100));
        panel3.setPreferredSize(new Dimension(100, 100));
        panel4.setPreferredSize(new Dimension(100, 100));
        panel5.setPreferredSize(new Dimension(100, 100));

        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);
        add(panel5);
        add(panel6);
        add(panel7);
        add(panel8);
        add(panel9);


    }


    public static void main(String[] args) {
        GridLayoutFrame frame = new GridLayoutFrame();
        frame.setVisible(true);
    }


}
