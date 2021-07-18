package main.java.com.company.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.com.company.dm.DataModel;
import main.java.com.company.services.CacheUnitController;


import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;

/**
 *get the request from server, read the data from it. the data will be in Jsom format.
 * raerd the HEADER request and move it to the relevant method in CacheUnitController
 * @param <T>
 */
public class HandelRequest<T> extends Thread implements Runnable {
    private final Socket socket;
    private final CacheUnitController<T> controller;

    /**
     * constructor
     * @param s socket to connect
     * @param controller handle the request with
     */
    public HandelRequest(Socket s, CacheUnitController<T> controller) {
        this.socket = s;
        this.controller = controller;
    }

    @Override
    /**
     * Reads the information that comes in Json format,
     * reads the HEADER and sends to the appropriate method
     * write the return value to output
     */
    public void run() {
        try {
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            Gson gson = new Gson();
            DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            //get all data from client
            String content = "";
            do {
                content = reader.readUTF();
                sb.append(content);
            } while (reader.available() != 0);
            content = sb.toString();
            Type requestType = new TypeToken<Request<DataModel<T>[]>>() {
            }.getType();
            //Converter to the appropriate object
            Request<DataModel<T>[]> request = new Gson().fromJson(content, requestType);
            DataModel<T>[] returnValues;
            //command - action to do in request
            String command = request.getHeaders().get("action");
            boolean res = true;
            String stats = "";
            switch (command) {
                case "GET":
                    returnValues = controller.get(request.getBody());
                    writer.writeUTF(gson.toJson(returnValues));
                    writer.flush();
                    break;
                case "UPDATE":
                    res=controller.update(request.getBody());
                    writer.writeUTF(gson.toJson(res));
                    writer.flush();
                    break;
                case "DELETE":
                    res=controller.delete(request.getBody());
                    writer.writeUTF(gson.toJson(res));
                    writer.flush();
                    break;
                case "STATS":
                    stats=controller.getStatistics();
                    writer.writeUTF(stats);
                    writer.flush();
                default:
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
