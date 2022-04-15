package server.Database;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import server.Enums.Status;
import server.Model.ResponseDTO;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataStorage {

    JsonObject jsonDatabase;
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private static final File fileOnDisk = Paths.get(".", "src", "server", "data", "db.json").toFile();
    private final Lock readLock;
    private final Lock writeLock;

    {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        writeLock = lock.writeLock();
        readLock = lock.readLock();
    }

    public DataStorage() {
        this.jsonDatabase = new JsonObject();
    }

    public ResponseDTO get(List<String> keys) {
        loadDatabaseFromFile();

        JsonElement currentElement = jsonDatabase;
        for (String key : keys) {
            currentElement = currentElement.getAsJsonObject().get(key);
            if (Objects.isNull(currentElement)) {
                return new ResponseDTO(Status.ERROR, "No such key");
            }
        }

        return new ResponseDTO(Status.OK, currentElement);
    }

    public ResponseDTO save(List<String> keys, JsonElement newValue) {
        loadDatabaseFromFile();
        String lastKey = keys.remove(keys.size() - 1);

        JsonObject current = jsonDatabase;
        for (String currentKey : keys) {
            JsonElement next = current.getAsJsonObject().get(currentKey);
            if (!Objects.isNull(next)) {
                current = next.getAsJsonObject();
            } else {
                JsonObject newObject = new JsonObject();
                current.add(currentKey, newObject);
                current = newObject;
            }
        }
        current.add(lastKey, newValue);
        saveDatabaseToFile();
        return new ResponseDTO(Status.OK);
    }

    public ResponseDTO delete(List<String> keys) {
        loadDatabaseFromFile();
        String lastKey = keys.remove(keys.size() - 1);

        JsonObject current = jsonDatabase;
        for (String key : keys) {
            JsonElement next = current.getAsJsonObject().get(key);
            if (Objects.isNull(next)) {
                return new ResponseDTO(Status.ERROR, "No such key");
            }
            current = next.getAsJsonObject();
        }

        JsonElement removed = current.remove(lastKey);
        if(Objects.isNull(removed)){
            return new ResponseDTO(Status.ERROR, "No such key");
        }
        saveDatabaseToFile();
        return new ResponseDTO(Status.OK);
    }

    private synchronized void loadDatabaseFromFile() {
        readLock.lock();

        try (
                FileReader reader = new FileReader(fileOnDisk);
                JsonReader jsonReader = new JsonReader(reader);
        ) {
            jsonDatabase = gson.fromJson(jsonReader, JsonObject.class);
        } catch (IOException | NullPointerException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    private synchronized void saveDatabaseToFile() {
        writeLock.lock();

        try (
                Writer writer = new FileWriter(fileOnDisk);
                JsonWriter jsonWriter = new JsonWriter(writer)
        ) {
            gson.toJson(this.jsonDatabase, jsonWriter);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not saved!");
        } finally {
            writeLock.unlock();
        }
    }
}
