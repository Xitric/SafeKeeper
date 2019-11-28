package dk.sdu.safekeeper.repository.messenger.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Entry {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("time")
    @Expose
    private int time;
    @SerializedName("messaging")
    @Expose
    private List<Messaging> messaging = null;

    public Entry() {
    }

    public Entry(String id, int time, List<Messaging> messaging) {
        super();
        this.id = id;
        this.time = time;
        this.messaging = messaging;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<Messaging> getMessaging() {
        return messaging;
    }

    public void setMessaging(List<Messaging> messaging) {
        this.messaging = messaging;
    }
}
