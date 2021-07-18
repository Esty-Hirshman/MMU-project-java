package main.java.com.mbj.client;

import java.io.*;
import java.net.Socket;


/**
 * This class creates communication to the server through the socket the port is 12345 and IP is localhost
 */
public class CacheUnitClient {
    private Socket socket;
    private int port;

    public CacheUnitClient() {
        this.port = 12345;
    }

    /**
     * send data to server and get the response from server and return it.
     * if server is not on return - Connection refused
     * @param request to server from client
     * @return response from server
     */
    public String send(String request) {
        String res = "Empty";
        try {
            socket = new Socket("127.0.0.1",port);
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            writer.writeUTF(request);
            writer.flush();
            do {
                res = reader.readUTF();
                sb.append(res);
            } while (reader.available() != 0);
            res = sb.toString();
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            res = "Connection refused";
        }
        return res;
    }


}
