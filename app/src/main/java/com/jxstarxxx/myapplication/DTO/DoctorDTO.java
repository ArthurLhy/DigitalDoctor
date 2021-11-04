package com.jxstarxxx.myapplication.DTO;

public class DoctorDTO {

    private String uid, fullName, clinicName, departmentName, imgUrl;
    private boolean isAdded;



    public DoctorDTO(String uid, String full_name, String clinicName, String departmentName, String imgUrl, boolean isAdded) {
        this.uid = uid;
        this.fullName = full_name;
        this.clinicName = clinicName;
        this.departmentName = departmentName;
        this.imgUrl = imgUrl;
        this.isAdded = isAdded;
    }

    public String getUid() {
        return uid;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public boolean isAdded() {
        return isAdded;
    }
}
