package com.johnsbank.java.repositories;

import com.johnsbank.java.accountdata.Account;
import com.johnsbank.java.accountdata.Transaction;
import com.johnsbank.java.accountdata.User;
import com.johnsbank.java.utilities.MyArrayList;
import com.johnsbank.java.utilities.ResourceNotFoundException;

public interface BankRepository {

    public User addUser(User newUser) ;
    public User getUser(String username) throws ResourceNotFoundException;
    public User updateUser(User change) throws ResourceNotFoundException;
    public User deleteUser(String username) throws ResourceNotFoundException;
    public MyArrayList<User> getAllUsers();
    public MyArrayList<User> getAllOwners(Account account);

    public Account addAccount(Account newAccount);
    public Account getAccount(String accountId) throws ResourceNotFoundException;
    public Account updateAccount(Account change) throws ResourceNotFoundException;
    public Account deleteAccount(String accountId) throws ResourceNotFoundException;
    public MyArrayList<Account> getAllAccounts();
    public MyArrayList<Account> getAllAccounts(User user);
    public Account getFromAccount(String transactionId) throws ResourceNotFoundException;
    public Account getFromAccount(Transaction transaction) throws ResourceNotFoundException;
    public Account getToAccount(String transactionId) throws ResourceNotFoundException;
    public Account getToAccount(Transaction transaction) throws ResourceNotFoundException;


    public Transaction addTransaction(Transaction newTransaction);
    public Transaction getTransaction(String transactionId) throws ResourceNotFoundException;
    public Transaction updateTransaction(Transaction change) throws ResourceNotFoundException;
    public Transaction deleteTransaction(String transactionId) throws ResourceNotFoundException;
    public MyArrayList<Transaction> getAllTransactions();
    public MyArrayList<Transaction> getAllTransactions(User user);
    public MyArrayList<Transaction> getAllTransactions(Account account);
}
