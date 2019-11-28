package dk.sdu.safekeeper.repository.messenger.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coordinates {

    @SerializedName("lat")
    @Expose
    private int lat;
    @SerializedName("long")
    @Expose
    private int _long;

    public Coordinates() {
    }

    public Coordinates(int lat, int _long) {
        super();
        this.lat = lat;
        this._long = _long;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLong() {
        return _long;
    }

    public void setLong(int _long) {
        this._long = _long;
    }
}
