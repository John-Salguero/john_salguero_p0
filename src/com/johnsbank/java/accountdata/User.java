package com.johnsbank.java.accountdata;

import com.johnsbank.java.utilities.MyArrayList;
import com.johnsbank.java.utilities.MyLinkedList;

public class User {

    public User() {};

    public User(String username, String firstName, String lastName, MyArrayList<Account> accounts, MyLinkedList<Transaction> transactions) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = accounts;
        this.transactions = transactions;
    }

    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getSSN_Hash() {
        return SSN_Hash;
    }

    public void setSSN_Hash(String SSN_Hash) {
        this.SSN_Hash = SSN_Hash;
    }

    public String getPass_Hash() {
        return pass_Hash;
    }

    public void setPass_Hash(String pass_Hash) {
        this.pass_Hash = pass_Hash;
    }

    public MyArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(MyArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public MyLinkedList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(MyLinkedList<Transaction> transactions) {
        this.transactions = transactions;
    }

    private String email;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String zipCode;
    private String SSN_Hash;
    private String pass_Hash;

    private MyArrayList<Account> accounts;
    private MyLinkedList<Transaction> transactions;
}
