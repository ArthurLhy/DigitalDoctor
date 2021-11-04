package com.jxstarxxx.myapplication.DTO;

public class DoctorModel {
    private String fullName;
    private String clinicName;
    private String departmentName;
    private String profilePic;
    private String uid;
    private boolean chatted;
    private String username;

    public DoctorModel(String fullName, String clinicName, String departmentName, String profilePic, String uid, boolean chatted, String username) {
        this.fullName = fullName;
        this.clinicName = clinicName;
        this.departmentName = departmentName;
        this.profilePic = profilePic;
        this.uid = uid;
        this.chatted = chatted;
        this.username = username;
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

    public String getUid() {
        return uid;
    }

    public boolean isChatted() {
        return chatted;
    }

    public void setChatted(boolean chatted) {
        this.chatted = chatted;
    }

    public String getUsername() {
        return username;
    }
}
