package com.ib.assistant.panels;

import apidemo.util.HtmlButton;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FooterPanel extends JPanel {

    private JTextArea m_msg = null;

    private JTextArea m_inLog = null;
    private JTextArea m_outLog = null;

    public JTextArea getM_msg() {
        return m_msg;
    }

    public JTextArea getM_inLog() {
        return m_inLog;
    }

    public JTextArea getM_outLog() {
        return m_outLog;
    }

    private HtmlButton  clearBtn;

    public FooterPanel(int width, int height) {

        setPreferredSize(new Dimension(width, (int) (height*0.3)));
        setLayout( new BorderLayout());
        Border b = new EmptyBorder(5 , 5 , 5 , 5 );
        setBorder(b);

        m_msg = new JTextArea();
        m_inLog = new JTextArea();
        m_outLog = new JTextArea();

        m_msg.setEditable(false);
        m_msg.setLineWrap(true);

        clearBtn = new HtmlButton("clear");
        clearBtn.setBounds(0 , 200 , 40 , 20);
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_msg.setText("");
            }
        });

        JScrollPane msgScroll = new JScrollPane(m_msg);

        JScrollPane outLogScroll = new JScrollPane(m_outLog);

        JScrollPane inLogScroll = new JScrollPane(m_inLog);

        JTabbedPane bot = new JTabbedPane();
        bot.setPreferredSize(new Dimension(800 , 200));
        bot.addTab("Messages", msgScroll);
        bot.addTab("Log (out)", outLogScroll);
        bot.addTab("Log (in)", inLogScroll);

        add(bot , BorderLayout.CENTER);
        add(clearBtn , BorderLayout.SOUTH);
    }
}
