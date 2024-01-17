package com.ib.assistant.message;

import com.ib.client.Contract;
import com.ib.client.Decimal;

import java.math.BigDecimal;

public class IbPosition {

    private String accountID ;

    private Contract  contract ;

    private Decimal position ;

    private double averageCost ;

    public IbPosition(String accountID, Contract contract, Decimal position, double averageCost) {
        this.accountID = accountID;
        this.contract = contract;
        this.position = position;
        this.averageCost = averageCost;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Decimal getPosition() {
        return position;
    }

    public void setPosition(Decimal position) {
        this.position = position;
    }

    public double getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(double averageCost) {
        this.averageCost = averageCost;
    }
}
