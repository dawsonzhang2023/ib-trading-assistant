package com.ib.assistant.panels;

import javax.swing.*;
import java.awt.*;

public class ControlsPanel extends JPanel {

    private JButton startButton ;

    private JButton pauseButton;

    private  JButton manualButton;

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getPauseButton() {
        return pauseButton;
    }

    public JButton getManualButton() {
        return manualButton;
    }

    public ControlsPanel() {
        setLayout(new FlowLayout(FlowLayout.LEADING));
        startButton = new JButton("开始 Start");
        pauseButton = new JButton("暂停 Pause");
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        p1.setPreferredSize(new Dimension(200, 50));
        p1.add(startButton);
        p1.add(pauseButton);

        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        p2.setPreferredSize(new Dimension(200, 50));
        manualButton = new JButton("手动 Manual");

        p2.add(manualButton);

        add(p1);
        add(p2);

    }
}
