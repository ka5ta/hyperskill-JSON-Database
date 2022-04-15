package server;

import server.Database.DataStorage;
import server.Model.Controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Main {

    final static int PORT = 23456;

    public static void main(String[] args) {

        DataStorage jsonDatabase = new DataStorage();
        runServer(jsonDatabase);
    }

    private static void runServer(DataStorage jsonDatabase) {
        Controller controller = new Controller(jsonDatabase);

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started!");
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











