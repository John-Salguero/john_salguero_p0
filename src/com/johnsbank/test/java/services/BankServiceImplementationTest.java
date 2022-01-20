package com.johnsbank.test.java.services;

import com.johnsbank.java.app.BankingApp;
import com.johnsbank.java.models.Account;
import com.johnsbank.java.models.Transaction;
import com.johnsbank.java.models.User;
import com.johnsbank.java.services.BankServiceImplementation;
import com.johnsbank.java.utilities.HashGenerator;
import com.johnsbank.java.utilities.MyArrayList;
import com.johnsbank.java.utilities.MyLinkedList;
import com.johnsbank.java.utilities.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class BankServiceImplementationTest {

    static HashGenerator hash = HashGenerator.getInstance();
    static Supplier<String> goodFirstNames = new FirstNames();
    static Supplier<String> goodLastNames = new LastNames();
    static Supplier<String> goodEmailAddresses = new Email();
    static Supplier<String> goodPhoneNumber = new PhoneNumber();
    static Supplier<String> goodZipCode = new ZipCode();
    static Supplier<String> goodAddress = new Address();
    static Supplier<String> goodCity = new City();
    static Supplier<String> goodState = new State();
    static Supplier<String> goodUsername = new Username();


    @Test
    void addUser() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();

        // Good Case
        User newUser = buildRandomUser();
        User addedUser = service.addUser(newUser);
        assertEquals(addedUser.getUsername(), newUser.getUsername());

        // Bad Cases
        addedUser = service.addUser(newUser);
        assertNull(addedUser);
        newUser = null;
        addedUser = service.addUser(newUser);
        assertNull(addedUser);
        newUser = new User();
        addedUser = service.addUser(newUser);
        assertNull(addedUser);
        newUser.setFirstName(goodFirstNames.get());
        addedUser = service.addUser(newUser);
        assertNull(addedUser);
        newUser.setLastName(goodLastNames.get());
        addedUser = service.addUser(newUser);
        assertNull(addedUser);
        newUser.setPhoneNumber(goodPhoneNumber.get());
        addedUser = service.addUser(newUser);
        assertNull(addedUser);
        newUser.setZipCode(goodZipCode.get());
        addedUser = service.addUser(newUser);
        assertNull(addedUser);
        newUser.setAddressLine1(goodAddress.get());
        addedUser = service.addUser(newUser);
        assertNull(addedUser);
        newUser.setAddressLine2(goodCity.get());
        addedUser = service.addUser(newUser);
        assertNull(addedUser);
        newUser.setState(goodState.get());
        addedUser = service.addUser(newUser);
        assertNull(addedUser);
        newUser.setEmail(goodEmailAddresses.get());
        addedUser = service.addUser(newUser);
        assertNull(addedUser);
        newUser.setUsername(goodUsername.get());
        addedUser = service.addUser(newUser);
        assertNull(addedUser);
        newUser.setSSN_Hash(hash.getMessageDigest(UUID.randomUUID().toString()));
        addedUser = service.addUser(newUser);
        assertNull(addedUser);
    }

    @Test
    void getUser() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();

        User user = buildRandomUser();
        User testUser = null;
        service.addUser(user);

        // Bad Case
        ResourceNotFoundException e =
                assertThrows(ResourceNotFoundException.class, ()->service.getUser(buildRandomUser().getUsername()));
        assertNotNull(e);
        e = assertThrows(ResourceNotFoundException.class, ()->service.getUser(null));
        assertNotNull(e);

        // Good Case
        try {
            testUser = service.getUser(user.getUsername());
        } catch (ResourceNotFoundException ex) {
            ex.printStackTrace();
        }
        assertNotNull(testUser);
    }

    @Test
    void updateUser() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();

        // Good Case
        User newUser = buildRandomUser();
        service.addUser(newUser);
        User testUser = new User();
        testUser.setUsername(newUser.getUsername());
        testUser.setFirstName(goodFirstNames.get());
        testUser.setLastName(goodLastNames.get());
        testUser.setPhoneNumber(goodPhoneNumber.get());
        testUser.setZipCode(goodZipCode.get());
        testUser.setAddressLine1(goodAddress.get());
        testUser.setAddressLine2(goodCity.get());
        testUser.setState(goodState.get());
        testUser.setEmail(goodEmailAddresses.get());
        testUser.setSSN_Hash(hash.getMessageDigest(UUID.randomUUID().toString()));
        testUser.setPass_Hash(hash.getMessageDigest(UUID.randomUUID().toString()));

        User changedUser = null;
        try {
            changedUser = service.updateUser(testUser);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(changedUser);
        assertNotEquals(changedUser.getEmail(), newUser.getEmail());

        // Bad Cases
        ResourceNotFoundException e =
                assertThrows(ResourceNotFoundException.class, ()->service.updateUser(buildRandomUser()));
        assertNotNull(e);

        try {
            newUser = null;
            changedUser = service.updateUser(newUser);
            assertNull(changedUser);
            newUser = new User();
            changedUser = service.updateUser(newUser);
            assertNull(changedUser);
            changedUser = service.updateUser(newUser);
            assertNull(changedUser);
            newUser.setFirstName(goodFirstNames.get());
            changedUser = service.updateUser(newUser);
            assertNull(changedUser);
            newUser.setLastName(goodLastNames.get());
            changedUser = service.updateUser(newUser);
            assertNull(changedUser);
            newUser.setPhoneNumber(goodPhoneNumber.get());
            changedUser = service.updateUser(newUser);
            assertNull(changedUser);
            newUser.setZipCode(goodZipCode.get());
            changedUser = service.updateUser(newUser);
            assertNull(changedUser);
            newUser.setAddressLine1(goodAddress.get());
            changedUser = service.updateUser(newUser);
            assertNull(changedUser);
            newUser.setAddressLine2(goodCity.get());
            changedUser = service.updateUser(newUser);
            assertNull(changedUser);
            newUser.setState(goodState.get());
            changedUser = service.updateUser(newUser);
            assertNull(changedUser);
            newUser.setEmail(goodEmailAddresses.get());
            changedUser = service.updateUser(newUser);
            assertNull(changedUser);
            newUser.setUsername(goodUsername.get());
            changedUser = service.updateUser(newUser);
            assertNull(changedUser);
            newUser.setSSN_Hash(hash.getMessageDigest(UUID.randomUUID().toString()));
            changedUser = service.updateUser(newUser);
            assertNull(changedUser);
        }catch (ResourceNotFoundException ex) {
            ex.printStackTrace();
        }


    }

    @Test
    void deleteUser() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();

        // bad case
        User newUser = buildRandomUser();
        User testUser = null;
        ResourceNotFoundException e =
                assertThrows(ResourceNotFoundException.class, ()->service.deleteUser(newUser.getUsername()));
        assertNotNull(e);
        service.addUser(newUser);

        // good case
        try {
            testUser = service.deleteUser(newUser.getUsername());
        } catch (ResourceNotFoundException ex) {
            ex.printStackTrace();
        }
        assertNotNull(newUser);

        e = assertThrows(ResourceNotFoundException.class, ()->service.getUser(newUser.getUsername()));
        assertNotNull(e);
    }

    @Test
    void getAllUsers() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();

        // Good case
        MyArrayList<User> list = service.getAllUsers();
        assertNotNull(list);
    }

    @Test
    void getAllOwners() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
        Account account = buildRandomAccount();
        service.addAccount(account);
        MyArrayList<User> list = new MyArrayList<>();
        int count = (int)((Math.random() + 1) * 4);

        for(int i = 0; i < count; ++i) {
            list.add(buildRandomUser());
            service.addUser(list.end());
            try {
                service.makeOwner(account, list.end());
            } catch (ResourceNotFoundException e) {
                e.printStackTrace();
            }
        }


        // Bad Case
        ResourceNotFoundException e =
                assertThrows(ResourceNotFoundException.class, ()->service.getAllOwners(null));
        assertNotNull(e);

        // Good Case
        try {
            list = service.getAllOwners(account);
        } catch (ResourceNotFoundException ex) {
            ex.printStackTrace();
        }

        assertNotNull(list);
        assertEquals(list.getCount(), count);
    }

    @Test
    void addAccount() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
        Account newAccount = null;
        Account testAccount;

        // Bad Case
        testAccount = service.addAccount(newAccount);
        assertNull(testAccount);
        newAccount = new Account();
        testAccount = service.addAccount(newAccount);
        assertNull(testAccount);
        newAccount.setAccountID(hash.getMessageDigest(UUID.randomUUID().toString()));
        testAccount = service.addAccount(newAccount);
        assertNull(testAccount);
        newAccount.setBalance(new BigDecimal(0));
        testAccount = service.addAccount(newAccount);
        assertNull(testAccount);

        //Good Case
        newAccount.setType(Account.AccountType.values()[(int)(Math.random() * 6)]);
        testAccount = service.addAccount(newAccount);
        assertNotNull(testAccount);
    }

    @Test
    void getAccount() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
        Account newAccount = buildRandomAccount();
        service.addAccount(newAccount);

        // bad case
        ResourceNotFoundException e =
                assertThrows(ResourceNotFoundException.class,
                        ()->service.getAccount(hash.getMessageDigest(UUID.randomUUID().toString())));
        assertNotNull(e);

        // good case
        try {
            newAccount = service.getAccount(newAccount.getAccountID());
        } catch (ResourceNotFoundException ex) {
            ex.printStackTrace();
        }
        assertNotNull(newAccount);
    }

    @Test
    void updateAccount() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
        Account newAccount = buildRandomAccount();
        Account changedAccount;
        Account testAccount = null;
        service.addAccount(newAccount);

        // bad case
        ResourceNotFoundException e =
                assertThrows(ResourceNotFoundException.class, ()->service.updateAccount(buildRandomAccount()));
        assertNotNull(e);
        try {
            changedAccount = service.updateAccount(testAccount);
            assertNull(changedAccount);
            testAccount = new Account();
            changedAccount = service.updateAccount(testAccount);
            assertNull(changedAccount);
            testAccount.setAccountID(newAccount.getAccountID());
            changedAccount = service.updateAccount(testAccount);
            assertNull(changedAccount);
            testAccount.setBalance(newAccount.getBalance());
            changedAccount = service.updateAccount(testAccount);
            assertNull(changedAccount);
            testAccount.setType(Account.AccountType.values()[(int)(Math.random() * 6)]);

        }catch (ResourceNotFoundException ex) {
            ex.printStackTrace();
        }


        // good case
        try {
            changedAccount = service.updateAccount(testAccount);
            assertNotNull(changedAccount);
        }catch (ResourceNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void deleteAccount() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
        Account newAccount = buildRandomAccount();
        Account testAccount = null;

        // Bad Case
        ResourceNotFoundException e =
                assertThrows(ResourceNotFoundException.class, ()->service.deleteAccount(newAccount.getAccountID()));
        assertNotNull(e);

        service.addAccount(newAccount);
        // Good Case
        try {
            testAccount = service.deleteAccount(newAccount.getAccountID());
        } catch (ResourceNotFoundException ex) {
            ex.printStackTrace();
        }
        assertNotNull(testAccount);

    }

    @Test
    void getAllAccounts() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();

        // Good Case
        MyArrayList<Account> list = service.getAllAccounts();
        assertNotNull(list);
    }

    @Test
    void testGetAllAccounts() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
        User newUser = buildRandomUser();
        service.addUser(newUser);
        int count = (int)((Math.random() + 1) * 4);
        try {
            for (int i = 0; i < count; ++i) {
                service.makeOwner(service.addAccount(buildRandomAccount()), newUser);
            }
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

        // Bad case
        ResourceNotFoundException ex =
                assertThrows(ResourceNotFoundException.class, ()->service.getAllAccounts(null));
        assertNotNull(ex);

        // Good case
        MyArrayList<Account> list = null;
        try {
            list = service.getAllAccounts(newUser);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(list);

    }

    @Test
    void sendTransaction() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
        Account to = buildRandomAccount();
        Account from = buildRandomAccount();
        service.addAccount(to);
        service.addAccount(from);
        Transaction newTransaction = new Transaction(new Date(System.currentTimeMillis()), new BigDecimal(-50),
                null, null);
        service.makeDeposit(from, new BigDecimal(50));

        // bad case
        assertFalse(service.sendTransaction(null));
        assertFalse(service.sendTransaction(newTransaction));
        newTransaction.setTo(to.getAccountID());
        assertFalse(service.sendTransaction(newTransaction));
        newTransaction.setFrom(from.getAccountID());
        assertFalse(service.sendTransaction(newTransaction));
        newTransaction.setAmount(new BigDecimal(50));

        // good case
        assertTrue(service.sendTransaction(newTransaction));
        newTransaction = new Transaction(new Date(System.currentTimeMillis()), new BigDecimal(50),
                to.getAccountID(), hash.getMessageDigest(UUID.randomUUID().toString()));
        assertTrue(service.sendTransaction(newTransaction));

    }

    @Test
    void getTransaction() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
        Account to = buildRandomAccount();
        Account from = buildRandomAccount();
        service.addAccount(to);
        service.addAccount(from);
        Transaction newTransaction = new Transaction(new Date(System.currentTimeMillis()), new BigDecimal(50),
                from.getAccountID(), to.getAccountID());
        service.makeDeposit(from, new BigDecimal(50));

        // Good Case
        assertTrue(service.sendTransaction(newTransaction));

    }

//    @Test
//    void updateTransaction() {
//        BankServiceImplementation service = BankServiceImplementation.getInstance();
//    }
//
//    @Test
//    void deleteTransaction() {
//        BankServiceImplementation service = BankServiceImplementation.getInstance();
//    }

    @Test
    void getAllTransactions() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();

        // Good case
        MyArrayList<Transaction> list = service.getAllTransactions();
        assertNotNull(list);
    }

    @Test
    void testGetAllTransactions() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
        Account to = buildRandomAccount();
        Account from = buildRandomAccount();
        service.addAccount(to);
        service.addAccount(from);
        Transaction newTransaction = new Transaction(new Date(System.currentTimeMillis()), new BigDecimal(50),
                from.getAccountID(), to.getAccountID());
        service.makeDeposit(from, new BigDecimal(50));

        // Good Case
        service.sendTransaction(newTransaction);
        MyLinkedList<Transaction> list = null;
        try {
            list = service.getAllTransactions(to);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

        assertNotNull(list);

    }

    @Test
    void testGetAllTransactions1() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
        User owner = buildRandomUser();
        Account to = buildRandomAccount();
        Account from = buildRandomAccount();
        Transaction newTransaction = new Transaction(new Date(System.currentTimeMillis()), new BigDecimal(50),
                from.getAccountID(), to.getAccountID());
        service.addAccount(to);
        service.addAccount(from);
        service.addUser(owner);
        try {
            service.makeOwner(to, owner);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        service.makeDeposit(from, new BigDecimal(50));

        // Good Case
        service.sendTransaction(newTransaction);
        MyLinkedList<Transaction> list = null;
        try {
            list = service.getAllTransactions(owner);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

        assertNotNull(list);
    }

    @Test
    void makeOwner() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
        Account newAccount = buildRandomAccount();
        User owner = buildRandomUser();
        service.addUser(owner);
        service.addAccount(newAccount);

        // Good Case
        try {
            assertTrue(service.makeOwner(newAccount, owner));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    void revokeOwner() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
        Account newAccount = buildRandomAccount();
        User owner = buildRandomUser();
        service.addUser(owner);
        service.addAccount(newAccount);

        // Good Case
        try {
            assertTrue(service.makeOwner(newAccount, owner));
            assertTrue(service.revokeOwner(newAccount, owner));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void usernameIsUnique() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
    }

    @Test
    void makeDeposit() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
    }

    @Test
    void makeWithdrawal() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
    }

    @Test
    void holdAccount() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
    }

    @Test
    void removeHold() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
    }

    @Test
    void logUserIn() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
    }

    @Test
    void isConnected() {
        BankServiceImplementation service = BankServiceImplementation.getInstance();
    }

    static private Account buildRandomAccount() {


        Account retVal = new Account();

        retVal.setType(Account.AccountType.values()[(int)(Math.random() * 6)]);
        retVal.setBalance(new BigDecimal(0));
        retVal.setActive(true);
        retVal.setAccountID(hash.getMessageDigest(UUID.randomUUID().toString()));

        return retVal;
    }

    static private User buildRandomUser() {
        User newUser = new User();
        newUser.setFirstName(goodFirstNames.get());
        newUser.setLastName(goodLastNames.get());
        newUser.setPhoneNumber(goodPhoneNumber.get());
        newUser.setZipCode(goodZipCode.get());
        newUser.setAddressLine1(goodAddress.get());
        newUser.setAddressLine2(goodCity.get());
        newUser.setState(goodState.get());
        newUser.setEmail(goodEmailAddresses.get());
        newUser.setUsername(goodUsername.get());
        newUser.setSSN_Hash(hash.getMessageDigest(UUID.randomUUID().toString()));
        newUser.setPass_Hash(hash.getMessageDigest(UUID.randomUUID().toString()));

        return newUser;
    }

    private static class FirstNames implements Supplier<String> {

        @Override
        public String get() {

            InputStream is = BankingApp.class.getClassLoader().getResourceAsStream("first-names.txt");
            try {
                is.skip((long)(Math.random() * 35145));
                char nextChar = (char)is.read();
                while(nextChar != '\n'){nextChar = (char)is.read();}
                StringBuilder out = new StringBuilder();
                nextChar = (char)is.read();
                do {out.append(nextChar); nextChar = (char)is.read();}while(nextChar != '\n');

                is.close();
                return out.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    private static class LastNames implements Supplier<String> {

        @Override
        public String get() {
            InputStream is = BankingApp.class.getClassLoader().getResourceAsStream("last-names.txt");
            try {
                is.skip((long)(Math.random() * 157358));
                char nextChar = (char)is.read();
                while(nextChar != '\n'){nextChar = (char)is.read();}
                StringBuilder out = new StringBuilder();
                nextChar = (char)is.read();
                do {out.append(nextChar); nextChar = (char)is.read();}while(nextChar != '\n');

                is.close();
                return out.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }private static class PhoneNumber implements Supplier<String> {

        @Override
        public String get() {
            StringBuilder out = new StringBuilder("(");
            while(out.length() < 4)
                out.append((int)(Math.random() * 10));
            out.append(") ");
            while(out.length() < 9)
                out.append((int)(Math.random() * 10));
            out.append("-");
            while(out.length() < 14)
                out.append((int)(Math.random() * 10));

            return out.toString();
        }
    }private static class Address implements Supplier<String> {

        @Override
        public String get() {
            StringBuilder out = new StringBuilder();
            out.append((int)(Math.random() * 10000)).append(" ");

            InputStream is = BankingApp.class.getClassLoader().getResourceAsStream("street-names.txt");
            try {
                is.skip((long)(Math.random() * 827186));
                char nextChar = (char)is.read();
                while(nextChar != '\n'){nextChar = (char)is.read();}
                nextChar = (char)is.read();
                do {out.append(nextChar); nextChar = (char)is.read();}while(nextChar != '\n');

                is.close();
                return out.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }private static class City implements Supplier<String> {

        @Override
        public String get() {
            InputStream is = BankingApp.class.getClassLoader().getResourceAsStream("city-names.txt");
            try {
                is.skip((long)(Math.random() * 3805));
                char nextChar = (char)is.read();
                while(nextChar != '\n'){nextChar = (char)is.read();}
                StringBuilder out = new StringBuilder();
                nextChar = (char)is.read();
                do {out.append(nextChar); nextChar = (char)is.read();}while(nextChar != '\n');

                is.close();
                return out.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }private static class State implements Supplier<String> {

        static String[] territoryCodes = {"AL","AK","AZ","AR","AS","CA","CO","CT","DE","DC",
                "FL","GA","GU","HI","ID","IL","IN","IA","KS","KY",
                "LA","ME","MD","MA","MI","MN","MS","MO","MT","NE",
                "NV","NH","NJ","NM","NY","NC","ND","CM","OH","OK",
                "OR","PA","PR","RI","SC","SD","TN","TX","TT","UT",
                "VT","VA","VI","WA","WV","WI","WY"};

        @Override
        public String get() {
            return territoryCodes[(int)(Math.random() * 57)];
        }
    }private static class ZipCode implements Supplier<String> {

        @Override
        public String get() {
            StringBuilder out = new StringBuilder();
            while(out.length() < 5)
                out.append((int)(Math.random() * 10));
            if(Math.random() > .5)
                return out.toString();

            out.append("-");
            while (out.length() < 10)
                out.append((int)(Math.random() * 10));

            return out.toString();
        }
    }private static class Email implements Supplier<String> {

        @Override
        public String get() {
            InputStream adj = BankingApp.class.getClassLoader().getResourceAsStream("english-adjectives.txt");
            InputStream nou = BankingApp.class.getClassLoader().getResourceAsStream("english-nouns.txt");
            InputStream tld = BankingApp.class.getClassLoader().getResourceAsStream("tld-list.txt");

            StringBuilder out = new StringBuilder();

            try {
                adj.skip((long)(Math.random() * 10846));
                char nextChar = (char)adj.read();
                while(nextChar != '\n'){nextChar = (char)adj.read();}
                nextChar = (char)adj.read();
                do {out.append(nextChar); nextChar = (char)adj.read();}while(nextChar != '\n');

                out.append(".");
                adj.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            try {
                nou.skip((long)(Math.random() * 10702));
                char nextChar = (char)nou.read();
                while(nextChar != '\n'){nextChar = (char)nou.read();}
                nextChar = (char)nou.read();
                do {out.append(nextChar); nextChar = (char)nou.read();}while(nextChar != '\n');
                out.append("@");
                nextChar = (char)nou.read();
                do {out.append(nextChar); nextChar = (char)nou.read();}while(nextChar != '\n');
                out.append(".");

                nou.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            try {
                tld.skip((long)(Math.random() * 8586));
                char nextChar = (char)tld.read();
                while(nextChar != '\n'){nextChar = (char)tld.read();}
                nextChar = (char)tld.read();
                do {out.append(nextChar); nextChar = (char)tld.read();}while(nextChar != '\n');

                tld.close();
                return out.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }private static class Username implements Supplier<String> {

        @Override
        public String get() {
            InputStream adj = BankingApp.class.getClassLoader().getResourceAsStream("english-adjectives.txt");
            InputStream nou = BankingApp.class.getClassLoader().getResourceAsStream("english-nouns.txt");

            StringBuilder out = new StringBuilder();

            try {
                adj.skip((long)(Math.random() * 10846));
                char nextChar = (char)adj.read();
                while(nextChar != '\n'){nextChar = (char)adj.read();}
                nextChar = (char)adj.read();
                do {out.append(nextChar); nextChar = (char)adj.read();}while(nextChar != '\n');

                out.append(".");
                adj.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            try {
                nou.skip((long)(Math.random() * 10702));
                char nextChar = (char)nou.read();
                while(nextChar != '\n'){nextChar = (char)nou.read();}
                nextChar = (char)nou.read();
                do {out.append(nextChar); nextChar = (char)nou.read();}while(nextChar != '\n');

                nou.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            if(out.length() > 25)
                out.replace(25, out.length(), "");
            while(out.length() < 25 && Math.random() > .3)
                out.append((int)(Math.random() * 10));

            return out.toString();
        }
    }


}