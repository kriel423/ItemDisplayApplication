package com.example.itemdisplayapplication;

public class Upload {
    private String mTitle;
    private String mDescription;
    private String mQuanitity;
    private String mImageUrl;

    public Upload()
    {}

    public Upload(String title, String description, String quantity, String imageUrl)
    {
        if(title.trim().equals(""))
        {
            title = "No Title";
        }

        mTitle = title;
        mDescription = description;
        mQuanitity = quantity;
        mImageUrl = imageUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmQuanitity() {
        return mQuanitity;
    }

    public void setmQuanitity(String mQuanitity) {
        this.mQuanitity = mQuanitity;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
