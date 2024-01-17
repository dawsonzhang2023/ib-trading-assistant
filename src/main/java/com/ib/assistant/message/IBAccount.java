package com.ib.assistant.message;

public class IBAccount {

    private String accountID ;

    private double netLiquidation;

    private double totalCash;

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public double getNetLiquidation() {
        return netLiquidation;
    }

    public void setNetLiquidation(double netLiquidation) {
        this.netLiquidation = netLiquidation;
    }

    public double getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(double totalCash) {
        this.totalCash = totalCash;
    }
}
