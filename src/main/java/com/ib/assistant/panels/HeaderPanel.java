package com.ib.assistant.panels;

import com.ib.assistant.DashBoardFrame;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {

    private  IbConnectionPanel  connectionPanel = null ;

    private  ControlsPanel  controlsPanel = null ;

    public IbConnectionPanel getConnectionPanel() {
        return connectionPanel;
    }

    public ControlsPanel getControlsPanel() {
        return controlsPanel;
    }

    public HeaderPanel(DashBoardFrame DashBoard ) {
        setPreferredSize(new Dimension(800, 150));
        setLayout(new GridLayout(2, 1 , 10 , 10));

        JPanel   leftHeader = new JPanel( new GridLayout(2 , 1));
        JLabel label = new JLabel();
        label.setText(" 自管式智能化指数系统 Self-Directed Intelligent Index Assistant V 2.2.1");
        label.setBackground(Color.white);
        label.setFont(new Font("仿宋", Font.BOLD, 15));
        label.setHorizontalAlignment(JLabel.LEFT);

        controlsPanel = new ControlsPanel();


        leftHeader.add(label);
        //leftHeader.add(labelEnglish);
        leftHeader.add(controlsPanel);



        connectionPanel= new IbConnectionPanel();
        connectionPanel.setPreferredSize(new Dimension(400, 150));
        add(leftHeader);
        add(connectionPanel);

    }
}
