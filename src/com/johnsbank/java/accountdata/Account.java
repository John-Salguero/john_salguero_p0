package com.johnsbank.java.accountdata;

import com.johnsbank.java.utilities.MyArrayList;
import com.johnsbank.java.utilities.MyLinkedList;

import java.math.BigDecimal;

public class Account {

    public enum AccountType {
        SAVINGS, CHECKING, MMA, CD, IRA, BROKERAGE
    }

    public Account() {
        owners = new MyArrayList<>();
        transactions = new MyLinkedList<>();
    }

    public String accountID;
    public AccountType type;
    public MyArrayList<User> owners;
    public MyLinkedList<Transaction> transactions;
    public BigDecimal balance;
    public boolean active;
}
