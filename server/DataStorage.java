package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataStorage {
    final Map<String, String> storage;
    private final File fileOnDisk = Paths.get(".", "src", "server", "data", "db.json").toFile();

    public DataStorage() {
        this.storage = new HashMap<>(1000);
    }

    public ResponseDTO get(String key) {
        readFile();
        try {
            String returnedKey = storage.get(key);
            if (Objects.isNull(returnedKey)) {
                throw new NullPointerException();
            }
            return new ResponseDTO(Status.OK, returnedKey);
        } catch (NullPointerException e) {
            return new ResponseDTO(Status.ERROR, "No such key");
        }
    }

    public ResponseDTO save(String key, String value) {
        readFile();
        storage.put(key, value);
        saveToFile();
        return new ResponseDTO(Status.OK);
    }

    public ResponseDTO delete(String key) {
        readFile();
        if (storage.containsKey(key)) {
            storage.remove(key);
            saveToFile();
            return new ResponseDTO(Status.OK);
        }
        return new ResponseDTO(Status.ERROR, "No such key");
    }

    private synchronized void readFile() {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        Lock readLock = lock.readLock();
        readLock.lock();

        Gson gson = new Gson();
        try (
                FileReader reader = new FileReader(fileOnDisk);
                JsonReader jsonReader = new JsonReader(reader);
        ) {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();

            Map<String, String> map = gson.fromJson(jsonReader, type);
            storage.putAll(map);
        } catch (IOException | NullPointerException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    private synchronized void saveToFile() {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        Lock writeLock = lock.writeLock();
        writeLock.lock();

        Gson gson = new Gson();
        try (Writer writer = new FileWriter(fileOnDisk)) {
            gson.toJson(this.storage, writer);
            System.out.println("File saved.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not saved!");
        } finally {
            writeLock.unlock();
        }
    }
}
