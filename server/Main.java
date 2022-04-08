package server;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;

public class Main {

    final static int PORT = 23456;

    public static void main(String[] args) {

        DataStorage textStore = new DataStorage();
        runServer(textStore);
    }


    private static void runServer(DataStorage storage) {
        System.out.println("Server started!");
        Controller controller = new Controller(storage);
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Session session = new Session(server.accept(), controller); // accepting a new client
                session.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}











