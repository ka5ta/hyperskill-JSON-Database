package client;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;


public class Main {
    final static String HOST = "127.0.0.1";
    final static int PORT = 23456;

    public static void main(String[] args) {
        sendCommandToServer(buildJsonCommand(args));
    }

    private static void sendCommandToServer(JsonObject json) {
        System.out.println("Client started!");
        try (
                Socket socket = new Socket(HOST, PORT);
                DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())
        ) {

            //Convert JsonObject to String message
            String commandToSend = new Gson().toJson(json);

            //Sending msg to server
            dataOut.writeUTF(commandToSend);
            System.out.println("Sent: " + commandToSend);

            //Response msg received
            String received = dataIn.readUTF();
            System.out.println("Received: " + received);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonObject buildJsonCommand(String[] args) {
        JsonObject jsonObject = new JsonObject();

        for (int i = 1; i < args.length; i++) {
            switch (args[i - 1]) {
                case "-t":
                    jsonObject.addProperty("type", args[i]);
                    break;
                case "-k":
                    jsonObject.addProperty("key", args[i]);
                    break;
                case "-v":
                    jsonObject.addProperty("value", args[i]);
                    break;
                case "-in":
                    return jsonFromFile(args[i]);
                default:
                    break;
            }
        }
        return jsonObject;
    }

    public static JsonObject jsonFromFile(String fileName) {
        File commandFile = Paths.get(".", "src", "client", "data", fileName).toFile();
        Gson gson = new Gson();

        try (
                FileReader reader = new FileReader(commandFile);
                JsonReader jsonReader = new JsonReader(reader)
        ) {
            return gson.fromJson(jsonReader, JsonObject.class);

        } catch (IOException | NullPointerException e) {
            System.out.println("File not found or file corrupted");
            e.printStackTrace();
        }
        throw new NullPointerException("Json object wasn't found");
    }
}
