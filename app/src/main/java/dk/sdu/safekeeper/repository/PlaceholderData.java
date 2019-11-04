package dk.sdu.safekeeper.repository;

import com.google.gson.annotations.SerializedName;

public class PlaceholderData {

    @SerializedName("name")
    public String name;

    @SerializedName("age")
    public int age;

    @SerializedName("height")
    public double height;

    public PlaceholderData(String name, int age, double height) {
        this.name = name;
        this.age = age;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
