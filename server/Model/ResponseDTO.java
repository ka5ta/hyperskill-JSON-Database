package server.Model;

import com.google.gson.*;
import server.Enums.Status;

import java.lang.reflect.Type;

public class ResponseDTO {
    Status status;
    JsonElement element;
    String reason;


    public ResponseDTO(Status status) {
        this.status = status;
    }

    public ResponseDTO(Status status, String reason) {
        this(status);
        this.reason = reason;
    }

    public ResponseDTO(Status status, JsonElement element) {
        this(status);
        this.element = element;
    }

    public Status getStatus() {
        return status;
    }

    public JsonElement getElement() {
        return element;
    }

    public String getReason() {
        return reason;
    }



    public String serializeTextResponse(){
        Gson gsonSerialize = new GsonBuilder()
                .registerTypeAdapter(ResponseDTO.class, serializer)
                .create();

        return gsonSerialize.toJson(this);
    }

    static JsonSerializer<ResponseDTO> serializer = new JsonSerializer<ResponseDTO>() {
        @Override
        public JsonObject serialize(ResponseDTO source, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("response", source.getStatus().name());
            if (source.getStatus() == Status.OK) {
                jsonObject.add("value", source.getElement());
            } else if (source.getStatus() == Status.ERROR) {
                jsonObject.addProperty("reason", source.getReason());
            }
            return jsonObject;
        }
    };


}



