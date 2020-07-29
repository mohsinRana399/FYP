package com.paradigmshift.fyp.FirebaseDatamodel;

public class MiniProfileDataModel {

    private String DocumentId;
    private String ProfilePicUrl;
    private String Name;
    private String Status;

    public MiniProfileDataModel(){}
    public MiniProfileDataModel( String name,String profilePicUrl, String status) {
        this.ProfilePicUrl = profilePicUrl;
        this.Name = name;
        this.Status = status;
    }
    public void ChangeStatus(String text){
        Status = text;
    }

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }

    public String getProfilePicUrl() {
        return ProfilePicUrl;
    }

    public String getName() {
        return Name;
    }

    public String getStatus() {
        return Status;
    }
}
