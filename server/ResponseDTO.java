package server;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ResponseDTO {
    Status status;
    String text;

    public ResponseDTO(Status status) {
        this.status = status;
    }

    public ResponseDTO(Status status, String text) {
        this(status);
        this.text = text;
    }

    public Status getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }

    static JsonSerializer<ResponseDTO> serializer = new JsonSerializer<ResponseDTO>() {
        @Override
        public JsonElement serialize(ResponseDTO source, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("response", source.getStatus().name());
            if (source.getStatus() == Status.OK) {
                jsonObject.addProperty("value", source.getText());
            } else if (source.getStatus() == Status.ERROR) {
                jsonObject.addProperty("reason", source.getText());
            }
            return jsonObject;
        }
    };
}



