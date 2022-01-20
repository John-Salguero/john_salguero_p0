package com.johnsbank.java.repositories;

import com.johnsbank.java.models.Account;
import com.johnsbank.java.models.Transaction;
import com.johnsbank.java.models.User;
import com.johnsbank.java.utilities.MyArrayList;
import com.johnsbank.java.utilities.MyLinkedList;
import com.johnsbank.java.utilities.ResourceNotFoundException;

public interface BankRepository {

    // CRUD Operations on Users
    public User addUser(User newUser) ;
    public User getUser(String username) throws ResourceNotFoundException;
    public User updateUser(User change) throws ResourceNotFoundException;
    public User deleteUser(String username) throws ResourceNotFoundException;
    public MyArrayList<User> getAllUsers();
    public MyArrayList<User> getAllOwners(Account account) throws ResourceNotFoundException;

    // CRUD Operations on Accounts
    public Account addAccount(Account newAccount);
    public Account getAccount(String accountId) throws ResourceNotFoundException;
    public Account updateAccount(Account change) throws ResourceNotFoundException;
    public Account deleteAccount(String accountId) throws ResourceNotFoundException;
    public MyArrayList<Account> getAllAccounts();
    public MyArrayList<Account> getAllAccounts(User user) throws ResourceNotFoundException;

    // CRUD operations on Transactions
    public Transaction addTransaction(Transaction newTransaction);
    public Transaction getTransaction(String transactionId) throws ResourceNotFoundException;
    public Transaction updateTransaction(Transaction change) throws ResourceNotFoundException;
    public Transaction deleteTransaction(String transactionId) throws ResourceNotFoundException;
    public MyArrayList<Transaction> getAllTransactions();
    public MyLinkedList<Transaction> getAllTransactions(User user) throws ResourceNotFoundException;
    public MyLinkedList<Transaction> getAllTransactions(Account account) throws ResourceNotFoundException;

    // CRUD operations on User Accounts
    public boolean addUserAccount(String username, String accountId);
    public boolean deleteUserAccount(String username, String accountId) throws ResourceNotFoundException;

    public boolean isConnected();
}
