package com.johnsbank.java.menus;


import java.io.IOException; // Thrown when reading from input stream

/**
 * The class that holds the functions used to print to screen
 */
final class ScreenPrinter {

    /**
     * Clears the screen
     */
    static void clear() {

        // Spam the backspace key to clear the console
        for(int i = 0; i < 1000; i++)
        {
            System.out.println("\b");
        }
        System.out.flush();

        // Clear the input stream so Scanner doesn't pick up all those backspaces
        try {
            System.in.read(new byte[System.in.available()]);
        }
        catch (IOException e){
            System.err.println("Caught an IO Exception, Something is wrong with the input stream!");
            e.printStackTrace();
            System.exit(-30);
        }
    }

    /**
     * A helper method that allows for fancier printing
     * it prints out a number of framed filler lines
     * @param count the number of framed filler lines
     */
    private static void printFramedFiller(short count){

        for(int i = 0; i < count; ++i) {
            System.out.print('|');
            for (int j = 0; j < 78; ++j)
                System.out.print("*");
            System.out.println('|');
        }

    }

    /**
     * A helper method that allows for fancier printing
     * it centers the message and adds padding for framed messages
     * @param messages The messages to be printed
     */
    private static void centerFramedPrints(String[] messages){

        for (String message : messages) {

            int len = message.length(); // the length of the message to center
            if (len > 78) {            // if the length is over 78, just print out message
                System.out.println(message);
                continue;
            }

            StringBuilder out = new StringBuilder("|");            // the string to print

            if(len <= 70) {              // if message can be padded with spaces do so
                out.append("****");
                int count = (70 - len) / 2;
                while (count-- > 0)
                    out.append(' ');
                out.append(message);
                while (out.length() < 75)
                    out.append(' ');
                out.append("****");
            }
            else {                       // if message cannot be padded don't
                int count = (78 - len) / 2;
                while (count-- > 0)
                    out.append('*');
                out.append(message);
                while (out.length() < 79)
                    out.append('*');
            }

            out.append("|");
            System.out.println(out);  // print out the centered message
        }
    }

    /**
     * A helper method that allows for fancier printing
     * it centers the message and adds padding
     * @param message The message to be printed
     * @param filler The padding used on the console
     */
    static void centerPrints(String message, char filler){

        // the length of the message to center
        int len = message.length();
        // the string to print
        StringBuilder out = new StringBuilder();

        // if the length is over 80, just print out message
        if(len >= 80)
            System.out.println(message);
        else   // if the message can be centered, center it
        {
            int count = (80 - len) / 2;
            while(count-- > 0)
                out.append(filler);
            out.append(message);
            while (out.length() < 80)
                out.append(filler);
        }

        // print out the centered message
        System.out.println(out);
    }

    /**
     * Prints the messages to be printed inside the frame
     * @param msg The message or set of messages to be framed
     */
    static void framePrint(String[] msg) {

        // Print the top bar of the frame
        System.out.print('|');
        for(int i = 0; i < 78; ++i)
            System.out.print("=");
        System.out.println('|');

        // print out the framed filler lines
        printFramedFiller((short)1);
        // print out the message
        centerFramedPrints(msg);
        // print out the framed filler lines
        printFramedFiller((short)1);

        // Print the bottom bar of the frame
        System.out.print('|');
        for(int i = 0; i < 78; ++i)
            System.out.print("=");
        System.out.println('|');
    }
}
