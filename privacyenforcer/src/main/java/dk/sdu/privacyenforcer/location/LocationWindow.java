package dk.sdu.privacyenforcer.location;

import android.location.Location;

import java.util.LinkedList;

class LocationWindow {

    private static final int WINDOW_SIZE = 5;
    private LinkedList<Location> locations;

    LocationWindow() {
        this.locations = new LinkedList<>();
    }

    /**
     * Add a new location to this window. This will automatically purge old locations when the
     * window size has been reached.
     *
     * @param location the new location to add
     */
    void addLocation(Location location) {
        locations.addFirst(location);
        if (locations.size() > WINDOW_SIZE) {
            locations.removeLast();
        }
    }

    /**
     * Get the location that was most recently added to this window.
     *
     * @return the location that was most recently added
     */
    Location getMostRecentLocation() {
        if (locations.isEmpty()) {
            return null;
        }
        return locations.getFirst();
    }

    /**
     * Get the average speed over the location fixes in this window. Using the average speed ensures
     * that erroneous outliers have minimal a effect on the speed.
     *
     * @return the average speed of the device
     */
    float getAverageSpeed() {
        if (locations.size() == 0) return 0;
        if (locations.size() == 1) return locations.get(0).getSpeed();

        float accumulatedSpeeds = 0;
        for (int i = 0; i < locations.size() - 1; i++) {
            Location newLoc = locations.get(i);
            Location oldLoc = locations.get(i + 1);

            float dist = oldLoc.distanceTo(newLoc);
            float time = (newLoc.getTime() - oldLoc.getTime()) / 1000f;

            accumulatedSpeeds += dist / time;
        }

        return accumulatedSpeeds / locations.size();
    }
}
