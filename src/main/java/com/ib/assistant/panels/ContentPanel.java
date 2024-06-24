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
        setLayout(new BorderLayout());

        disclourePanel = new JScrollPane();
        disclourePanel.add( new DisclouseArea());

        configPanel = new TQQQConfigurationPanel();
        marketDataPanel = new IBMarketDataPanel();
        JTabbedPane  tab =  new JTabbedPane();

        // 获取屏幕尺寸并设置组件大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.8);
        int height = (int) (screenSize.height * 0.5);
        tab.setPreferredSize(new Dimension(width, height));

        DisclouseArea  disclouseArea = new DisclouseArea();
        disclouseArea.setPreferredSize(new Dimension(width, height / 2));
        tab.addTab( "用户须知 Disclaimer" , new DisclouseArea() );

        tw = new JPanel(new BorderLayout());
        tw.add(marketDataPanel);

        tab.addTab(  "市场数据 Market Data" , tw);
        tab.addTab( "配置文件 Configuration" ,  configPanel );
        add(tab, BorderLayout.CENTER);
    }
}
