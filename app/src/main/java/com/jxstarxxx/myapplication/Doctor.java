package com.jxstarxxx.myapplication;

public class Doctor {
    private String fullName;
    private String clinicName;
    private String departmentName;
    private String uid;
    private String imageUrl;

    public Doctor(String fullName, String clinicName, String departmentName, String uid, String imageUrl) {
        this.fullName = fullName;
        this.clinicName = clinicName;
        this.departmentName = departmentName;
        this.uid = uid;
        this.imageUrl = imageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public String getClinicName() {
        return clinicName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getUid() {
        return uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
