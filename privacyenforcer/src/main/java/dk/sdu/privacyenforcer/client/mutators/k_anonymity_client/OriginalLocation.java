package dk.sdu.privacyenforcer.client.mutators.k_anonymity_client;

public class OriginalLocation {

    private String id;
    private double lat;
    private double lon;
    private int k;
    private double dx;
    private double dy;
    private long timestamp;

    public OriginalLocation(String id, double lat, double lon, int k, double dx, double dy, long timestamp) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.k = k;
        this.dx = dx;
        this.dy = dy;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
