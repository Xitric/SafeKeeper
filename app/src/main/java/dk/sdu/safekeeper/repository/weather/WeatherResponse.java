package dk.sdu.safekeeper.repository.weather;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class WeatherResponse {

    private float latitude;
    private float longitude;
    private String weatherName;
    private String description;
    private float temperature;
    private float minTemperature;
    private float maxTemperature;
    private int pressure;
    private int humidity;
    private float windSpeed;
    private int windDirection;
    private int clouds;
    private Calendar sunrise;
    private Calendar sunset;

    public float getLatitude() {
        return latitude;
    }

    private void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    private void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getWeatherName() {
        return weatherName;
    }

    private void setWeatherName(String weatherName) {
        this.weatherName = weatherName;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public float getTemperature() {
        return temperature;
    }

    private void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    private void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    private void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public int getPressure() {
        return pressure;
    }

    private void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    private void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    private void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    private void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public int getClouds() {
        return clouds;
    }

    private void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public Calendar getSunrise() {
        return sunrise;
    }

    private void setSunrise(Calendar sunrise) {
        this.sunrise = sunrise;
    }

    public Calendar getSunset() {
        return sunset;
    }

    private void setSunset(Calendar sunset) {
        this.sunset = sunset;
    }

    public static final class WeatherResponseDeserializer implements JsonDeserializer<WeatherResponse> {

        @Override
        public WeatherResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            WeatherResponse response = new WeatherResponse();
            response.setLatitude(jsonObject.getAsJsonObject("coord").get("lat").getAsFloat());
            response.setLongitude(jsonObject.getAsJsonObject("coord").get("lon").getAsFloat());

            response.setWeatherName(jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString());
            response.setDescription(jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString());

            response.setTemperature(toCelsius(jsonObject.getAsJsonObject("main").get("temp").getAsFloat()));
            response.setMinTemperature(toCelsius(jsonObject.getAsJsonObject("main").get("temp_min").getAsFloat()));
            response.setMaxTemperature(toCelsius(jsonObject.getAsJsonObject("main").get("temp_max").getAsFloat()));
            response.setPressure(jsonObject.getAsJsonObject("main").get("pressure").getAsInt());
            response.setHumidity(jsonObject.getAsJsonObject("main").get("humidity").getAsInt());

            response.setWindSpeed(jsonObject.getAsJsonObject("wind").get("speed").getAsFloat());
            response.setWindDirection(jsonObject.getAsJsonObject("wind").get("deg").getAsInt());

            response.setClouds(jsonObject.getAsJsonObject("clouds").get("all").getAsInt());

            int sunriseSeconds = jsonObject.getAsJsonObject("sys").get("sunrise").getAsInt();
            Calendar sunriseCalendar = new GregorianCalendar();
            sunriseCalendar.setTimeInMillis(sunriseSeconds * 1000);
            response.setSunset(sunriseCalendar);

            int sunsetSeconds = jsonObject.getAsJsonObject("sys").get("sunset").getAsInt();
            Calendar sunsetCalendar = new GregorianCalendar();
            sunsetCalendar.setTimeInMillis(sunsetSeconds * 1000);
            response.setSunset(sunsetCalendar);

            return response;
        }

        private float toCelsius(float kelvin) {
            return kelvin - 272.15f;
        }
    }
}
