package com.johnsbank.java.models;

import com.johnsbank.java.utilities.HashGenerator;

import java.math.BigDecimal;
import java.sql.Date;

public class Transaction {
    private String transactionID;
    private Date date;
    private BigDecimal amount;
    private String from;
    private String to;

    public Transaction() {}

    public Transaction(Date date, BigDecimal amount, String from, String to) {
        String ID = System.currentTimeMillis() + amount.toString();
        if(from != null)
            ID += from;
        if(to != null);
            ID += to;
        transactionID = HashGenerator.getInstance().getMessageDigest(ID);
        this.amount = amount;
        this.date = date;
        this.from = from;
        this.to = to;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
