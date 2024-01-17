package com.ib.assistant.configuration;

import java.math.BigDecimal;

public class TQQQConfiguration {


    /**
     * refPriceField.setText(  prop.getProperty("tqqq.cfg.reference.price"));
     * triggerRateField.setText( prop.getProperty("tqqq.cfg.trigger.rate"));
     * planAmtField.setText( prop.getProperty("tqqq.cfg.plan.amount"));
     * fundingRateField.setText( prop.getProperty("tqqq.cfg.funding.rate"));
     * additionRateField.setText( prop.getProperty( "tqqq.cfg.addition.rate"));
     * debtLimitField.setText( prop.getProperty( "tqqq.cfg.debt.limit" ));
     */
    public static final String KEY_PROP_TQQQ_REF_PRICE = "tqqq.cfg.reference.price";
    public static final String KEY_PROP_TQQQ_TRIGGER_RATE = "tqqq.cfg.trigger.rate";
    public static final String KEY_PROP_TQQQ_PLAN_AMT = "tqqq.cfg.plan.amount";
    public static final String KEY_PROP_TQQQ_FUND_RATE = "tqqq.cfg.funding.rate";
    public static final String KEY_PROP_TQQQ_ADDITION_RATE = "tqqq.cfg.addition.rate";
    public static final String KEY_PROP_TQQQ_DEBT_LIMIT = "tqqq.cfg.debt.limit";

    public static final String KEY_PROP_TQQQ_CLIENT_ID="tqqq.cfg.clientId";



    private  int  clientId;
    private double referencePrice;   //   ref  price
    private double triggerRate; //  trigger rate

    private  double upTriggerPrice;  //  up trigger price

    private  double  downTriggerPrice ; // down  trigger price


    private double planAmount;  // plan  amont

    private double fundRate; // funding rate

    private double addRate;  // addition rate

    private double debtLimit;   // debtLimit

    public TQQQConfiguration(int clientId ,double referencePrice, double triggerRate, double planAmount, double fundRate, double addRate, double debtLimit) {
        this.referencePrice = referencePrice;
        this.triggerRate = triggerRate;
        this.planAmount = planAmount;
        this.fundRate = fundRate;
        this.addRate = addRate;
        this.debtLimit = debtLimit;
        this.clientId = clientId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public double getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(double referencePrice) {
        this.referencePrice = referencePrice;
    }

    public double getTriggerRate() {
        return triggerRate;
    }

    public void setTriggerRate(double triggerRate) {
        this.triggerRate = triggerRate;
    }

    public double getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(double planAmount) {
        this.planAmount = planAmount;
    }

    public double getFundRate() {
        return fundRate;
    }

    public void setFundRate(double fundRate) {
        this.fundRate = fundRate;
    }

    public double getAddRate() {
        return addRate;
    }

    public void setAddRate(double addRate) {
        this.addRate = addRate;
    }

    public double getDebtLimit() {
        return debtLimit;
    }

    public void setDebtLimit(double debtLimit) {
        this.debtLimit = debtLimit;
    }

    public double getUpTriggerPrice() {
        upTriggerPrice = referencePrice * ( 1 + triggerRate );
        BigDecimal  price = new BigDecimal( upTriggerPrice );
        return  price.setScale(2 , BigDecimal.ROUND_UP).doubleValue();
    }

    public double getDownTriggerPrice() {
        downTriggerPrice =referencePrice*( 1 - triggerRate );
        BigDecimal  price = new BigDecimal( downTriggerPrice );
        return  price.setScale(2 , BigDecimal.ROUND_UP).doubleValue();
    }
}
