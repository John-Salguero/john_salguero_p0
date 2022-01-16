package com.johnsbank.java.services;

import com.johnsbank.java.models.Account;
import com.johnsbank.java.models.Transaction;
import com.johnsbank.java.models.User;
import com.johnsbank.java.utilities.MyArrayList;
import com.johnsbank.java.utilities.MyLinkedList;
import com.johnsbank.java.utilities.ResourceNotFoundException;

import java.math.BigDecimal;

public interface BankService {

    // Trivial Service Methods
    public User addUser(User newUser) ;
    public User getUser(String username) throws ResourceNotFoundException;
    public User updateUser(User change) throws ResourceNotFoundException;
    public User deleteUser(String username) throws ResourceNotFoundException;
    public MyArrayList<User> getAllUsers();
    public MyArrayList<User> getAllOwners(Account account);

    // Trivial Operations on Accounts
    public Account addAccount(Account newAccount);
    public Account getAccount(String accountId) throws ResourceNotFoundException;
    public Account updateAccount(Account change) throws ResourceNotFoundException;
    public Account deleteAccount(String accountId) throws ResourceNotFoundException;
    public MyArrayList<Account> getAllAccounts();
    public MyArrayList<Account> getAllAccounts(User user);

    // Trivial operations on Transactions
    public Transaction getTransaction(String transactionId) throws ResourceNotFoundException;
    public Transaction updateTransaction(Transaction change) throws ResourceNotFoundException;
    public Transaction deleteTransaction(String transactionId) throws ResourceNotFoundException;
    public MyArrayList<Transaction> getAllTransactions();
    public MyLinkedList<Transaction> getAllTransactions(User user);
    public MyLinkedList<Transaction> getAllTransactions(Account account);

    // Business Logic operations
    public boolean sendTransaction(Transaction newTransaction);
    public boolean makeOwner(Account account, User owner) throws ResourceNotFoundException;
    public boolean revokeOwner(Account account, User owner) throws ResourceNotFoundException;
    public boolean usernameIsUnique(String username);
    public boolean makeDeposit(Account account, BigDecimal amount);
    public boolean makeWithdrawal(Account account, BigDecimal amount);
    public boolean holdAccount(Account account);
    public boolean removeHold(Account account);
    public User logUserIn(String username, String passHash);

    public boolean isConnected();
}
