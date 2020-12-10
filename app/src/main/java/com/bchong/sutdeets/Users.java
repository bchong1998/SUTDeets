package com.bchong.sutdeets;

public class Users {
    private String Name;
    private String StudentID;
    private String Email;
    private String TAGS;

    public Users(String name, String studentID,String email, String TAGS) {
        Name = name;
        StudentID = studentID;
        this.Email=email;
        this.TAGS = TAGS;
    }

    public Users() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getTAGS() {
        return TAGS;
    }

    public void setTAGS(String TAGS) {
        this.TAGS = TAGS;
    }
}
