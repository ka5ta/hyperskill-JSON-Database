package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
            while (!controller.isShuttingDown()) {
                Socket acceptedSocket = server.accept();
                Session session = new Session(acceptedSocket, server, controller); // accepting a new client
                session.start();
            }
        } catch (SocketException e) {
            System.out.println("Server socket closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}











