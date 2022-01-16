package com.johnsbank.java.models;

import com.johnsbank.java.utilities.MyArrayList;
import com.johnsbank.java.utilities.MyLinkedList;

import java.math.BigDecimal;

public class Account {

    public enum AccountType {
        SAVINGS, CHECKING, MMA, CD, IRA, BROKERAGE
    }

    private String accountID;
    private AccountType type;
    private MyArrayList<User> owners;
    private MyLinkedList<Transaction> transactions;
    private BigDecimal balance;
    private boolean active;


    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public MyArrayList<User> getOwners() {
        return owners;
    }

    public void setOwners(MyArrayList<User> owners) {
        this.owners = owners;
    }

    public MyLinkedList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(MyLinkedList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
