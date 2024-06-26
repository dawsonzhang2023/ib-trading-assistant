package com.ib.assistant.panels;

import apidemo.util.IConnectionConfiguration;
import apidemo.util.VerticalPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class IbConnectionPanel extends JPanel {

    public JTextField m_host = null;
    public JTextField m_port = null;
    public JTextField m_connectOptionsTF = null;

    public JTextField m_clientId = null;

    private JLabel m_status = new JLabel("中断 Disconnected");

    public JButton connectButton = null;

    public JLabel getStatusLabel() {
        return m_status;
    }

    private final JLabel m_defaultPortNumberLabel = new JLabel("真实交易端口 Live Trading Port : 7496"
            + "        模拟账户交易端口 Paper Trading Port : 7497 ");

    private  final  JLabel  expireDateLabel = new JLabel("Expiration Date: 2025-06-30");

    private IConnectionConfiguration m_connectionConfiguration;

    public IConnectionConfiguration getConnectionConfiguration() {
        return m_connectionConfiguration;
    }

    public IbConnectionPanel() {

        setLayout(new BorderLayout());
        if (m_connectionConfiguration == null)
            m_connectionConfiguration = new IConnectionConfiguration.DefaultConnectionConfiguration();

        m_host = new JTextField("localhost", 10);
        m_port = new JTextField(m_connectionConfiguration.getDefaultPort(), 7);
        m_connectOptionsTF = new JTextField(m_connectionConfiguration.getDefaultConnectOptions(), 30);




        connectButton = new JButton("连接");
        connectButton.setPreferredSize(new Dimension( 100 ,  30));
       /* JButton disconnect = new JButton("Disconnect") {
            @Override
            public void actionPerformed() {
                // controller().disconnect();
            }
        };*/

        JPanel p1 = new VerticalPanel();
        p1.add("服务器 Server", m_host);
        p1.add("端口 Port", m_port);
        m_clientId = new JTextField("0", 7);
        p1.add("Account ID", m_clientId);
        if (m_connectionConfiguration.getDefaultConnectOptions() != null) {
            p1.add("Connect options", m_connectOptionsTF);
        }
        p1.add("", m_defaultPortNumberLabel);
        p1.add("" , expireDateLabel );

        JPanel p2 = new VerticalPanel();
        p2.add(connectButton);
        //p2.add( disconnect);
        p2.add(Box.createVerticalStrut(40));

        JPanel p3 = new VerticalPanel();
        p3.setBorder(new EmptyBorder(20, 0, 0, 0));
        p3.add("连接状态 Connection status: ", m_status);

        JPanel p4 = new JPanel(new BorderLayout());
        p4.add(p1, BorderLayout.WEST);
        p4.add(p2, BorderLayout.EAST);
        p4.add(p3, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(p4, BorderLayout.NORTH);

    }


}
