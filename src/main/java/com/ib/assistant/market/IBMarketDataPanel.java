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
        setLayout(new BorderLayout());
        marketDateTable = new JTable(dataModel);
        marketDateTable.setRowHeight(30);

        DefaultTableCellRenderer  cellRenderer = new DefaultTableCellRenderer.UIResource();
        cellRenderer.setHorizontalAlignment( SwingConstants.CENTER );
        marketDateTable.setDefaultRenderer(Boolean.class , cellRenderer);

        // 设置表头
        JScrollPane scrollPane = new JScrollPane(marketDateTable);
        add(scrollPane, BorderLayout.CENTER);

        marketDateTable.getTableHeader().setPreferredSize(new Dimension(marketDateTable.getColumnModel().getTotalColumnWidth(), 30));

        // 获取屏幕尺寸并设置组件大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.8);
        int height = (int) (screenSize.height * 0.8);
        setPreferredSize(new Dimension(width, height / 2));
    }
}
