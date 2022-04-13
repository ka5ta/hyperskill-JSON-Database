package client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class MessageDTO {
    private String type;
    private String key;
    private String value;

    public MessageDTO() {
    }

    public MessageDTO(String type) {
        this.type = type;
    }

    public MessageDTO(String type, String key) {
        this(type);
        this.key = key;
    }

    public MessageDTO(String type, String key, String value) {
        this(type, key);
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MessageDTO messageFromFile(String fileName) throws IOException {

        File commandFile = Paths.get(".", "src", "client", "data", fileName).toFile();

        Map<String, String> messagePairs = fromFile(commandFile);

        if(messagePairs.containsKey("type")){
            this.setType(messagePairs.get("type"));
        }

        if(messagePairs.containsKey("key")){
            this.setKey(messagePairs.get("key"));
        }

        if(messagePairs.containsKey("value")){
            this.setValue(messagePairs.get("value"));
        }

        return this;
}

    private Map<String, String> fromFile(File file) {
        Gson gson = new Gson();
        try (
                FileReader reader = new FileReader(file);
                JsonReader jsonReader = new JsonReader(reader)
        ) {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();

            return gson.fromJson(jsonReader, type);

        } catch (IOException | NullPointerException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
        throw new NullPointerException("File not found");
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "type='" + type + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
