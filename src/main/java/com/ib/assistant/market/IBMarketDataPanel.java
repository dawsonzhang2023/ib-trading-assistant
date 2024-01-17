package com.ib.assistant.market;

import com.ib.assistant.content.DisclouseArea;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class IBMarketDataPanel extends JPanel {

    private JTable marketDateTable;

    private MarketDataModel dataModel;

    public JTable getMarketDateTable() {
        return marketDateTable;
    }

    public MarketDataModel getDataModel() {
        return dataModel;
    }

    public IBMarketDataPanel() {

        dataModel = new MarketDataModel();
        setPreferredSize(new Dimension(1600, 200));
        setLayout(new GridLayout(4, 1));
        marketDateTable = new JTable(dataModel);
        //marketDateTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        setAutoscrolls(true);
        marketDateTable.setRowHeight(50);
       DefaultTableCellRenderer  cellRenderer = new DefaultTableCellRenderer.UIResource();
       cellRenderer.setHorizontalAlignment( SwingConstants.CENTER );
       marketDateTable.setDefaultRenderer(Boolean.class , cellRenderer);
        add(marketDateTable.getTableHeader());
        marketDateTable.getTableHeader().setPreferredSize(new Dimension(1600, 50));
        //marketDateTable.setAutoscrolls(true);


        add(marketDateTable);

    }
}
