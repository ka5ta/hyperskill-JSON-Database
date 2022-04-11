package client;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class Main {
    final static String HOST = "127.0.0.1";
    final static int PORT = 23456;

    public static void main(String[] args) {

        MessageDTO messageDTO = getMessage(args);

        Gson gson = new Gson();
        String jsonMessage = gson.toJson(messageDTO);
        sendMessageToServer(jsonMessage);

    }

    private static MessageDTO getMessage(String[] args){
        String type = null;
        String index = null;
        String text = null;

        for (int i = 1; i < args.length; i++) {
            switch(args[i-1]){
                case "-t":
                    type = args[i];
                    break;
                case "-k":
                    index = args[i];
                    break;
                case "-v":
                    text = args[i];
            }
        }

        if(Objects.isNull(type)){
            throw new NullPointerException("Type is null");
        }

        return new MessageDTO(type, index, text);
    }

    private static void sendMessageToServer(String messageToSend) {
        System.out.println("Client started!");
        try (
                Socket socket = new Socket(HOST, PORT);
                DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())
        ) {
            dataOut.writeUTF(messageToSend);  // sending msg to the server
            System.out.println("Sent: " + messageToSend);
            String received = dataIn.readUTF(); // response msg
            System.out.println("Received: " + received);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
