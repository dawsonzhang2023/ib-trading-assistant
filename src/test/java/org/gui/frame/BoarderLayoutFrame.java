package org.gui.frame;

import javax.swing.*;
import java.awt.*;

public class BoarderLayoutFrame extends JFrame {

    public BoarderLayoutFrame() throws HeadlessException {
        // setLayout(null);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JPanel panel5 = new JPanel();

        panel1.setBackground(Color.white);
        panel2.setBackground(Color.blue);
        panel3.setBackground(Color.green);
        panel4.setBackground(Color.gray);
        panel5.setBackground(Color.pink);

        panel1.setPreferredSize(new Dimension(100, 100));
        panel2.setPreferredSize(new Dimension(100, 300));
        panel3.setPreferredSize(new Dimension(100, 100));
        panel4.setPreferredSize(new Dimension(100, 100));
        panel5.setPreferredSize(new Dimension(100, 100));

        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.SOUTH);
        add(panel3, BorderLayout.EAST);
        add(panel4, BorderLayout.WEST);
        add(panel5, BorderLayout.CENTER);

    }


    public static void main(String[] args) {
        BoarderLayoutFrame frame = new BoarderLayoutFrame();
        frame.setVisible(true);
    }


}
