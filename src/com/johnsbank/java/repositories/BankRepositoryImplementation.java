package com.johnsbank.java.repositories;

import com.johnsbank.java.accountdata.Account;
import com.johnsbank.java.accountdata.Transaction;
import com.johnsbank.java.accountdata.User;
import com.johnsbank.java.utilities.JDBCConnection;
import com.johnsbank.java.utilities.MyArrayList;
import com.johnsbank.java.utilities.ResourceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Repository Layer of our application - handles all communication directly to and from the database
 */
public class BankRepositoryImplementation implements BankRepository{

    // The Connection we will use for our CRUD operations to the Database
    Connection connection = JDBCConnection.getInstance().getConnection();

    /**
     * Adds a user to the database
     * @param newUser - The user to add
     * @return - if successful returns the user added, otherwise null
     */
    @Override
    public User addUser(User newUser) {

        String sql = "INSERT INTO \"User_Data\" VALUES (?,?,?,?,?,?,?,?,?,?,?) RETURNING *";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            // Set Values
            ps.setString(1, newUser.username);
            ps.setBigDecimal(2, m.getPrice());
            ps.setBoolean(3, m.isAvailable());
            ps.setLong(4, m.getReturnDate());

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return buildMovie(rs);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

        return null;
    }

    @Override
    public User getUser(String username) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public User updateUser(User change) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public User deleteUser(String username) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public MyArrayList<User> getAllUsers() {
        return null;
    }

    @Override
    public MyArrayList<User> getAllOwners(Account account) {
        return null;
    }

    @Override
    public Account addAccount(Account newAccount) {
        return null;
    }

    @Override
    public Account getAccount(String accountId) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public Account updateAccount(Account change) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public Account deleteAccount(String accountId) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public MyArrayList<Account> getAllAccounts() {
        return null;
    }

    @Override
    public MyArrayList<Account> getAllAccounts(User user) {
        return null;
    }

    @Override
    public Account getFromAccount(String transactionId) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public Account getFromAccount(Transaction transaction) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public Account getToAccount(String transactionId) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public Account getToAccount(Transaction transaction) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public Transaction addTransaction(Transaction newTransaction) {
        return null;
    }

    @Override
    public Transaction getTransaction(String transactionId) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public Transaction updateTransaction(Transaction change) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public Transaction deleteTransaction(String transactionId) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public MyArrayList<Transaction> getAllTransactions() {
        return null;
    }

    @Override
    public MyArrayList<Transaction> getAllTransactions(User user) {
        return null;
    }

    @Override
    public MyArrayList<Transaction> getAllTransactions(Account account) {
        return null;
    }
}
