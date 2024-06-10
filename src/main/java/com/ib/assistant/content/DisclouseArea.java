package com.ib.assistant.content;

import javax.swing.*;
import java.awt.*;

public class DisclouseArea extends JTextArea {

    private final  static String disclouseContent = "免责声明：\n" +
            "本程序由明瑞家族办公室版权所有，仅供明瑞客户参考，不构成任何投资或财务建议，使用者将承担自己的全部使用风险。\n" +
            "\n" +
            "Disclaimer:\n" +
            "1. No Representation or Warranty: \n" +
            "All use of this software program (“the Software\") is for reference only and is subject to change at the discretion of Meritigroup Ltd. (“the Company”) at any time without prior notice. The Company confirms that no express or implied representations or warranties are given as to suitability, reliability, or accuracy of the Software or its use. Nothing in the Software should be relied upon without independent due diligence or financial advice. \n" +
            "2. No Advice or Solicitation:\n" +
            "Nothing in the Software constitutes investment or financial advice. Nothing in the Software constitutes an offer or solicitation by the Company to buy and/or sell any products or services of any kind including, without limitation, the offering of or any financial instrument of any issuer.\n" +
            "3. Copyright: \n" +
            "The Software is the exclusive property of the Company and its affiliates, and it is under the protection of intellectual property laws and rights (including, without limitation, laws protecting copyright). Unauthorized use or distribution of the Software is an infringing act under the applicable laws. The Software may not be used, copied, sold, transmitted, modified, published, stored or otherwise exploited, for any purpose without the Company’s express consent";
    public DisclouseArea() {

        setText( disclouseContent );
        setLineWrap(true);
        setMargin(new Insets(10, 10, 10, 10));
        //System.out.println("Finished init DisclouseArea");
    }
}
