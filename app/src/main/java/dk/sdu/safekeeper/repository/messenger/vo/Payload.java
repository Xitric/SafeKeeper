package dk.sdu.safekeeper.repository.messenger.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;

    public Payload() {
    }

    public Payload(Coordinates coordinates) {
        super();
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
