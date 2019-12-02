package dk.sdu.privacyenforcer.client.mutators.k_anonymity_client;

public class AnonymizedLocation {

    private String id;
    private double latMin;
    private double latMax;
    private double lonMin;
    private double lonMax;

    public AnonymizedLocation(String id, double latMin, double latMax, double lonMin, double lonMax) {
        this.id = id;
        this.latMin = latMin;
        this.latMax = latMax;
        this.lonMin = lonMin;
        this.lonMax = lonMax;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatMin() {
        return latMin;
    }

    public void setLatMin(double latMin) {
        this.latMin = latMin;
    }

    public double getLatMax() {
        return latMax;
    }

    public void setLatMax(double latMax) {
        this.latMax = latMax;
    }

    public double getLonMin() {
        return lonMin;
    }

    public void setLonMin(double lonMin) {
        this.lonMin = lonMin;
    }

    public double getLonMax() {
        return lonMax;
    }

    public void setLonMax(double lonMax) {
        this.lonMax = lonMax;
    }
}
