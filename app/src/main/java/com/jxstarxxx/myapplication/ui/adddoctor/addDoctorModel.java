package com.jxstarxxx.myapplication.ui.adddoctor;

public class addDoctorModel {

    private String full_name, clinic_name, department_name, img_url;

    public addDoctorModel(String full_name, String clinic_name, String department_name, String img_url) {
        this.full_name = full_name;
        this.clinic_name = clinic_name;
        this.department_name = department_name;
        this.img_url = img_url;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public String getImg_url() {
        return img_url;
    }
}
