package com.ib.assistant;

import javax.swing.*;

public class IbDashboard {

    public static void main(String[] args) {

        DashBoardFrame dashBoard = new DashBoardFrame();
        //dashBoard.setIconImage(new I);

        ImageIcon imageIcon = new ImageIcon(IbDashboard.class.getClassLoader().getResource("Ib_logo.png"));
        dashBoard.setIconImage(imageIcon.getImage());
        dashBoard.initComponent();
        dashBoard.setResizable(false);
        dashBoard.setLocationRelativeTo(null);
        dashBoard.setVisible(true);


    }


}
