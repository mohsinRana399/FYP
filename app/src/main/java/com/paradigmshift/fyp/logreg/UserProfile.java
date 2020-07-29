package com.paradigmshift.fyp.logreg;

public class UserProfile {

    private String FullName;
    private String password;
    private String Email;

    public UserProfile() {
        //firebase constructor
    }


    public UserProfile(String username, String password, String email) {

        this.FullName = username;
        this.password = password;
        this.Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
