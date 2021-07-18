package main.java.com.company.server;

import main.java.com.company.services.CacheUnitController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.EventListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * hole ServerSocket object, listen in port 12345 and manage communication with customers.
 */
public class Server implements PropertyChangeListener ,Runnable, EventListener {
    private static final int NUM_THREADS = 10;
    private ServerSocket server;
    private Socket socket;
    private Boolean serverUp;
    private CacheUnitController<String> controller;
    ExecutorService executor ;


    /**
     * constructor
     */
    public Server() {
        controller = new CacheUnitController<String>();
        try {
            server  = new ServerSocket(12345);
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverUp = false;
        executor = Executors.newFixedThreadPool(NUM_THREADS);

    }

    @Override
    /**
     * get relevant commands from CLI and is conducted according to them
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String action = (String) evt.getNewValue();
        if(action.equals("start")){
            if(!serverUp){
                serverUp = true;
                new Thread(this).start();
            }else{
                System.out.println("server already ON");
            }

        }else if(action.matches("stop|shutdown")){
            if(serverUp){
                serverUp = false;
                controller.stop();
            }else{
                System.out.println("server already OFF");
            }
        }

    }


    @Override
    /**
     * when server up wait for connection with client and create thread to handle with the request
     * otherwise close the socket and waite to all threads to finish
     */
    public void run() {
            while (serverUp){
                try {
                    socket = server.accept();
                    if(!serverUp){
                        try {
                            server.close();
                            executor.shutdown();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else{
                        executor.execute(new HandelRequest<String>(socket,controller));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

    }
}
