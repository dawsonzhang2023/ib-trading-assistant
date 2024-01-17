package com.ib.assistant.panels;

import com.ib.assistant.content.DisclouseArea;
import com.ib.assistant.content.TQQQConfigurationPanel;
import com.ib.assistant.market.IBMarketDataPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ContentPanel extends JPanel {

    JScrollPane disclourePanel =  null ;

    IBMarketDataPanel  marketDataPanel;

    JPanel  tw;

    public IBMarketDataPanel getMarketDataPanel() {
        return marketDataPanel;
    }

    TQQQConfigurationPanel configPanel;

    public TQQQConfigurationPanel getConfigPanel() {
        return configPanel;
    }

    public ContentPanel() {
        disclourePanel = new JScrollPane();
        disclourePanel.setPreferredSize( new Dimension(1600 , 500 ));
        disclourePanel.add( new DisclouseArea());

        configPanel = new TQQQConfigurationPanel();



        marketDataPanel = new IBMarketDataPanel();
        JTabbedPane  tab =  new JTabbedPane();
        tab.setPreferredSize(new Dimension(1600 , 500));
        DisclouseArea  disclouseArea = new DisclouseArea();
        disclouseArea.setPreferredSize( new Dimension(1590 , 300));
        tab.addTab( "用户须知 Disclaimer" , new DisclouseArea() );

        tw=new JPanel();
        tw.add( marketDataPanel);
        tw.add(disclouseArea);

        tab.addTab(  "市场数据 Market Data" , tw);
        tab.addTab( "配置文件 Configuration" ,  configPanel );
        add( tab );
    }
}
