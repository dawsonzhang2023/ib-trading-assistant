package org.gui.frame;

import com.ib.assistant.market.IBMarketDataPanel;

import javax.swing.*;
import java.awt.*;

public class TestMartketPanel {

    public static void main(String[] args) {
        JFrame  mainFrame = new JFrame();
        IBMarketDataPanel  marketDataPanel = new IBMarketDataPanel();
        marketDataPanel.setBounds( 10 , 10 , 1000 , 500);
        mainFrame.add(marketDataPanel);
        mainFrame.setVisible(true);
        mainFrame.setSize(new Dimension(1000, 500));
    }
}
