package com.johnsbank.java.services;

import com.johnsbank.java.models.Account;
import com.johnsbank.java.models.Transaction;
import com.johnsbank.java.models.User;
import com.johnsbank.java.repositories.BankRepository;
import com.johnsbank.java.repositories.BankRepositoryImplementation;
import com.johnsbank.java.utilities.MyArrayList;
import com.johnsbank.java.utilities.MyLinkedList;
import com.johnsbank.java.utilities.ResourceNotFoundException;

import java.math.BigDecimal;
import java.sql.Date;

public class BankServiceImplementation implements BankService{

    private static final BankServiceImplementation instance = new BankServiceImplementation();
    private final BankRepository repository;

    private BankServiceImplementation() {repository = BankRepositoryImplementation.getInstance();}
    public static BankServiceImplementation getInstance() {return instance;}

    /**
     * Checks to make sure the username is Unique, if true adds the user, if false returns null
     * @param newUser - the user to add
     * @return - The User that was added
     */
    @Override
    public User addUser(User newUser) {

        if(usernameIsUnique(newUser.getUsername()))
            return repository.addUser(newUser);

        return null;
    }

    /**
     * Returns a user based on the username, if the user does not exist - throws an exception
     * @param username - The username used to locate the user
     * @return - Returns the User found in the database
     * @throws ResourceNotFoundException
     */
    @Override
    public User getUser(String username) throws ResourceNotFoundException {

        User retVal = repository.getUser(username);
        retVal.setAccounts(getAllAccounts(retVal));
        retVal.setTransactions(getAllTransactions(retVal));

        return retVal;
    }

    /**
     * Given changes to a user in the form of a User object, submits those changes to the database
     * @param change - The changes in a User object
     * @return - The User that was changed
     * @throws ResourceNotFoundException
     */
    @Override
    public User updateUser(User change) throws ResourceNotFoundException {
        return repository.updateUser(change);
    }


    /**
     * Given a username, delete the user from the database - if the user is not found throw an exception
     * @param username - The username that identifies the user to delete
     * @return - The User object that was deleted
     * @throws ResourceNotFoundException
     */
    @Override
    public User deleteUser(String username) throws ResourceNotFoundException {
        return repository.deleteUser(username);
    }

    /**
     * Returns a list of all the users in the database
     * @return - A custom Array List of all the Users in the database
     */
    @Override
    public MyArrayList<User> getAllUsers() {

        MyArrayList<User> retVal = repository.getAllUsers();
        for(User user : retVal){
            user.setAccounts(getAllAccounts(user));
            user.setTransactions(getAllTransactions(user));
        }

        return retVal;
    }

    /**
     * Given an account, retrieves all the users that own that account
     * @param account - The account with which to find all the users who own it
     * @return - A custom Array List of all the users who own the account given
     */
    @Override
    public MyArrayList<User> getAllOwners(Account account) {

        MyArrayList<User> retVal = repository.getAllOwners(account);
        for(User user : retVal) {
            user.setAccounts(getAllAccounts(user));
            user.setTransactions(getAllTransactions(user));
        }

        return retVal;
    }

    /**
     * Given an account in the form of an Account Object, add it to the database
     * @param newAccount - the account to add to the database
     * @return - The account that was added to the database
     */
    @Override
    public Account addAccount(Account newAccount) {
        return repository.addAccount(newAccount);
    }

    /**
     * Given an accountID, get the account details
     * @param accountId - The ID associated with Account to retrieve
     * @return - The account retrieved
     * @throws ResourceNotFoundException
     */
    @Override
    public Account getAccount(String accountId) throws ResourceNotFoundException {

        Account account = repository.getAccount(accountId);
        account.setTransactions(repository.getAllTransactions(account));
        account.setOwners(getAllOwners(account));

        return account;
    }

    /**
     * Given a set of changes in the form of an Account object, change the account on the database
     * @param change - A set of changes for the identifying account
     * @return - The account changed
     * @throws ResourceNotFoundException
     */
    @Override
    public Account updateAccount(Account change) throws ResourceNotFoundException {
        return repository.updateAccount(change);
    }

    /**
     * Given an accountID, delete said account from the database
     * @param accountId - The account ID used to identify the account to delete
     * @return - The Account deleted
     * @throws ResourceNotFoundException
     */
    @Override
    public Account deleteAccount(String accountId) throws ResourceNotFoundException {
        return repository.deleteAccount(accountId);
    }

    /**
     * Get an Arraylist of all the accounts
     * @return - A Custom Array list holding all the accounts
     */
    @Override
    public MyArrayList<Account> getAllAccounts() {

        MyArrayList<Account> retVal = repository.getAllAccounts();
        for(Account account: retVal) {
            account.setOwners(getAllOwners(account));
            account.setTransactions(getAllTransactions(account));
        }
        return retVal;
    }

    /**
     * Given a user gets all the accounts the user owns
     * @param user - The user to with which to get all accounts owned
     * @return
     */
    @Override
    public MyArrayList<Account> getAllAccounts(User user) {

        MyArrayList<Account> retVal = repository.getAllAccounts(user);
        for(Account account: retVal) {
            account.setOwners(repository.getAllOwners(account));
            account.setTransactions(repository.getAllTransactions(account));
        }
        return retVal;
    }

    /**
     * Given a new transaction, check to make sure you have the necessary funds,
     * then if they are available add the transaction to the database, and deduct the
     * necessary funds, or if they are not available return null
     * @param newTransaction - The new Transaction in question
     * @return - The transaction made, if one was made
     */
    @Override
    public boolean sendTransaction(Transaction newTransaction) {

        // get the Account the funds are going to
        Account to;
        try {
            to = getAccount(newTransaction.getTo());
        }catch (ResourceNotFoundException e) { // the funds are being transferred externally
            to = null;
        }

        // Get the from account and update the funds
        // if there are necessary funds update the accounts and record the transaction
        Account from;
        try {
            // get the account making the transaction - if it throws an exception, the bank doesn't control it
            from = getAccount(newTransaction.getFrom());
            // test to see if the funds exist And the account does not have a hold
            if(from.isActive() && from.getBalance().compareTo(newTransaction.getAmount()) > -1) {
                // update the funds from the account making the transaction
                from.setBalance(from.getBalance().subtract(newTransaction.getAmount()));
                if (to != null) // if the bank controls the to account, update the funds
                    to.setBalance(to.getBalance().add(newTransaction.getAmount()));
                // add the transaction to the database
                Transaction retVal = repository.addTransaction(newTransaction);
                    if(retVal != null) { // if the transaction worked update accounts
                        repository.updateAccount(from);
                        if(to != null) // if the to account is internal, update account
                            repository.updateAccount(to);
                        return  true;
                    }
            }
        } catch (ResourceNotFoundException e) // The bank does not control the account making the transaction
        {
            return false;
        }

        return false;
    }

    /**
     * Given a Transaction ID retrieve the transaction
     * @param transactionId The transaction ID to look up and retrieve
     * @return - The transaction associated with the given id
     * @throws ResourceNotFoundException
     */
    @Override
    public Transaction getTransaction(String transactionId) throws ResourceNotFoundException {
        return repository.getTransaction(transactionId);
    }

    /**
     * Given a set of changes for a transaction, alters the data in the database
     * @param change - The set of changes in a transaction object
     * @return - The transaction changed
     * @throws ResourceNotFoundException
     */
    @Override
    public Transaction updateTransaction(Transaction change) throws ResourceNotFoundException {
        return repository.updateTransaction(change);
    }

    /**
     * Given transaction id, deletes the associated transaction
     * @param transactionId - The transaction id of the transaction to delete
     * @return - the transaction deleted
     * @throws ResourceNotFoundException
     */
    @Override
    public Transaction deleteTransaction(String transactionId) throws ResourceNotFoundException {
        return repository.deleteTransaction(transactionId);
    }

    /**
     * Retrieves all the transactions in the database
     * @return
     */
    @Override
    public MyArrayList<Transaction> getAllTransactions() {
        return repository.getAllTransactions();
    }

    /**
     * Given a User gets all the transactions owned by that user
     * @param user - the user with which to get all the transactions
     * @return - An ordered linked list of all the transactions
     */
    @Override
    public MyLinkedList<Transaction> getAllTransactions(User user) {
        return repository.getAllTransactions(user);
    }

    /**
     * Given an account, get all the transactions of that account
     * @param account - the account with which to get all its transactions
     * @return - an ordered linked list of all the transactions
     */
    @Override
    public MyLinkedList<Transaction> getAllTransactions(Account account) {
        return repository.getAllTransactions(account);
    }

    /**
     * Given an account, and a user, gives the account a new owner
     * @param account - the account to give an owner
     * @param newOwner - the new owner of the account
     * @return - whether or not the operation succeeds
     * @throws ResourceNotFoundException
     */
    @Override
    public boolean makeOwner(Account account, User newOwner) throws ResourceNotFoundException {

        // First check to make sure the Account is valid
        account = getAccount(account.getAccountID());
        // Second check to make sure the User is valid
        newOwner = getUser(newOwner.getUsername());

        // check to make sure the account doesn't already have the user as an owner
        for(User owner : account.getOwners())
        {
            if(owner.getUsername().equalsIgnoreCase(newOwner.getUsername()))
                return false;
        }

        account.getOwners().add(newOwner);
        repository.addUserAccount(newOwner.getUsername(), account.getAccountID());


        return true;
    }

    /**
     * Given an account and an owner, revoke access to the account by the owner
     * @param account - The account with which to remove ownership of
     * @param owner - the owner revoking ownership
     * @return - True if the operation was successful
     * @throws ResourceNotFoundException
     */
    @Override
    public boolean revokeOwner(Account account, User owner) throws ResourceNotFoundException {

        // First check to make sure the Account is valid
        account = getAccount(account.getAccountID());
        // Second check to make sure the User is valid
        owner = getUser(owner.getUsername());

        // check to make sure the account doesn't already have the user as an owner
        for(int i = 0; i < account.getOwners().getCount(); ++i)
        {
            User elem = account.getOwners().get(i);
            if(elem.getUsername().equalsIgnoreCase(owner.getUsername())) {
                account.getOwners().remove(i);
                repository.deleteUserAccount(owner.getUsername(), account.getAccountID());
                return true;
            }
        }

        // The user did not own the account to begin with
        return false;
    }

    /**
     * Returns true if the username is not found in the database
     * @param username - The given username to check for uniqueness
     * @return - Returns true if the username doesn't exist in the database
     */
    @Override
    public boolean usernameIsUnique(String username) {

        try {
            repository.getUser(username);
        } catch (ResourceNotFoundException e) {
            return true; // the username is unique
        }
        return false; // the username exists
    }

    /**
     * Makes a deposit into an account given an Account
     * @param account - The account to put the deposit into
     * @param amount - The amount being deposited
     * @return - Returns true if the operation was successful
     */
    @Override
    public boolean makeDeposit(Account account, BigDecimal amount) {

        try {
            account = getAccount(account.getAccountID());
            account.setBalance(account.getBalance().add(amount));
            repository.addTransaction(
                    new Transaction(new Date(System.currentTimeMillis()), amount, null, account.getAccountID()));
            repository.updateAccount(account);
        } catch (ResourceNotFoundException e) {
            return false; // the account does not exist
        }

        return true;
    }

    /**
     * Given an account and amount, make a withdrawal if there is enough money
     * @param account The account to withrawal from
     * @param amount The amount being withdrawn
     * @return - Returns true if the operation was successful
     */
    @Override
    public boolean makeWithdrawal(Account account, BigDecimal amount) {

        try {
            account = getAccount(account.getAccountID());
            if(account.getBalance().compareTo(amount) > -1) { // if there are enough funds
                account.setBalance(account.getBalance().subtract(amount));
                repository.addTransaction(
                        new Transaction(new Date(System.currentTimeMillis()), amount, account.getAccountID(), null));
                return repository.updateAccount(account) != null;
            }
        } catch (ResourceNotFoundException e) {
            return false; // the account does not exist
        }

        return false;
    }

    /**
     * Given an account put a hold on it
     * @param account - The account to put a hold on
     * @return - returns true if the operation was successful
     */
    @Override
    public boolean holdAccount(Account account) {
        try {
            // Make sure the account exists
            account = getAccount(account.getAccountID());
            if(account.isActive()) {
                account.setActive(false);
                repository.updateAccount(account);
                return true; // successfully put a hold on the account
            }
        } catch (ResourceNotFoundException e) {
            return false; // the account doesn't exist
        }

        return false; // The account already has a hold on it
    }

    /**
     * Given an account wiht a hold on it, remove the hold
     * @param account - The account to remove the hold
     * @return - true if the operation was successful
     */
    @Override
    public boolean removeHold(Account account) {
        try {
            // Make sure the account exists
            account = getAccount(account.getAccountID());
            if(!account.isActive()) {
                account.setActive(true);
                repository.updateAccount(account);
                return true; // successfully took a hold off the account
            }
        } catch (ResourceNotFoundException e) {
            return false; // the account doesn't exist
        }

        return false; // The doesn't have a hold on it
    }

    /**
     * Given a username and password hash, log in the user
     * @param username - The username of the user to log in
     * @param passHash - The password hash of the user to log in
     * @return - Returns the User object of the user if successful, otherwise null
     */
    @Override
    public User logUserIn(String username, String passHash) {

        User retVal = null;
        try {
            retVal = getUser(username);
        } catch (ResourceNotFoundException e) {
            return null; // The username doesn't exist
        }
        if(retVal.getPass_Hash().equalsIgnoreCase(passHash))
            return retVal;

        return null; // the password was incorrect
    }

    /**
     * Returns if there is a connection to the data storage
     * @return - true if the connection is established
     */
    @Override
    public boolean isConnected() {
        return repository.isConnected();
    }
}
