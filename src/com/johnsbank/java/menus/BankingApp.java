package com.johnsbank.java.menus;

import com.johnsbank.java.services.BankService;
import com.johnsbank.java.services.BankServiceImplementation;

import java.util.Scanner;                                                    // The scanner for input
import static com.johnsbank.java.menus.ScreenPrinter.*;                      // All the functions to print to the screen
import static com.johnsbank.java.menus.MenuInterface.*;                      // Controls the menu selection
//import static com.johnsbank.java.services.DatabaseCommunication.isConnected; // Returns the connected state of the DB

final class BankingApp {

    static boolean running = true;                         // Whether the app is running or not
    static final Scanner scan = new Scanner(System.in);    // The scanner for input


    /**
     * The entry point to our program, welcomes our users and
     * continues execution until shutdown
     * @param args The command line arguments
     */
    public static void main(String[] args) {

        BankService service = BankServiceImplementation.getInstance();
        while (running) {

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
                // The message greeting the user to the application
                String[] welcomeMessage = {
                        "Connection to the Database has been lost!",
                        "Do you have the proper authority? Is the network down?",
                        "Enter \"Exit\" to shutdown or Press Enter to retry.",
                };

                framePrint(welcomeMessage);
                if(scan.nextLine().equalsIgnoreCase("exit"))
                    running = false;
                else
                    service.isConnected();
            }
        }

        scan.close();
    }

}
