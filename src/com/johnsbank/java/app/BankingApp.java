package com.johnsbank.java.app;

import com.johnsbank.java.services.BankService;                              // Interface to the banking service
import com.johnsbank.java.services.BankServiceImplementation;                // Instance of the Banking Service

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;                                                    // The scanner for input
import static com.johnsbank.java.menus.ScreenPrinter.*;                      // All the functions to print to the screen
import static com.johnsbank.java.menus.MenuImplementation.*;                 // Controls the menu selection

public final class BankingApp {

    public static boolean running = true;                                    // Whether the app is running or not
    public static final Scanner scan = new Scanner(System.in);               // The scanner for input


    /**
     * The entry point to our program, welcomes our users and
     * continues execution until shutdown
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        
        // Instantiates the instance to the banking service
        BankService service = BankServiceImplementation.getInstance();
        while (running) {

            clear();
            if(service.isConnected()) {
                // The message greeting the user to the application
                String[] welcomeMessage = {
                        "Welcome to The John Banking App",
                        "Where we take care of all your banking needs!",
                        "Press \"Enter\" to continue",
                };

                framePrint(welcomeMessage);
                scan.nextLine();
                mainMenu();
            }
            else {
                // The message used if the connection to the database is no longer established
                String[] disconnectedMessage = {
                        "Connection to the Database has been lost!",
                        "Do you have the proper authority? Is the network down?",
                        "Enter \"Exit\" to shutdown or Press Enter to retry.",
                };

                framePrint(disconnectedMessage);
                if(scan.nextLine().equalsIgnoreCase("exit"))
                    running = false;
                else
                    service.isConnected();
            }
        }

        scan.close();
    }

}
