package dk.sdu.safekeeper.repository;

import com.google.gson.annotations.SerializedName;

public class ServerResponse {

    @SerializedName("message")
    public String message;

    @SerializedName("code")
    public int code;

    public ServerResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
