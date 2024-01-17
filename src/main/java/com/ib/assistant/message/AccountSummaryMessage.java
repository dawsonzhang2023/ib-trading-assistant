package com.ib.assistant.message;

import com.ib.controller.AccountSummaryTag;

public class AccountSummaryMessage {
    private  int  requestID;

    private String accountName ;

    private AccountSummaryTag  tag;

    private String accountValue ;

    private String  currency ;

    public AccountSummaryMessage(int requestID, String accountName, AccountSummaryTag tag, String accountValue, String currency) {
        this.requestID = requestID;
        this.accountName = accountName;
        this.tag = tag;
        this.accountValue = accountValue;
        this.currency = currency;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public AccountSummaryTag getTag() {
        return tag;
    }

    public void setTag(AccountSummaryTag tag) {
        this.tag = tag;
    }

    public String getAccountValue() {
        return accountValue;
    }

    public void setAccountValue(String accountValue) {
        this.accountValue = accountValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "AccountSummaryMessage{" +
                "requestID=" + requestID +
                ", accountName='" + accountName + '\'' +
                ", tag=" + tag +
                ", accountValue='" + accountValue + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
