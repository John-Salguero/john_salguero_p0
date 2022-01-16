package com.johnsbank.java.repositories;

import com.johnsbank.java.models.Account;
import com.johnsbank.java.models.Transaction;
import com.johnsbank.java.models.User;
import com.johnsbank.java.utilities.JDBCConnection;
import com.johnsbank.java.utilities.MyArrayList;
import com.johnsbank.java.utilities.MyLinkedList;
import com.johnsbank.java.utilities.ResourceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Repository Layer of our application - handles all communication directly to and from the database
 */
public class BankRepositoryImplementation implements BankRepository{

    private static final BankRepositoryImplementation instance = new BankRepositoryImplementation();

    // The Connection we will use for our Communication to the Database
    Connection connection = JDBCConnection.getInstance().getConnection();

    private BankRepositoryImplementation() {/* Prevents anyone from instantiating */}

    /* Adheres to the Singleton Pattern */
    public static BankRepositoryImplementation getInstance() {return instance;}

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
            ps.setString(1, newUser.getUsername());
            ps.setString(2, newUser.getFirstName());
            ps.setString(3, newUser.getLastName());
            ps.setString(4, newUser.getPhoneNumber());
            ps.setString(5, newUser.getEmail());
            ps.setString(6, newUser.getAddressLine1());
            ps.setString(7, newUser.getAddressLine2());
            ps.setString(8, newUser.getState());
            ps.setString(9, newUser.getZipCode());
            ps.setString(10, newUser.getSSN_Hash());
            ps.setString(11, newUser.getPass_Hash());

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return buildUser(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not add a User to the Database!", e);
        }

        return null;
    }

    /**
     * Given a username retrieves the user from the database
     * @param username - the username used to locate the user
     * @return - The user associated with the username
     * @throws ResourceNotFoundException
     */
    @Override
    public User getUser(String username) throws ResourceNotFoundException {

        String query = "SELECT * FROM \"User_Data\" WHERE \"Username\" = ?";

        try {
            // Set up PreparedStatement
            PreparedStatement ps = connection.prepareStatement(query);
            // Set Values for any Placeholders
            ps.setString(1, username);
            // Execute the statement
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return buildUser(rs);
            }
        } catch (SQLException e)
        {
            throw new RuntimeException("Could not get user from Database!",e);
        }

        throw new ResourceNotFoundException("Could not Locate User in Database!");
    }

    /**
     * Given a user, adds those changes to the Database
     * @param change - the user with the changes needed
     * @return The updated User
     * @throws ResourceNotFoundException
     */
    @Override
    public User updateUser(User change) throws ResourceNotFoundException {

        String sql = "UPDATE \"User_Data\" set \"First_Name\"=?, \"Last_Name\"=?, \"Phone_Number\"=?," +
                " \"Email\"=?, \"Address_Line_1\"=?, \"Address_Line_2\"=?, \"State\"=?, \"Zip_Code\"=?, " +
                "\"SSN_Hash\"=?, \"Pass_Hash\"=? WHERE \"Username\" = ? RETURNING *";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);

            // Set Values
            ps.setString(1, change.getFirstName());
            ps.setString(2, change.getLastName());
            ps.setString(3, change.getPhoneNumber());
            ps.setString(4, change.getEmail());
            ps.setString(5, change.getAddressLine1());
            ps.setString(6, change.getAddressLine2());
            ps.setString(7, change.getState());
            ps.setString(8, change.getZipCode());
            ps.setString(9, change.getSSN_Hash());
            ps.setString(10, change.getPass_Hash());
            ps.setString(11, change.getUsername());

            ResultSet rs = ps.executeQuery();

            if(rs.next())
                return buildUser(rs);

        } catch (SQLException e) {
            throw new RuntimeException("Could not update user from Database!",e);
        }

        throw new ResourceNotFoundException("The user could not be found in the database!");
    }

    /**
     * Given a username, deletes that user in the database
     * @param username - The username used to locate and delete the user
     * @return - The user deleted
     * @throws ResourceNotFoundException
     */
    @Override
    public User deleteUser(String username) throws ResourceNotFoundException {

        String sql = "DELETE FROM \"User_Data\" WHERE \"Username\" = ? RETURNING *";

        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
                return buildUser(rs);

        }catch (SQLException e) {
            throw new RuntimeException("Could not delete user!", e);
        }

        throw new ResourceNotFoundException("User Not Found in Database!");
    }

    /**
     * Returns a custom ArrayList of all Users
     * @return - The custom ArrayList of all users
     */
    @Override
    public MyArrayList<User> getAllUsers() {

        String query = "SELECT * FROM \"User_Data\"";
        MyArrayList<User> users = new MyArrayList<>();

        try {
            // Set up PreparedStatement
            PreparedStatement ps = connection.prepareStatement(query);
            // Execute the statement
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                users.add(buildUser(rs));
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Given an account, returns a custom ArrayList of all its owners
     * @param account - The account to get the owners from
     * @return - A custom ArrayList of all the owners
     */
    @Override
    public MyArrayList<User> getAllOwners(Account account) {
        String query = "SELECT * From \"User_Data\" WHERE \"Username\" IN\n" +
                "  (SELECT \"Username\" FROM \"User_Accounts\" WHERE \"Account_ID\"=?);";
        MyArrayList<User> users = new MyArrayList<>();

        try {
            // Set up PreparedStatement
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, account.getAccountID());
            // Execute the statement
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                users.add(buildUser(rs));
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Given a new account, adds an account to the database
     * @param newAccount - The new account to add to the database
     * @return - Returns the Account added to the database
     */
    @Override
    public Account addAccount(Account newAccount) {

        String sql = "INSERT INTO \"Account_Data\" VALUES (?,?,?,?) RETURNING *";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            // Set Values
            ps.setString(1, newAccount.getAccountID());
            ps.setBigDecimal(2, newAccount.getBalance());
            ps.setString(3, newAccount.getType().toString());
            ps.setBoolean(4, newAccount.isActive());

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return buildAccount(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not add an Account to the Database!", e);
        }

        return null;
    }

    /**
     * Given an accountId, returns the Account found
     * @param accountId - The account ID of the account to find
     * @return - Returns the Account found associated with the account ID
     * @throws ResourceNotFoundException
     */
    @Override
    public Account getAccount(String accountId) throws ResourceNotFoundException {
        String query = "SELECT * FROM \"Account_Data\" WHERE \"Account_ID\" = ?";

        try {
            // Set up PreparedStatement
            PreparedStatement ps = connection.prepareStatement(query);
            // Set Values for any Placeholders
            ps.setString(1, accountId);
            // Execute the statement
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return buildAccount(rs);
            }
        } catch (SQLException e)
        {
            throw new RuntimeException("Could not get account from Database!",e);
        }

        throw new ResourceNotFoundException("Could not Locate Account in Database!");
    }

    /**
     * Given an Account to change, changes the account in the database
     * @param change - the account holding all the changes needed
     * @return - Returns the account changed
     * @throws ResourceNotFoundException
     */
    @Override
    public Account updateAccount(Account change) throws ResourceNotFoundException {
        String sql = "UPDATE \"Account_Data\" set \"Balance\"=?, \"Type\"=?, \"Active\"=? " +
                "WHERE \"Account_ID\" = ? RETURNING *";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);

            // Set Values
            ps.setBigDecimal(1, change.getBalance());
            ps.setString(2, change.getType().toString());
            ps.setBoolean(3, change.isActive());
            ps.setString(4, change.getAccountID());

            ResultSet rs = ps.executeQuery();

            if(rs.next())
                return buildAccount(rs);

        } catch (SQLException e) {
            throw new RuntimeException("Could not update account from Database!",e);
        }

        throw new ResourceNotFoundException("The account could not be found in the database!");
    }

    /**
     * Given an Account ID, deletes the account associated with that ID from the database
     * @param accountId - The account ID used to find the account to delete
     * @return - The account deleted
     * @throws ResourceNotFoundException
     */
    @Override
    public Account deleteAccount(String accountId) throws ResourceNotFoundException {
        String sql = "DELETE FROM \"Account_Data\" WHERE \"Account_ID\" = ? RETURNING *";

        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, accountId);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
                return buildAccount(rs);

        }catch (SQLException e) {
            throw new RuntimeException("Could not delete Account!", e);
        }

        throw new ResourceNotFoundException("Account Not Found in Database!");
    }

    /**
     * Returns a custom ArrayList of All the Accounts
     * @return - A custom array list of all accounts
     */
    @Override
    public MyArrayList<Account> getAllAccounts() {
        String query = "SELECT * FROM \"Account_Data\"";
        MyArrayList<Account> accounts = new MyArrayList<>();

        try {
            // Set up PreparedStatement
            PreparedStatement ps = connection.prepareStatement(query);
            // Execute the statement
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                accounts.add(buildAccount(rs));
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return accounts;
    }

    /**
     * Given a user, return all the accounts owned by that user
     * @param user - The user whose accounts we are getting
     * @return - An array list of all the accounts owned by said user
     */
    @Override
    public MyArrayList<Account> getAllAccounts(User user) {

        String query = "SELECT * From \"Account_Data\" WHERE \"Account_ID\" IN\n" +
                "  (SELECT \"Account_ID\" FROM \"User_Accounts\" WHERE \"Username\"=?);";
        MyArrayList<Account> accounts = new MyArrayList<>();

        try {
            // Set up PreparedStatement
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, user.getUsername());
            // Execute the statement
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                accounts.add(buildAccount(rs));
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return accounts;
    }

    /**
     * Given a new transaction, add it to the database
     * @param newTransaction - The new Transaction to add to the database
     * @return - The Transaction added to the database
     */
    @Override
    public Transaction addTransaction(Transaction newTransaction) {

        String sql = "INSERT INTO \"Transactions\" VALUES (?,?,?,?,?) RETURNING *";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            // Set Values
            ps.setString(1, newTransaction.getTransactionID());
            ps.setDate(2, newTransaction.getDate());
            ps.setBigDecimal(3, newTransaction.getAmount());
            ps.setString(4, newTransaction.getFrom());
            ps.setString(5, newTransaction.getTo());

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return buildTransaction(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not add an Transaction to the Database!", e);
        }

        return null;
    }

    /**
     * Given a Transaction ID returns the transaction
     * @param transactionId - The transaction ID of the Transaction to fetch
     * @return - The Transaction found associated with the ID
     * @throws ResourceNotFoundException
     */
    @Override
    public Transaction getTransaction(String transactionId) throws ResourceNotFoundException {

        String query = "SELECT * FROM \"Transactions\" WHERE \"Transacation_ID\" = ?";

        try {
            // Set up PreparedStatement
            PreparedStatement ps = connection.prepareStatement(query);
            // Set Values for any Placeholders
            ps.setString(1, transactionId);
            // Execute the statement
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return buildTransaction(rs);
            }
        } catch (SQLException e)
        {
            throw new RuntimeException("Could not get transaction from Database!",e);
        }

        throw new ResourceNotFoundException("Could not Locate transaction in Database!");
    }

    /**
     * Given a Transaction to change, finds, and changes the transaction
     * @param change - The transaction to change
     * @return - The transaction changes
     * @throws ResourceNotFoundException
     */
    @Override
    public Transaction updateTransaction(Transaction change) throws ResourceNotFoundException {

        String sql = "UPDATE \"Transactions\" set \"Date_Made\"=?, \"Amount\"=?, " +
                "\"From_Account\"=?, \"To_Account\"=? WHERE \"Transacation_ID\" = ? RETURNING *";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);

            // Set Values
            ps.setDate(1, change.getDate());
            ps.setBigDecimal(2, change.getAmount());
            ps.setString(3, change.getFrom());
            ps.setString(4, change.getTo());
            ps.setString(5, change.getTransactionID());

            ResultSet rs = ps.executeQuery();

            if(rs.next())
                return buildTransaction(rs);

        } catch (SQLException e) {
            throw new RuntimeException("Could not update transaction from Database!",e);
        }

        throw new ResourceNotFoundException("The transaction could not be found in the database!");
    }

    /**
     * Given a transaction ID, find and delete the transaction
     * @param transactionId - The Transaction id to find and delete
     * @return - the transaction deleted
     * @throws ResourceNotFoundException
     */
    @Override
    public Transaction deleteTransaction(String transactionId) throws ResourceNotFoundException {

        String sql = "DELETE FROM \"Transactions\" WHERE \"Transacation_ID\" = ? RETURNING *";

        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, transactionId);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
                return buildTransaction(rs);

        }catch (SQLException e) {
            throw new RuntimeException("Could not delete Transaction!", e);
        }

        throw new ResourceNotFoundException("Transaction Not Found in Database!");
    }

    /**
     * Returns a custom array list of all the transactions
     * @return - A custom Array list of all the transactions
     */
    @Override
    public MyArrayList<Transaction> getAllTransactions() {

        String query = "SELECT * FROM \"Transactions\"";
        MyArrayList<Transaction> transactions = new MyArrayList<>();

        try {
            // Set up PreparedStatement
            PreparedStatement ps = connection.prepareStatement(query);
            // Execute the statement
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                transactions.add(buildTransaction(rs));
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return transactions;
    }

    /**
     * Given a certain user, get all of their Transactions
     * @param user - The user to get all their transactions
     * @return - Return an array list of all the User's transactions
     */
    @Override
    public MyLinkedList<Transaction> getAllTransactions(User user) {

        String query = "SELECT * FROM \"Transactions\" WHERE \"From_Account\" IN " +
                "(SELECT \"Account_ID\" FROM \"User_Accounts\" WHERE \"Username\" =?);";
        MyLinkedList<Transaction> transactions = new MyLinkedList<>();

        try {
            // Set up PreparedStatement
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, user.getUsername());
            // Execute the statement
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                transactions.insertOrdered(buildTransaction(rs),
                        (Transaction a, Transaction b) -> a.getDate().getTime() < b.getDate().getTime());
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return transactions;
    }

    /**
     * Given an account, get all its transactions
     * @param account - Account to get all its transactions
     * @return - An array list of transactions associated with given account
     */
    @Override
    public MyLinkedList<Transaction> getAllTransactions(Account account) {

        String query = "SELECT * FROM \"Transactions\" WHERE \"From_Account\"=?;";
        MyLinkedList<Transaction> transactions = new MyLinkedList<>();

        try {
            // Set up PreparedStatement
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, account.getAccountID());
            // Execute the statement
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                transactions.insertOrdered(buildTransaction(rs),
                        (Transaction a, Transaction b) -> a.getDate().getTime() < b.getDate().getTime());
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public boolean addUserAccount(String username, String accountId) {

        String sql = "INSERT INTO \"User_Accounts\" VALUES (?,?) RETURNING *";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            // Set Values
            ps.setString(1, username);
            ps.setString(2, accountId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not add a User/Account to the Database!", e);
        }

        return false;
    }

    @Override
    public boolean deleteUserAccount(String username, String accountId) throws ResourceNotFoundException {

        String sql = "DELETE FROM \"User_Accounts\" WHERE \"Username\"=? AND \"Account_ID\"=? RETURNING *";

        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, accountId);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
                return true;

        }catch (SQLException e) {
            throw new RuntimeException("Could not delete Transaction!", e);
        }

        throw new ResourceNotFoundException("User/Account Not Found in Database!");
    }

    /**
     * Given a ResultSet from a Query, returns the constructed user
     * @param rs - The result set holding the User's info
     * @return - The user from the result set
     * @throws SQLException
     */
    private User buildUser(ResultSet rs) throws SQLException {

        User retVal = new User();
        retVal.setUsername(rs.getString("Username"));
        retVal.setFirstName(rs.getString("First_Name"));
        retVal.setLastName(rs.getString("Last_Name"));
        retVal.setPhoneNumber(rs.getString("Phone_Number"));
        retVal.setEmail(rs.getString("Email"));
        retVal.setAddressLine1(rs.getString("Address_Line_1"));
        retVal.setAddressLine2(rs.getString("Address_Line_2"));
        retVal.setState(rs.getString("State"));
        retVal.setZipCode(rs.getString("Zip_Code"));
        retVal.setSSN_Hash(rs.getString("SSN_Hash"));
        retVal.setPass_Hash(rs.getString("Pass_Hash"));

        return retVal;
    }

    /**
     * Given a ResultSet from a Query, returns the constructed Account
     * @param rs - The result set holding the Account's info
     * @return - The account from the result set
     * @throws SQLException
     */
    private Account buildAccount(ResultSet rs) throws SQLException {

        Account retVal = new Account();
        retVal.setAccountID(rs.getString("Account_ID"));
        retVal.setBalance(rs.getBigDecimal("Balance"));
        retVal.setType(Account.AccountType.valueOf(rs.getString("Type")));
        retVal.setActive(rs.getBoolean("Active"));

        return retVal;
    }

    /**
     * Given a ResultSet from a Query, returns the constructed Transaction
     * @param rs - The result set holding the Transaction's info
     * @return - The Transaction from the result set
     * @throws SQLException
     */
    private Transaction buildTransaction(ResultSet rs) throws SQLException {

        Transaction retVal = new Transaction();
        retVal.setTransactionID(rs.getString("Transacation_ID"));
        retVal.setDate(rs.getDate("Date_Made"));
        retVal.setAmount(rs.getBigDecimal("Amount"));
        retVal.setFrom(rs.getString("From_Account"));
        retVal.setTo(rs.getString("To_Account"));

        return retVal;
    }

    /**
     * Returns if there is a connection to the data storage
     * @return - true if the connection is established
     */
    @Override
    public boolean isConnected() {
        return connection != null;
    }
}
