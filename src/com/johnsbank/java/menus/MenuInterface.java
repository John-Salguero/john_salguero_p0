package com.johnsbank.java.menus;

import com.johnsbank.java.accountdata.Account;
import com.johnsbank.java.accountdata.User;
import com.johnsbank.java.services.BankService;
import com.johnsbank.java.services.BankServiceImplementation;
import com.johnsbank.java.utilities.MyArrayList;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Locale;

import static com.johnsbank.java.menus.ScreenPrinter.*;    // All the functions to print to the screen
import static com.johnsbank.java.menus.BankingApp.running; // Whether the app is running or not
import static com.johnsbank.java.menus.BankingApp.scan;    // Scanner for input

/**
 * Used to control the flow of user experience, menu selection
 */
final class MenuInterface {

    private static final BankService service = BankServiceImplementation.getInstance();

    private static final char[] HEX_ARRAY = { // Used for transforming the Hash byte data into human-readable form
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'};

    /**
     * Used with the command pattern to accept lambdas
     */
    private interface Validator {
        boolean validate(String input);
    }

    /**
     * Transforms the given byte array into a hexadecimal number in string format
     * @param bytes the byte array to transform
     * @return a hexadecimal representation of the array in string format
     */
    public static String bytesToHex(byte[] bytes) {
        // array of characters in ascii form representing the data in byte array
        char[] hexChars = new char[bytes.length * 2];

        // convert each byte into a hexadecimal value and convert that into ascii format
        for (int j = 0; j < bytes.length; j++) {
            int curByte = bytes[j];
            // bit-shift the byte by 4 to retrieve the higher WORD and use that as an index into the array to convert
            hexChars[j * 2] = HEX_ARRAY[(curByte >>> 4) & 0x0F];
            // AND the lower WORD by 16 to isolate it and use that as an index into the array to convert
            hexChars[j * 2 + 1] = HEX_ARRAY[curByte & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * The method that securely Hashes User's passwords
     */
    private static String getMessageDigest(String pass){

        MessageDigest digestAlg = null; // The digest object used

        // Hash the password to securely save and compare
        try {
            digestAlg = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: SHA-256 could not be used!", e);
        }

        byte[] hash = digestAlg.digest(pass.getBytes(StandardCharsets.UTF_8));


        return bytesToHex(hash);
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
            System.out.println(" Please enter a password of at least 8 characters with at least 1 capital letter," +
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

            passHash = getMessageDigest(password);
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
            SSNHash = getMessageDigest(SSN);

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
                System.out.println("Address Line 2: " + address2);
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
        newAccount.setAccountID(getMessageDigest(uniqueId.toString()));
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
        newAccount.getOwners().add(user);
        while(isJoint) {
            System.out.print("Please enter the username of the account you wish to add as a co-owner");
            System.out.print("Enter an empty line to stop specifying co-owners");
            String input = scan.nextLine();
            if(input.equals("") ) {
                break;
            } else if(!service.usernameIsUnique(input)){
                for(User elem : newAccount.getOwners()) {
                    if(elem.getUsername().equals(input)) {
                        System.out.println("You have already specified " + input);
                        continue;
                    }
                    // add the joint owner to the list of owners
                    User jointOwner = new User();
                    jointOwner.setUsername(input);
                    newAccount.getOwners().add(jointOwner);
                }
            } else
                System.out.println("\nThat user is not in our system\n");
        }
    }

    static boolean confirmAccount(Account newAccount) {
        while(true)
        {
            System.out.print("\n\nDo you wish to confirm opening " + newAccount.getType().toString() + " account? (Y/n): ");
            String input = scan.nextLine();
            if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes"))
                return true;
            if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
                return false;
            System.out.println("That is not a valid option, please select \"Y\" or \"N\"");
        }
    }

    private static void viewAccounts(User user) {

        while(true) {
            // Clear the screen and take the users input
            clear();

            // Instructions for the viewing the menu
            String[] instructions = {
                    "Here is a list of your accounts",
                    "Select the one you wish to view"
            };
            framePrint(instructions);

            MyArrayList<String> centeredBankAccounts = new MyArrayList<>();
            int longestLength = 0;
            for (Account account : user.getAccounts()) {
                String bankAccountOption = (centeredBankAccounts.getCount() + 1) + ". " + account.getAccountID().substring(0, 7) + "... Type: " + account.getType();
                centeredBankAccounts.add(bankAccountOption);
                if (longestLength < bankAccountOption.length())
                    longestLength = bankAccountOption.length();
            }
            centeredBankAccounts.add((centeredBankAccounts.getCount() + 1) + ". Exit");
            String spaces = "                     ";
            for (String centeredStrings : centeredBankAccounts) {
                int length = centeredStrings.length();
                if (length < longestLength)
                    centeredStrings = centeredStrings + spaces.substring(0, longestLength - length);
            }

            String[] options = new String[centeredBankAccounts.getCount()];
            System.arraycopy(centeredBankAccounts.getData(), 0, options, 0, options.length);

            framePrint(options);
            short optionSelected = selectOptions((short) options.length);
            while (optionSelected == -1) {
                System.out.println("That is not a valid option, Please select again!");
                optionSelected = selectOptions((short) options.length);
            }

            if (optionSelected == options.length)
                return;

            //useAccount();

        }

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
        short optionSelected = selectOptions((short)6);
        while(optionSelected == -1) {
            System.out.println("That is not a valid option, Please select again!");
            optionSelected = selectOptions((short)6);
        }
        Account newAccount = new Account();
        newAccount.setBalance(new BigDecimal(0));

        if (!setAccountType(optionSelected, newAccount))
            return;
        boolean isJoint = setIsJoint();
        setNewOwners(user, newAccount, isJoint);
        setUniqueID(newAccount);

        if(confirmAccount(newAccount)) {
            service.addAccount(newAccount);
            user.getAccounts().add(newAccount);

            String[] confirmationMessage = {
                    "Congratulations! you have opened a new Account",
                    "Press Enter to Continue"
            };
            framePrint(confirmationMessage);
            scan.nextLine();
        }

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
                    //viewTransactions(user);
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
                password = getMessageDigest(new String(charPass));
            } else {
                System.out.print("Password: ");
                password = getMessageDigest(scan.nextLine());
            }

            user = service.logUserIn(username, password);
            if(user != null)
                break;
        }

        if(user == null)
        {
            System.out.println("You have exceeded the log-in attempts for this session, please try again later.");
        }
        userMenu(user);
    }

    /**
     * Main menu of the Application
     */
    static void mainMenu(){
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
