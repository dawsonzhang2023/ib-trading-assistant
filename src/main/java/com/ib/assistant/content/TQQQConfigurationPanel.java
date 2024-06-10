package com.ib.assistant.content;

import com.ib.assistant.configuration.TQQQConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Properties;

public class TQQQConfigurationPanel extends Panel {

    //private  String
    private Boolean isLoad = false;
    private JTextField refPriceField = null;

    private JTextField triggerRateField = null;

    private JTextField planAmtField = null;

    private JTextField fundingRateField = null;

    private JTextField additionRateField = null;

    private JTextField debtLimitField = null;

    private final String TQQQ_CONFIG_FILE_PATH = "tqqq.cfg.properties";

    private JButton saveButton;

    private JTextField clientIdField;

    public TQQQConfiguration getConfiguration() {
        String clientIdValue = clientIdField.getText();
        int cid = 0;
        if (clientIdValue != null && !"".equals(clientIdValue)) cid = Integer.parseInt(clientIdValue);

        return new TQQQConfiguration(
                cid,
                Double.parseDouble(refPriceField.getText()),
                Double.parseDouble(triggerRateField.getText()),
                Double.parseDouble(planAmtField.getText()),
                Double.parseDouble(fundingRateField.getText()),
                Double.parseDouble(additionRateField.getText()),
                Double.parseDouble(debtLimitField.getText())
        );
    }

    public void loadConfiguration() throws IOException {
        File configFile = new File(TQQQ_CONFIG_FILE_PATH);
        if (configFile.exists() && configFile.isFile()) {
            Properties prop = new Properties();
            FileReader reader = new FileReader(configFile);
            prop.load(reader);
            if (!prop.isEmpty()) {

                String clientIdValue = prop.getProperty(TQQQConfiguration.KEY_PROP_TQQQ_CLIENT_ID);
                clientIdValue = (clientIdValue == null || "".equals(clientIdValue)) ? "0" : clientIdValue;

                refPriceField.setText(prop.getProperty(TQQQConfiguration.KEY_PROP_TQQQ_REF_PRICE));
                triggerRateField.setText(prop.getProperty(TQQQConfiguration.KEY_PROP_TQQQ_TRIGGER_RATE));
                planAmtField.setText(prop.getProperty(TQQQConfiguration.KEY_PROP_TQQQ_PLAN_AMT));
                fundingRateField.setText(prop.getProperty(TQQQConfiguration.KEY_PROP_TQQQ_FUND_RATE));
                additionRateField.setText(prop.getProperty(TQQQConfiguration.KEY_PROP_TQQQ_ADDITION_RATE));
                debtLimitField.setText(prop.getProperty(TQQQConfiguration.KEY_PROP_TQQQ_DEBT_LIMIT));
                clientIdField.setText(clientIdValue);
                isLoad = true;
            }
        }
    }

    private void saveConfiguration() throws IOException {
        //  System.out.println("start save");
        File configFile = new File(TQQQ_CONFIG_FILE_PATH);

        //System.out.println("Start update File :" + TQQQ_CONFIG_FILE_PATH);
        Properties prop = new Properties();
        prop.setProperty(TQQQConfiguration.KEY_PROP_TQQQ_REF_PRICE, refPriceField.getText());
        prop.setProperty(TQQQConfiguration.KEY_PROP_TQQQ_PLAN_AMT, planAmtField.getText());
        prop.setProperty(TQQQConfiguration.KEY_PROP_TQQQ_ADDITION_RATE, additionRateField.getText());
        prop.setProperty(TQQQConfiguration.KEY_PROP_TQQQ_TRIGGER_RATE, triggerRateField.getText());
        prop.setProperty(TQQQConfiguration.KEY_PROP_TQQQ_FUND_RATE, fundingRateField.getText());
        prop.setProperty(TQQQConfiguration.KEY_PROP_TQQQ_DEBT_LIMIT, debtLimitField.getText());
        prop.setProperty(TQQQConfiguration.KEY_PROP_TQQQ_CLIENT_ID, clientIdField.getText());
        FileWriter writer = new FileWriter(configFile);
        prop.store(writer, "save");

    }


    public Boolean getLoad() {
        return isLoad;
    }

    public void setClientIdField(JTextField clientIdField) {
        this.clientIdField = clientIdField;
    }

    public TQQQConfigurationPanel() {

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2));
        //inputPanel.setPreferredSize(new Dimension(1600, 200));

        JLabel label1 = new JLabel("当前价格 Market Price");
        JLabel label2 = new JLabel("触发比例 Trigger Ratio");
        JLabel label3 = new JLabel("规划金额 Planned Amount");
        JLabel label4 = new JLabel("基金占比 ETF Ratio");
        JLabel label5 = new JLabel("加仓比例 Increase Ratio");
        JLabel label6 = new JLabel("负债上限 Debt Ceiling");

        label1.setBackground(Color.white);
        label2.setBackground(Color.white);
        label3.setBackground(Color.white);
        label4.setBackground(Color.white);
        label5.setBackground(Color.white);
        label6.setBackground(Color.white);

        //  config  ref  price
        refPriceField = new JTextField(20);
        triggerRateField = new JTextField(20);
        planAmtField = new JTextField(20);
        fundingRateField = new JTextField(20);
        additionRateField = new JTextField(20);
        debtLimitField = new JTextField(20);

        inputPanel.add(label1);
        inputPanel.add(refPriceField);
        inputPanel.add(label2);
        inputPanel.add(triggerRateField);
        inputPanel.add(label3);
        inputPanel.add(planAmtField);
        inputPanel.add(label4);
        inputPanel.add(fundingRateField);
        inputPanel.add(label5);
        inputPanel.add(additionRateField);
        inputPanel.add(label6);
        inputPanel.add(debtLimitField);

        JPanel panelCenter = new JPanel();
        saveButton = new JButton("保存配置文件 Save Configuration");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveConfiguration();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        panelCenter.add(saveButton);
        add(inputPanel, BorderLayout.WEST);
        panelCenter.setPreferredSize(new Dimension(800, 40));
        add(panelCenter, BorderLayout.CENTER);

        JPanel panel2 = new JPanel();
        //panel2.setPreferredSize(new Dimension(1600 , 300));
        DisclouseArea dArea = new DisclouseArea();
        dArea.setPreferredSize(new Dimension(780, 300));
        panel2.add(dArea);
        add(panel2, BorderLayout.SOUTH);

    }

    public void updateConfig(TQQQConfiguration currentConfig) throws IOException {
        refPriceField.setText(String.valueOf(currentConfig.getReferencePrice()));
        saveConfiguration();
    }
}
