package com.johnsbank.java.models;

import com.johnsbank.java.utilities.HashGenerator;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

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

    @Override
    public String toString() {
        StringBuilder retVal = new StringBuilder(); // The Transaction that can be printed to screen
        char[] repeat = new char[64];               // an array of spaces used for padding
        Arrays.fill(repeat, ' ');
        String spaces = new String(repeat);         // String of spaces used for padding
        int countLines = 0;

        retVal.append("|****  Date: ");
        retVal.append(date.toString());
        retVal.append("                                  Type: ");

        if(to == null)
            retVal.append("Withdrawal");
        else if(from == null)
            retVal.append("Deposit");
        else
            retVal.append("Transfer");
        retVal.append(spaces.substring(0, 75-retVal.length()));
        retVal.append("****|\n|****  Amount: ");
        retVal.append(NumberFormat.getCurrencyInstance(Locale.US).format(amount));
        retVal.append(spaces.substring(0, 75-(retVal.length()-81*++countLines)));
        retVal.append("****|\n");
        if(from != null){
            retVal.append("|****  From Account:");
            retVal.append(spaces.substring(0, 75-(retVal.length()-81*++countLines)));
            retVal.append("****|\n|****  ");
            retVal.append(from);
            retVal.append(spaces.substring(0, 75-(retVal.length()-81*++countLines)));
            retVal.append("****|\n");

        }
        if(to != null){
            retVal.append("|****  To Account:");
            retVal.append(spaces.substring(0, 75-(retVal.length()-81*++countLines)));
            retVal.append("****|\n|****  ");
            retVal.append(to);
            retVal.append(spaces.substring(0, 75-(retVal.length()-81*++countLines)));
            retVal.append("****|\n");
        }

        retVal.append("|==============================================================================|");

        return retVal.toString();
    }

}
