package com.paradigmshift.fyp.FirebaseDatamodel;

import android.view.View;

import java.io.Serializable;

public class FoldableDataModel {


    String DocumentID;
    private View.OnClickListener requestBtnClickListener;

    private String backgroundImage;
    private int studentsTutored;
    private String rate;
    private String profilePic;
    private String name;
    private int review = 0;
    private String strretAddress;
    private String city;
    private String availableFrom;
    private String availableTo;
    private String tutorRequests;
    private String Email;
    private String status;
    private String Title = "";
    private String PhoneNo = "";

    public FoldableDataModel() {
    }

    public FoldableDataModel(String backgroundImage, int studentsTutored, String rate, String profilePic, String name, int review, String strretAddress, String city, String availableFrom, String availableTo, String tutorRequests, String status) {
        this.backgroundImage = backgroundImage;
        this.studentsTutored = studentsTutored;
        this.rate = rate;
        this.profilePic = profilePic;
        this.name = name;
        this.review = review;
        this.strretAddress = strretAddress;
        this.city = city;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
        this.tutorRequests = tutorRequests;
        this.status = status;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public int getStudentsTutored() {
        return studentsTutored;
    }

    public String getRate() {
        return rate;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getName() {
        return name;
    }

    public int getReview() {
        return review;
    }

    public String getStrretAddress() {
        return strretAddress;
    }

    public String getCity() {
        return city;
    }

    public String getAvailableFrom() {
        return availableFrom;
    }

    public String getAvailableTo() {
        return availableTo;
    }

    public String getTutorRequests() {
        return tutorRequests;
    }


    public String getStatus() {
        return status;
    }

    public String getDocumentID() {
        return DocumentID;
    }

    public void setDocumentID(String documentID) {
        DocumentID = documentID;
    }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }
}
