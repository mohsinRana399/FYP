package com.paradigmshift.fyp.FirebaseDatamodel;

public class ProfileDataModel {

    private String Name;
    private String title;
    private String descruption;
    private int Ratings;
    private String phoneNo;
    private String email;
    private String Location;

    private String profilePicUrl;
    private String DocumentID;

    public ProfileDataModel(){
        // Empty constructor needed for firebase shit
    }

    public ProfileDataModel(String name, String title, String description, int ratings, String phoneNo, String email, String location) {
        this.Name = name;
        this.title = title;
        this.descruption = description;
        this.Ratings = ratings;
        this.phoneNo = phoneNo;
        this.email = email;
        this.Location = location;
    }

    public String getName() {
        return Name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescruption() {
        return descruption;
    }

    public int getRatings() {
        return Ratings;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return Location;
    }

    //DOCid

    public String getDocumentID() {
        return DocumentID;
    }

    public void setDocumentID(String documentID) {
        DocumentID = documentID;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }
}
