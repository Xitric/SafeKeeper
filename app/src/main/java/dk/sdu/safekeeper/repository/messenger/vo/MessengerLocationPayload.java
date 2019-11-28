package dk.sdu.safekeeper.repository.messenger.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessengerLocationPayload {

    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("entry")
    @Expose
    private List<Entry> entry = null;

    public MessengerLocationPayload() {
    }

    public MessengerLocationPayload(String object, List<Entry> entry) {
        super();
        this.object = object;
        this.entry = entry;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }
}
