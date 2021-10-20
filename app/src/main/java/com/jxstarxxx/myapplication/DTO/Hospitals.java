package com.jxstarxxx.myapplication.DTO;

import java.util.List;

public class Hospitals {
    private String hospitalName;

    private String rating;

    private String openingHours;

    private String address;

    private List<Double> geometry;

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Double> getGeometry() {
        return geometry;
    }

    public void setGeometry(List<Double> geometry) {
        this.geometry = geometry;
    }

    @Override
    public String toString() {
        return "Hospitals{" +
                "hospitalName='" + hospitalName + '\'' +
                ", rating='" + rating + '\'' +
                ", openingHours='" + openingHours + '\'' +
                ", address='" + address + '\'' +
                ", geometry=" + geometry +
                '}';
    }
}
