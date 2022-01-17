package com.johnsbank.java.menus;

import com.johnsbank.java.models.Account;
import com.johnsbank.java.models.Transaction;
import com.johnsbank.java.models.User;
import com.johnsbank.java.services.BankService;
import com.johnsbank.java.services.BankServiceImplementation;
import com.johnsbank.java.utilities.HashGenerator;
import com.johnsbank.java.utilities.MyArrayList;
import com.johnsbank.java.utilities.MyLinkedList;
import com.johnsbank.java.utilities.ResourceNotFoundException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import static com.johnsbank.java.menus.ScreenPrinter.*;  // All the functions to print to the screen
import static com.johnsbank.java.app.BankingApp.running; // Whether the app is running or not
import static com.johnsbank.java.app.BankingApp.scan;    // Scanner for input

/**
 * Used to control the flow of user experience, menu selection
 */
public final class MenuInterface {

    private static final BankService service = BankServiceImplementation.getInstance();
    private static final HashGenerator hash = HashGenerator.getInstance();

    /**
     * Used with the command pattern to accept lambdas
     */
    private interface Validator {
        boolean validate(String input);
    }

    static private class PhoneValidator implements Validator {
        public boolean validate(String phoneNumber) {
            if(phoneNumber.length() != 14)
                return false;
            if(phoneNumber.charAt(0) != '(')
                return false;
            if(phoneNumber.charAt(4) != ')')
                return false;
            if(phoneNumber.charAt(5) != ' ')
                return false;
            if(phoneNumber.charAt(9) != '-')
                return false;
            try{
                Integer.parseInt(phoneNumber.substring(1,4));
                Integer.parseInt(phoneNumber.substring(6,9));
                Integer.parseInt(phoneNumber.substring(10,14));
            } catch (NumberFormatException e) {
                return false;
            }

            return true;
        }
    }


    static private class PasswordValidator implements Validator {
        public boolean validate(String password) {

            if(password.length() < 8)
                return false;

            boolean hasSpecial = false;
            boolean hasCapital = false;
            boolean hasLowercase = false;
            boolean hasNumber =false;

            for(int i = 0; i < password.length(); ++i){
                char at = password.charAt(i);
                if(at >= 'a' && at <= 'z')
                    hasLowercase = true;
                else if (at >= 'A' && at <= 'Z')
                    hasCapital = true;
                else if (at >= '0' && at <= '9')
                    hasNumber = true;
                else if(at == '!' || at == '@' || at == '#' ||
                        at == '$' || at == '%' || at == '_')
                    hasSpecial = true;
                else
                    return false;
            }


            return hasSpecial && hasCapital && hasLowercase && hasNumber;
        }
    }
    static private class UsernameValidator implements Validator {
        public boolean validate(String username) {

            String usernameLower = username.toLowerCase(Locale.US);
            for(int i = 0; i < username.length(); ++i)
            {
                char at = usernameLower.charAt(i);
                if((at > 'z' || at < 'a') && (at > '9' || at < '0') &&
                        at != '.' && at != '-' && at != '_')
                    return false;
            }

            return true;
        }
    }
    static private class SSNValidator implements Validator {
        public boolean validate(String SSN) {

            String[] parts = SSN.split("-");
            if(parts.length != 3)
                return false;

            for(int i = 0; i < 3; ++i)
            {
                try {
                    Integer.parseInt(parts[i]);
                } catch (NumberFormatException e)
                {
                    return false;
                }

            }

            return true;
        }
    }
    static private class StateValidator implements Validator {
        public boolean validate(String state) {
            switch (state) {
                case "AL":
                case "AK":
                case "AZ":
                case "AR":
                case "AS":
                case "CA":
                case "CO":
                case "CT":
                case "DE":
                case "DC":
                case "FL":
                case "GA":
                case "GU":
                case "HI":
                case "ID":
                case "IL":
                case "IN":
                case "IA":
                case "KS":
                case "KY":
                case "LA":
                case "ME":
                case "MD":
                case "MA":
                case "MI":
                case "MN":
                case "MS":
                case "MO":
                case "MT":
                case "NE":
                case "NV":
                case "NH":
                case "NJ":
                case "NM":
                case "NY":
                case "NC":
                case "ND":
                case "CM":
                case "OH":
                case "OK":
                case "OR":
                case "PA":
                case "PR":
                case "RI":
                case "SC":
                case "SD":
                case "TN":
                case "TX":
                case "TT":
                case "UT":
                case "VT":
                case "VA":
                case "VI":
                case "WA":
                case "WV":
                case "WI":
                case "WY":
                    return true;
                default:
                    return false;
            }
        }
    }
    static private class ZipcodeValidator implements Validator {
        public boolean validate(String zipCode) {

            String[] parts = zipCode.toLowerCase(Locale.US).split("-");
            if(parts.length > 2)
                return false;

            if(parts[0].length() != 5)
                return false;
            for(int i = 0; i < parts[0].length(); ++i) {
                if(parts[0].charAt(i) > '9' || parts[0].charAt(i) < '0')
                    return false;
            }

            if(parts.length > 1) {
                if (parts[1].length() != 4)
                    return false;
                for (int i = 0; i < parts[1].length(); ++i) {
                    if (parts[1].charAt(i) > '9' || parts[1].charAt(i) < '0')
                        return false;
                }
            }

            return true;
        }
    }
    static private class EmailValidator implements Validator {
        public boolean validate(String email) {

            String[] parts = email.toLowerCase(Locale.US).split("@");

            if(parts.length != 2)
                return false;

            // Check the prefix
            boolean wasSpecial = false;
            for(int i = 0; i < parts[0].length(); ++i)
            {
                if(parts[0].charAt(i) == '.' ||
                        parts[0].charAt(i) == '-' ||
                        parts[0].charAt(i) == '_') {
                    if(wasSpecial)
                        return false;
                    wasSpecial = true;
                }
                else if((parts[0].charAt(i) <= 'z' || parts[0].charAt(i) >= 'a') &&
                        (parts[0].charAt(i) <= '9' || parts[0].charAt(i) >= '0'))
                    wasSpecial = false;
                else
                    return false;
            }
            if(wasSpecial) return false;

            // Check the domain
            parts = parts[1].split("\\.");
            if(parts.length != 2)
                return false;

            for(int i = 0; i < 2; ++i) {
                for (int j = 1; j < parts[i].length(); ++j) {
                    if (parts[i].charAt(j) > 'z' || parts[i].charAt(j) < 'a')
                        return false;
                }
            }

            return email.indexOf(email.length() - 1) != '.';
        }
    }
    static private class AddressValidator implements Validator {
        public boolean validate(String address) {
            return address.length() < 100 && address.length() > 3;
        }
    }
    static private class Address2Validator implements Validator {
        public boolean validate(String address) {
            return address.length() < 100;
        }
    }


    private static String ValidateInput(String nameOfInput, String[] forms, Validator method) {

        String input;

        boolean isValid;
        do {

            int formCount = 0;
            System.out.println("Please enter your " + nameOfInput + ".");
            System.out.print("it takes the form of \"" + forms[formCount++] + "'");
            while(formCount < forms.length)
            {
                System.out.print(" OR \"" + forms[formCount++] + '"');
            }
            System.out.println(":");

            input = scan.nextLine();
            isValid = method.validate(input);
            if (!isValid) {
                System.out.println("\nThat is not a valid format, please use the valid format\n");
            }

        } while (!isValid);

        return input;
    }

    static void inputPassword(User newUser) {

        String passHash = null;
        PasswordValidator validator =new PasswordValidator();


        boolean confirmed = false;
        do {
            String password;
            System.out.println("Please enter a password of at least 8 characters with at least 1 capital letter," +
                    "1 lowercase letter, 1 number, and 1 special {!, @, #, $, %, _} character.");
            // if a console is attached, make the password secure
            if (System.console() != null) {
                char[] charPass = System.console().readPassword("Password: ");
                password = new String(charPass);
            } else {
                System.out.print("Password: ");
                password = scan.nextLine();
            }
            if (!validator.validate(password)){
                System.out.println("\nThat is not a valid format, please use the valid format\n");
                continue;
            }
            // Confirm the password
            System.out.println("Please Confirm the Password by typing it again.");
            if (System.console() != null) {
                char[] charPass = System.console().readPassword("Password: ");
                if(!password.equals(new String(charPass))){
                    System.out.println("\nThe passwords do not match!\n");
                    continue;
                }
            } else {
                System.out.print("Password: ");
                if(!password.equals(scan.nextLine())){
                    System.out.println("\nThe passwords do not match!\n");
                    continue;
                }
            }

            passHash = hash.getMessageDigest(password);
            confirmed = true;

        } while(!confirmed);

        newUser.setPass_Hash(passHash);
        System.out.println("Your password has been set!");
    }

    static void inputUsername(User newUser) {
        boolean confirmed = false;
        String username;

        do {
            String[] usernameForms = {"of Only AlphaNumeric characters and only {_,., -} Special characters"};
            username = ValidateInput("username", usernameForms, new UsernameValidator());

            boolean isValid = false;
            do{
                System.out.println("\nPlease Confirm your Username");
                System.out.println("username: " + username);
                System.out.print("Confirm your Username? (Y/n): ");
                String input = scan.nextLine();
                if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                    isValid = true;
                    confirmed = true;
                }
                else if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
                {
                    isValid = true;
                    System.out.println("\n Contact information has been invalidated, please input your contact info.");
                }
                else
                    System.out.println("That is not a valid option. Please respond with 'Y' or 'N'.");

            }while(!isValid);

            if(confirmed && !service.usernameIsUnique(username)) {
                System.out.println("Sorry, the username " + username + "has already been taken, please choose another");
                confirmed = false;
            }
        }while(!confirmed);

        newUser.setUsername(username);
    }

    static void inputSSN(User newUser) {
        boolean confirmed = false;
        String SSNHash;

        do {
            String[] SSNForms = {"xxx-xx-xxxx"};
            String SSN = ValidateInput("SSN", SSNForms, new SSNValidator());
            SSNHash = hash.getMessageDigest(SSN);

            boolean isValid = false;
            do{
                System.out.println("\nPlease Confirm your SSN");
                System.out.print("Confirm your SSN? (Y/n): ");
                System.out.println("SSN: " + SSN);
                String input = scan.nextLine();
                if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                    isValid = true;
                    confirmed = true;
                }
                else if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
                {
                    isValid = true;
                    System.out.println("\n Contact information has been invalidated, please input your contact info.");
                }
                else
                    System.out.println("That is not a valid option. Please respond with 'Y' or 'N'.");

            }while(!isValid);
        }while(!confirmed);

        newUser.setSSN_Hash(SSNHash);
    }

    static void inputContact(User newUser) {
        boolean confirmed = false;
        String phoneNumber;
        String email;
        String address1;
        String address2;
        String state;
        String zipCode;

        do {
            String[] phoneNumberForms = {"(xxx) xxx-xxxx"};
            phoneNumber = ValidateInput("phone number", phoneNumberForms, new PhoneValidator());
            String[] emailForms = {"example@email.com"};
            email = ValidateInput("email", emailForms, new EmailValidator());
            String[] address1Forms = {"555 110th Ave NE"};
            address1 = ValidateInput("address line (100 character limit)", address1Forms, new AddressValidator());
            String[] address2Forms = {"City Name"};
            address2 = ValidateInput("city", address2Forms, new Address2Validator());
            String[] stateForms = {"two letter State form e.g. WA"};
            state = ValidateInput("State(Territory)", stateForms, new StateValidator());
            String[] zipForms = {"98053" , "98053-7241"};
            zipCode = ValidateInput("Zip-Code", zipForms, new ZipcodeValidator());


            boolean isValid = false;
            do{
                System.out.println("\nPlease Confirm your contact information");
                System.out.println("Phone Number: " + phoneNumber);
                System.out.println("Email: " + email);
                System.out.println("Address Line 1: " + address1);
                System.out.println("City: " + address2);
                System.out.println("State: " + state);
                System.out.println("Zipcode: " + zipCode);
                System.out.print("Confirm your contact information? (Y/n): ");
                String input = scan.nextLine();
                if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                    isValid = true;
                    confirmed = true;
                }
                else if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
                {
                    isValid = true;
                    System.out.println("\n Contact information has been invalidated, please input your contact info.");
                }
                else
                    System.out.println("That is not a valid option. Please respond with 'Y' or 'N'.");

            }while(!isValid);


        }while(!confirmed);

        newUser.setPhoneNumber(phoneNumber);
        newUser.setEmail(email);
        newUser.setAddressLine1(address1);
        newUser.setAddressLine2(address2);
        newUser.setState(state);
        newUser.setZipCode(zipCode);
    }



    static void inputName(User newUser) {

        boolean confirmed = false;
        String firstName;
        String lastName;

        do {
            System.out.print("What is your first name? ");
            firstName = scan.nextLine();
            System.out.print("What is your last name? ");
            lastName = scan.nextLine();

            boolean valid = false;
            do{
                System.out.println("\nPlease Confirm your name");
                System.out.print("Is your name " + firstName + " " + lastName + "? (Y/n): ");
                String input = scan.nextLine();
                if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                    valid = true;
                    confirmed = true;
                }
                else if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
                {
                    valid = true;
                }
                else
                    System.out.println("That is not a valid option. Please respond with 'Y' or 'N'.");

            }while(!valid);
        }while(!confirmed);

        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
    }
    static void register(){
        // holds info for the prospective new user
        User newUser = new User();

        // Clear the screen and take the users input
        clear();

        // Print out the sign in prompt
        String[] registerPrompt = {
                "Register New User!"
        };
        framePrint(registerPrompt);


        inputName(newUser);
        inputContact(newUser);
        inputSSN(newUser);
        inputUsername(newUser);
        inputPassword(newUser);

        if(service.addUser(newUser) == null)
        {
            // Clear the screen and take the users input
            clear();

            // Print out the sign in prompt
            String[] registeredPrompt = {
                    "Failed to Registered",
                    "Press \"Enter\" To Continue"
            };
            framePrint(registeredPrompt);
            scan.nextLine();

        } else
        {
            // Clear the screen and take the users input
            clear();

            // Print out the sign in prompt
            String[] registeredPrompt = {
                    "Successfully Registered",
                    "Press \"Enter\" To Continue"
            };
            framePrint(registeredPrompt);
            scan.nextLine();
        }

    }

    private static boolean setAccountType(short optionSelected, Account newAccount) {

        switch (optionSelected) {
            case 1:
                newAccount.setType(Account.AccountType.SAVINGS);
                break;
            case 2:
                newAccount.setType(Account.AccountType.CHECKING);
                break;
            case 3:
                newAccount.setType(Account.AccountType.MMA);
                break;
            case 4:
                newAccount.setType(Account.AccountType.CD);
                break;
            case 5:
                newAccount.setType(Account.AccountType.IRA);
                break;
            case 6:
                newAccount.setType(Account.AccountType.BROKERAGE);
                break;
            default:
                return false;
        }
        return true;
    }

    private static void setUniqueID(Account newAccount) {
        StringBuilder uniqueId = new StringBuilder();

        for(User elem : newAccount.getOwners())
        {
            uniqueId.append(elem.getUsername());
        }
        uniqueId.append(new Date());

        newAccount.setActive(true);
        newAccount.setAccountID(hash.getMessageDigest(uniqueId.toString()));
    }
    private static boolean setIsJoint() {
        boolean isJoint = false;
        boolean validInput = false;
        do {
            System.out.print("Will this be a joint Account? Y/n: ");
            String input = scan.nextLine();
            if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                isJoint = true;
                validInput = true;
            }
            else if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")){
                isJoint = false;
                validInput = true;
            }
            else
                System.out.println("\nThat is not a valid input, please select \"Y\" or \"N\"\n");
        }while(!validInput);

        return isJoint;
    }

    private static void setNewOwners(User user, Account newAccount, boolean isJoint) {
        MyArrayList<User> owners = new MyArrayList<>();
        owners.add(user);
        while(isJoint) {
            System.out.println("Please enter the username of the account you wish to add as a co-owner");
            System.out.print("Enter an empty line to stop specifying co-owners: ");
            String input = scan.nextLine();
            if(input.equals("") ) {
                break;
            } else if(!service.usernameIsUnique(input)){
                for(User elem : owners) {
                    if(elem.getUsername().equals(input)) {
                        System.out.println("You have already specified " + input);
                        continue;
                    }
                }
                // add the joint owner to the list of owners
                User jointOwner = new User();
                jointOwner.setUsername(input);
                owners.add(jointOwner);
                System.out.println("Co-owner " + input + " has been specified.\n");
            } else
                System.out.println("\nThat user is not in our system\n");
        }

        newAccount.setOwners(owners);
    }

    static boolean confirmAccount(Account newAccount) {
        while(true)
        {
            System.out.print("\nDo you wish to confirm opening " + newAccount.getType().toString() + " account? (Y/n): ");
            String input = scan.nextLine();
            if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes"))
                return true;
            if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
                return false;
            System.out.println("That is not a valid option, please select \"Y\" or \"N\"");
        }
    }

    /**
     * Lets the user check their available balance
     * @param account - The account to check
     */
    private static void checkBalance(Account account) {
        // Clear the screen and take the users input
        clear();

        // Instructions for the viewing the menu
        String[] instructions = {
                "Account Selected",
                account.getAccountID(),
                "Account Type: " +account.getType().toString(),
                "Current available Balance: " + NumberFormat.getCurrencyInstance(Locale.US)
                        .format(account.getBalance())
        };
        framePrint(instructions);
        scan.nextLine();
    }

    /**
     * Given an account, lets the user make a withdrawal
     * @param account - The account to withdraw from
     */
    private static void withdraw(Account account) {
        // Clear the screen and take the users input
        clear();

        // Instructions for the viewing the menu
        String[] instructions = {
                "To make a withdrawal, enter the amount you wish to withdraw",
                "Current available Balance: " + NumberFormat.getCurrencyInstance(Locale.US)
                        .format(account.getBalance())
        };
        framePrint(instructions);


        System.out.print("Enter the amount: ");
        String input = scan.nextLine();
        while (!input.matches("(0|[1-9][0-9]{0,2})(,\\d{3})*(\\.\\d{2})?")) {
            System.out.println("That is not a valid amount, Please enter an amount in the form of 5,432.10\n");
            System.out.print("Enter the amount: ");
            input = scan.nextLine();
        }

        BigDecimal amountWithdrawn = new BigDecimal(input.replace(",",""));
        if(account.getBalance().compareTo(amountWithdrawn) > -1) {
            if (service.makeWithdrawal(account, amountWithdrawn)) { // The withdrawal is successful
                clear();
                String[] output = {"You successfully withdrew: " +
                        NumberFormat.getCurrencyInstance(Locale.US).format(amountWithdrawn)};
                framePrint(output);
                account.setBalance(account.getBalance().subtract(amountWithdrawn));
                scan.nextLine();
                return;
            }
        }else // There is not enough funds
            {
                System.out.println("You do not have enough funds.");
            }

        System.out.println("The Withdrawal is Unsuccessful please try again.");
        scan.nextLine();
    }

    /**
     * Given an account, lets the user make a deposit
     * @param account - The account to deposit into
     */
    private static void deposit(Account account) {
        // Clear the screen and take the users input
        clear();

        // Instructions for the viewing the menu
        String[] instructions = {
                "To make a deposit, enter the amount you wish to deposit"
        };
        framePrint(instructions);


        System.out.print("Enter the amount: ");
        String input = scan.nextLine();
        while (!input.matches("(0|[1-9][0-9]{0,2})(,\\d{3})*(\\.\\d{2})?")) {
            System.out.println("That is not a valid amount, Please enter an amount in the form of 5,432.10\n");
            System.out.print("Enter the amount: ");
            input = scan.nextLine();
        }

        BigDecimal amountDeposited = new BigDecimal(input.replace(",",""));
        if (service.makeDeposit(account, amountDeposited)) { // The deposit is successful
            clear();
            String[] output = {"You successfully deposited: " +
                    NumberFormat.getCurrencyInstance(Locale.US).format(amountDeposited)};
            framePrint(output);
            account.setBalance(account.getBalance().add(amountDeposited));
            scan.nextLine();
            return;
        }

        System.out.println("The Deposit is Unsuccessful please try again.");
        scan.nextLine();
    }

    /**
     * Given an account, lets the user choose an account to Transfer money to
     * @param user - The user who owns both accounts
     * @param account - The account transferring from
     */
    private static  void internalTransfer(User user, Account account) {

        // Clear the screen and take the users input
        clear();

        // Instructions for the viewing the menu
        String[] instructions = {
                "Internal Account Transfer",
                account.getAccountID(),
                "Available Balance: " +
                        NumberFormat.getCurrencyInstance(Locale.US).format(account.getBalance()),
                "Select the one you wish to transfer money to"
        };


        // Set up the options
        MyArrayList<String> centeredBankAccounts = new MyArrayList<>();
        int longestLength = 0;
        for (Account elem : user.getAccounts()) {
            // Remove the same account from the set of options
            if(elem.getAccountID().equalsIgnoreCase(account.getAccountID()))
                continue;
            // Add the other accounts and keep track of the length to keep the options centered
            String bankAccountOption = (centeredBankAccounts.getCount() + 1) + ") " +
                    elem.getAccountID().substring(0, 7) + "... Type: " + elem.getType();
            centeredBankAccounts.add(bankAccountOption);
            if (longestLength < bankAccountOption.length())
                longestLength = bankAccountOption.length();
        }
        // add an exit option then center the options using spaces as padding
        centeredBankAccounts.add((centeredBankAccounts.getCount() + 1) + ") Exit");
        String spaces = "                                         ";
        for (int i = 0; i < centeredBankAccounts.getCount(); ++i) {

            String centeredString = centeredBankAccounts.get(i);
            int length = centeredString.length();
            if (length < longestLength)
                centeredBankAccounts.replace(i,centeredString + spaces.substring(0, longestLength - length));
        }
        // copy over the options to a String Array of the right size so framed printing can be achieved
        String[] options = new String[centeredBankAccounts.getCount()];
        System.arraycopy(centeredBankAccounts.getData(), 0, options, 0, options.length);


        //  Print out the instructions and options
        topFramePrint(instructions);
        bottomFramePrint(options);

        // Acquire the Account to transfer money to
        short optionSelected = selectOptions((short) options.length);
        while (optionSelected == -1) {
            System.out.println("That is not a valid option, Please select again!");
            optionSelected = selectOptions((short) options.length);
        }
        if (optionSelected == options.length)
            return;

        String accountTo = user.getAccounts().get(--optionSelected).getAccountID();
        if(accountTo.equalsIgnoreCase(account.getAccountID()))
            accountTo = user.getAccounts().get(++optionSelected).getAccountID();


        // Acquire how much money to transfer
        System.out.print("Enter the amount you wish to transfer: ");
        String input = scan.nextLine();
        while (!input.matches("(0|[1-9][0-9]{0,2})(,\\d{3})*(\\.\\d{2})?")) {
            System.out.println("That is not a valid amount, Please enter an amount in the form of 5,432.10\n");
            System.out.print("Enter the amount: ");
            input = scan.nextLine();
        }
        BigDecimal amountTransferred = new BigDecimal(input.replace(",",""));

        // Confirm the transfer
        boolean isValid = false;
        do{
            System.out.println("\nTransfer amount: " +
                    NumberFormat.getCurrencyInstance(Locale.US).format(amountTransferred));
            System.out.println("Transfer From: " + account.getType().toString() + " " + account.getAccountID());
            System.out.println("Transfer To: " + accountTo);
            System.out.print("Confirm the Transfer? (Y/n): ");
            input = scan.nextLine();
            if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                isValid = true;
            }
            else if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
            {
                return;
            }
            else
                System.out.println("That is not a valid option. Please respond with 'Y' or 'N'.");

        }while(!isValid);

        // Transfer has been confirmed, send out the Transfer
        if(service.sendTransaction(new Transaction(new java.sql.Date(System.currentTimeMillis()), amountTransferred,
                account.getAccountID(), accountTo))){
            // set the accounts to have the correct values client-side
            account.setBalance(account.getBalance().subtract(amountTransferred));
            user.getAccounts().get(optionSelected).setBalance(user.getAccounts()
                    .get(optionSelected).getBalance().add(amountTransferred));
            System.out.println("You successfully transferred " +
                    NumberFormat.getCurrencyInstance(Locale.US).format(amountTransferred) +
                    " from your account to the specified account.");
        } else  { // The Transfer was unsuccessful
            System.out.println("\nThe transaction was rejected, if you have enough funds, there could be a hold" +
                    "on your account - contact the John Bank for more information");
        }

        scan.nextLine();
    }

    /**
     * Given an account, lets the user specify an account and an amount to transfer money to
     * @param account - The account that is making a transaction
     */
    private static  void externalTransfer(Account account) {

        // Clear the screen and take the users input
        clear();
        // The instructions to select the type of Transfer
        String[] instructions = {
                "External Transfer from Account: ",
                account.getAccountID(),
                "Balance Available: " +
                        NumberFormat.getCurrencyInstance(Locale.US).format(account.getBalance())
        };
        framePrint(instructions);

        // Acquire the Account to transfer money to
        System.out.print("Input in the account ID: ");
        String input = scan.nextLine();
        input = input.toLowerCase(Locale.US);
        while(!input.matches("[a-f0-9]{64}")) {
            System.out.println("That is incorrect format, please input a user account in this bank's format");
            System.out.println("The format should look like " +
                    "631702663e53b9f1f2d925d3ec20dcc4d685491aae601df7626d15503c99a9b7");

            System.out.print("Input in the account ID: ");
            input = scan.nextLine();
        }
        String accountTo = input;

        // Acquire how much money to transfer
        System.out.print("Enter the amount you wish to transfer: ");
        input = scan.nextLine();
        while (!input.matches("(0|[1-9][0-9]{0,2})(,\\d{3})*(\\.\\d{2})?")) {
            System.out.println("That is not a valid amount, Please enter an amount in the form of 5,432.10\n");
            System.out.print("Enter the amount: ");
            input = scan.nextLine();
        }
        BigDecimal amountTransferred = new BigDecimal(input.replace(",",""));

        // Confirm the transfer
        boolean isValid = false;
        do{
            System.out.println("\nTransfer amount: " +
                    NumberFormat.getCurrencyInstance(Locale.US).format(amountTransferred));
            System.out.println("Transfer From: " + account.getType().toString() + " " + account.getAccountID());
            System.out.println("Transfer To: " + accountTo);
            System.out.print("Confirm the Transfer? (Y/n): ");
            input = scan.nextLine();
            if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                isValid = true;
            }
            else if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
            {
                return;
            }
            else
                System.out.println("That is not a valid option. Please respond with 'Y' or 'N'.");

        }while(!isValid);

        // Transfer has been confirmed, send out the Transfer
        if(service.sendTransaction(new Transaction(new java.sql.Date(System.currentTimeMillis()), amountTransferred,
                account.getAccountID(), accountTo))){
            account.setBalance(account.getBalance().subtract(amountTransferred));
            System.out.println("You successfully transferred " +
                    NumberFormat.getCurrencyInstance(Locale.US).format(amountTransferred) +
                    " from your account to the specified account.");
        } else  { // The Transfer was unsuccessful
            System.out.println("\nThe transaction was rejected, if you have enough funds, there could be a hold" +
                    "on your account - contact the John Bank for more information");
        }

        scan.nextLine();
    }

    /**
     * Given an account, lets the user transfer money
     * @param account - The account to withdraw from
     */
    private static void transfer(User user, Account account) {
        // Clear the screen and take the users input
        clear();

        if(user.getAccounts().getCount() > 1) {
            // The instructions to select the type of Transfer
            String[] instructions = {
                    "Would you like to internally transfer funds",
                    "between accounts or externally transfer funds?",
                    "1) Internal Transfer",
                    "2) External Transfer"
            };
            framePrint(instructions);


            short optionSelected = selectOptions((short) 2);
            while (optionSelected == -1) {
                System.out.println("That is not a valid option, Please select again!");
                optionSelected = selectOptions((short) 2);
            }

            if(optionSelected == 1)
                internalTransfer(user, account);
            else
                externalTransfer(account);
        }
        else
            externalTransfer(account);
    }

    private static void addOwner(Account account) {
        // Clear the screen and take the users input
        clear();

        // The instructions to select the type of Transfer
        String[] instructions = {
                "Please specify the username(s) of registered user(s) to be",
                "the new co-owners for account " +
                        account.getAccountID().substring(0, 7) + "... Type: " + account.getType(),
                "Enter a blank line to stop"
        };
        framePrint(instructions);

        MyLinkedList<User> newOwners = new MyLinkedList<>();
        while(true) {
            String input = scan.nextLine();
            if(input.equals("") ) {
                break;
            } else if(!service.usernameIsUnique(input)){
                boolean skipAdd = false;
                for(User owner : newOwners) {
                    if(owner.getUsername().equals(input)) {
                        System.out.println("You have already specified " + input);
                        skipAdd = true;
                        break;
                    }
                }
                for(User owner : account.getOwners()) {
                    if(owner.getUsername().equals(input)){
                        System.out.println(input + " already owns this account.");
                        skipAdd = true;
                        break;
                    }
                }
                if(skipAdd)
                    continue;
                // add the joint owner to the list of owners
                User jointOwner = new User();
                jointOwner.setUsername(input);
                newOwners.add(jointOwner);
                System.out.println("Co-owner " + input + " has been specified.\n");
            } else
                System.out.println("That user is not in our system");
        }

        // if no one has been specified, leave the menu
        if(newOwners.isEmpty())
            return;

        // print out the members specified before confirming
        System.out.print("You have specified the members:\n");
        Iterator<User> newOwnersIt = newOwners.iterator();
        while(newOwnersIt.hasNext()) {
            System.out.print(newOwnersIt.next().getUsername());
            if(newOwnersIt.hasNext())
                System.out.print(", ");
        }

        // Confirm adding the members
        boolean isValid = false;
        String input;
        do{
            System.out.print("\nConfirm adding these co-owners? (Y/n): ");
            input = scan.nextLine();
            if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                isValid = true;
            }
            else if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
            {
                return;
            }
            else
                System.out.println("That is not a valid option. Please respond with 'Y' or 'N'.");
        }while(!isValid);

        for(User owner : newOwners) {
            try{
                service.makeOwner(account, owner);}
            catch (ResourceNotFoundException e) {
                throw new RuntimeException("Something is wrong, an Account doesn't exist that should!", e);
            }
        }

    }

    /**
     * Given an Account show the Transactions to the user
     * @param account - The account used to print the Transactions
     */
    private static void viewTransactions(Account account) {

        boolean listEnd = false;
        int count = 0;
        MyLinkedList<Transaction> transactions = service.getAllTransactions(account);
        Iterator<Transaction> it =  transactions.iterator();

        while(it.hasNext()) {
            // Clear the screen and take the users input
            clear();

            // Instructions for the viewing the menu
            String[] topInstructions = {
                    "Account ID:",
                    account.getAccountID(),
                    "Showing Recent Transactions Page: " + ++count,
            };
            // Instructions for the viewing the menu
            String[] bottomInstructions = {
                    "Press Enter to view next page",
                    "Input \"Exit\" to leave Transaction Menu"
            };

            // Print the top of the screen
            topFramePrint(topInstructions);
            // Print out the Transactions
            for(int i = 0; i < 4 && it.hasNext(); ++i)
            {
                System.out.println(it.next().toString());
            }
            // Print the bottom of the screen
            bottomFramePrint(bottomInstructions);

            if(scan.nextLine().equalsIgnoreCase("exit"))
                return;
        }

        // Instructions for the viewing the menu
        String[] instructions = {
                "Account ID:",
                account.getAccountID(),
                "There are no more Transactions to show",
                "Press Enter to exit the menu"
        };
        // Print the top of the screen
        topFramePrint(instructions);

        scan.nextLine();
    }

    /**
     * Once a user selects an account to access, the app gives them options on what they can do
     * @param account - The account being accessed
     */
    private static void accessAccount(User user, Account account) {
        while(true) {
            // Clear the screen and take the users input
            clear();

            // Instructions for the viewing the menu
            String[] instructions = {
                    "Account ID:",
                    account.getAccountID(),
                    "What Do you wish to do with your account?" ,
                    "Enter the number of the option you wish to choose"};
            String[] options = {
                    "1) Check Balance                ",
                    "2) Make a Withdrawal            ",
                    "3) Make a Deposit               ",
                    "4) Make a Transfer              ",
                    "5) Add a Co-Owner               ",
                    "6) View Transactions            ",
                    "7) Exit                         "
            };
            topFramePrint(instructions);
            framePrint(options);

            short optionSelected = selectOptions((short) 7);
            while (optionSelected == -1) {
                System.out.println("That is not a valid option, Please select again!");
                optionSelected = selectOptions((short) 7);
            }

            switch(optionSelected){
                case 1:
                    checkBalance(account);
                    break;
                case 2:
                    withdraw(account);
                    break;
                case 3:
                    deposit(account);
                    break;
                case 4:
                    transfer(user, account);
                    break;
                case 5:
                    addOwner(account);
                    break;
                case 6:
                    viewTransactions(account);
                    break;
                case 7:
                    return;
            }
        }
    }

    /**
     * Once a user has successfully logged in, allows said user to view and access their accounts
     * @param user - The user that has logged in
     */
    private static void viewAccounts(User user) {

        // Clear the screen and take the users input
        clear();

        // Instructions for the viewing the menu
        String[] instructions = {
                "Here is a list of your accounts",
                "Select the one you wish to access"
        };


        // Set up the options
        MyArrayList<String> centeredBankAccounts = new MyArrayList<>();
        int longestLength = 0;
        for (Account account : user.getAccounts()) {
            String bankAccountOption = (centeredBankAccounts.getCount() + 1) + ") " +
                    account.getAccountID().substring(0, 7) + "... Type: " + account.getType();
            centeredBankAccounts.add(bankAccountOption);
            if (longestLength < bankAccountOption.length())
                longestLength = bankAccountOption.length();
        }
        centeredBankAccounts.add((centeredBankAccounts.getCount() + 1) + ") Exit");
        String spaces = "                                         ";
        for (int i = 0; i < centeredBankAccounts.getCount(); ++i) {

            String centeredString = centeredBankAccounts.get(i);
            int length = centeredString.length();
            if (length < longestLength)
                centeredBankAccounts.replace(i,centeredString + spaces.substring(0, longestLength - length));
        }

        String[] options = new String[centeredBankAccounts.getCount()];
        System.arraycopy(centeredBankAccounts.getData(), 0, options, 0, options.length);


        //  Print out the instructions and options
        topFramePrint(instructions);
        bottomFramePrint(options);


        short optionSelected = selectOptions((short) options.length);
        while (optionSelected == -1) {
            System.out.println("That is not a valid option, Please select again!");
            optionSelected = selectOptions((short) options.length);
        }
        if (optionSelected == options.length)
            return;

        accessAccount(user, user.getAccounts().get(--optionSelected));

    }

    static void openAccount(User user) {
        // Clear the screen and take the users input
        clear();

        // Instructions for the main menu
        String[] instructions = {
                "What Kind of Account do you wish to Open?",
                "Enter the number of the option you wish to choose",
                "1) Savings                      ",
                "2) Checking                     ",
                "3) Money Market                 ",
                "4) Certificate of Deposit       ",
                "5) Individual Retirement Account",
                "6) Brokerage Account            ",
                "7) Exit                         "
        };
        framePrint(instructions);
        short optionSelected = selectOptions((short)7);
        while(optionSelected == -1) {
            System.out.println("That is not a valid option, Please select again!");
            optionSelected = selectOptions((short)7);
        }

        if(optionSelected == 7)
            return;

        Account newAccount = new Account();
        newAccount.setBalance(new BigDecimal(0));

        if (!setAccountType(optionSelected, newAccount))
            return;
        boolean isJoint = setIsJoint();
        setNewOwners(user, newAccount, isJoint);
        setUniqueID(newAccount);

        if(confirmAccount(newAccount)) {
            service.addAccount(newAccount);
            for(User owner : newAccount.getOwners()) {
                try{
                service.makeOwner(newAccount, owner);}
                catch (ResourceNotFoundException e) {
                    throw new RuntimeException("Something is wrong, an Account doesn't exist that should!", e);
                }
            }
            user.getAccounts().add(newAccount);

            String[] confirmationMessage = {
                    "Congratulations! you have opened a new Account",
                    "Press Enter to Continue"
            };
            framePrint(confirmationMessage);
            scan.nextLine();
        }

    }

    /**
     * Given an Account show the Transactions to the user
     * @param user - The account used to print the Transactions
     */
    private static void viewTransactions(User user) {

        boolean listEnd = false;
        int count = 0;
        MyLinkedList<Transaction> transactions = service.getAllTransactions(user);
        Iterator<Transaction> it =  transactions.iterator();

        while(it.hasNext()) {
            // Clear the screen and take the users input
            clear();

            // Instructions for the viewing the menu
            String[] topInstructions = {
                    "Username:",
                    user.getUsername(),
                    "Showing Recent Transactions Page: " + ++count,
            };
            // Instructions for the viewing the menu
            String[] bottomInstructions = {
                    "Press Enter to view next page",
                    "Input \"Exit\" to leave Transaction Menu"
            };

            // Print the top of the screen
            topFramePrint(topInstructions);
            // Print out the Transactions
            for(int i = 0; i < 3 && it.hasNext(); ++i)
            {
                System.out.println(it.next().toString());
            }
            // Print the bottom of the screen
            bottomFramePrint(bottomInstructions);

            if(scan.nextLine().equalsIgnoreCase("exit"))
                return;
        }

        // Instructions for the viewing the menu
        String[] instructions = {
                "Account ID:",
                user.getUsername(),
                "There are no more Transactions to show",
                "Press Enter to exit the menu"
        };
        // Print the top of the screen
        topFramePrint(instructions);

        scan.nextLine();
    }

    static void userMenu(User user) {
        while(true) {
            // Clear the screen and take the users input
            clear();

            // Instructions for the main menu
            String[] instructions = {
                    "Enter the number of the option you wish to choose",
                    "1) View Account            ",
                    "2) Open an Account         ",
                    "3) View Recent Transactions",
                    "4) Quit                    "
            };
            framePrint(instructions);
            short optionSelected = selectOptions((short) 4);
            while (optionSelected == -1) {
                System.out.println("That is not a valid option, Please select again!");
                optionSelected = selectOptions((short) 4);
            }

            switch (optionSelected) {
                case 1:
                    viewAccounts(user);
                    break;
                case 2:
                    openAccount(user);
                    break;
                case 3:
                    viewTransactions(user);
                    break;
                default:
                    return;
            }
        }
    }

    /**
     * The Sign-In Menu of the Application
     */
    static void signIn(){
        String username;            // The user's username
        String password ;           // The user's password
        short attempts = 0;         // The amount of attempts the user has tried
        User user = null;           // The user's info once logged in

        // Clear the screen and take the users input
        clear();

        // Print out the sign in prompt
        String[] signInPrompt = {
                "Please Sign In"
        };
        framePrint(signInPrompt);

        while(attempts++ < 5) {
            System.out.print("Username: ");
            username = scan.nextLine();
            // if a console is attached, make the password secure
            if (System.console() != null) {
                char[] charPass = System.console().readPassword("Password: ");
                password = hash.getMessageDigest(new String(charPass));
            } else {
                System.out.print("Password: ");
                password = hash.getMessageDigest(scan.nextLine());
            }

            user = service.logUserIn(username, password);
            if(user != null)
                break;
            System.out.println("Either The Username Or The Password Is Incorrect. Try again.\n");
        }

        if(user == null)
            System.out.println("You have exceeded the log-in attempts for this session, please try again later.");
        else
            userMenu(user);
    }

    /**
     * Main menu of the Application
     */
    public static void mainMenu(){
        // Clear the screen and take the users input
        clear();

        // Instructions for the main menu
        String[] instructions = {
                "Enter the number of the option you wish to choose",
                "1) Sign In              ",
                "2) Resister an Account  ",
                "3) Quit                 ",
                "4) Shut Down Application"
        };
        framePrint(instructions);
        short optionSelected = selectOptions((short)4);
        while(optionSelected == -1) {
            System.out.println("That is not a valid option, Please select again!");
            optionSelected = selectOptions((short)4);
        }

        switch (optionSelected) {
            case 1:
                signIn();
                break;
            case 2:
                register();
                break;
            case 3:
                clear();
                return;
            case 4:
                running = false;
        }

    }

    /**
     * Given a number of options, restricts the user to only select
     * an option that is in bounds
     * @param count number of options there are
     * @return the option the user took
     */
    private static short selectOptions(short count){

        short input;
        try{
            input = Short.parseShort(scan.nextLine());
        }
        catch (NumberFormatException e){
            return -1;
        }

        if(input < 1 || input > count)
            return -1;
        return input;
    }
}
