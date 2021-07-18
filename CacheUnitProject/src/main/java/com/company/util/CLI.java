package main.java.com.company.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;


/**
 * This department will be responsible for the interface with the client
 * in order to activate the server and stop its operation if necessary.
 */
public class CLI implements Runnable {
    private final Scanner input;
    private final PrintWriter output;
    private final PropertyChangeSupport changes;
    private String command;

    /**
     * constructor
     * @param in
     * @param out
     */
    public CLI(InputStream in, OutputStream out) {
        input = new Scanner(in);
        output = new PrintWriter(out);
        changes = new PropertyChangeSupport(this);

    }

    /**
     * add event to property list and updates who is waiting for the event
     * @param pcl - event
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.changes.addPropertyChangeListener(pcl);
    }

    /**
     * remove event from list
     * @param pcl - event to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        this.changes.removePropertyChangeListener(pcl);
    }

    /**
     * write given string to output
     * @param string - string to write
     */
    public void write(String string) {
        output.write(string);
        output.flush();
    }

    @Override
    /**
     *get command from input - start or stop, and add it as event to firePropertyChange
     * if event is not valid a massage will appear.
     */
    public void run() {
        while (true) {
            output.write("Please enter your command\n");
            output.flush();
            String clientCommand = input.nextLine().toLowerCase();
            if (clientCommand.equals("start")) {
                manageCommand(clientCommand, "Starting server...\n");
            } else if (clientCommand.matches("shutdown|stop")) {
                manageCommand(clientCommand, "Shutdown server\n");
                return;
            } else {
                output.write("Not a valid command\n");
            }
        }
    }

    /**
     * add value to property list and print to output massage
     * @param value - event to add
     * @param outputString - massage to print
     */
    private void manageCommand(String value, String outputString) {
        this.command = value;
        output.write(outputString);
        output.flush();
        changes.firePropertyChange("action", null, value);
    }
}
