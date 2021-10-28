package com.jxstarxxx.myapplication.ui.doctorlist;

public class DoctorModel {
    private String fullName;
    private String clinicName;
    private String departmentName;
    private String profilePic;

    public DoctorModel(String fullName, String clinicName, String departmentName, String profilePic) {
        this.fullName = fullName;
        this.clinicName = clinicName;
        this.departmentName = departmentName;
        this.profilePic = profilePic;
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

    public String getProfilePic() {
        return profilePic;
    }
}
