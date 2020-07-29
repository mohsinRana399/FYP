package com.paradigmshift.fyp.viewpager;

public class UserData {

    private int mProfilePic;
    private String mName;
    private String mStatus;
    private String DocumentId;

    public UserData(int imageResource, String text1, String text2) {
        mProfilePic = imageResource;
        mName = text1;
        mStatus = text2;
    }

    public int getmProfilePic() {
        return mProfilePic;
    }

    public String getmName() {
        return mName;
    }

    public String getmStatus() {
        return mStatus;
    }

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }
}
