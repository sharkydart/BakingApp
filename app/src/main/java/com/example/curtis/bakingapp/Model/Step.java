package com.example.curtis.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Step implements Parcelable{
    private int theID;
    private String theShortDescription;
    private String theDescription;
    private String theVideoURL;
    private String theThumbnailURL;

    //constructors
    public Step(JSONObject jsonStepObj){
        if(jsonStepObj != null){
            try{
                this.theID = jsonStepObj.getInt("id");
                this.theShortDescription = jsonStepObj.getString("shortDescription");
                this.theDescription = jsonStepObj.getString("description");
                this.theVideoURL = jsonStepObj.getString("videoURL");
                this.theThumbnailURL = jsonStepObj.getString("thumbnailURL");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public Step(int theID, String theShortDescription, String theDescription, String theVideoURL, String theThumbnailURL) {
        this.theID = theID;
        this.theShortDescription = theShortDescription;
        this.theDescription = theDescription;
        this.theVideoURL = theVideoURL;
        this.theThumbnailURL = theThumbnailURL;
    }

    private Step(Parcel in) {
        this.theID = in.readInt();
        String[] strData = new String[4];
        in.readStringArray(strData);
        this.theShortDescription = strData[0];
        this.theDescription = strData[1];
        this.theVideoURL = strData[2];
        this.theThumbnailURL = strData[3];
    }

    //getters and setters
    public int getTheID() {
        return theID;
    }

    public void setTheID(int theID) {
        this.theID = theID;
    }

    public String getTheShortDescription() {
        return theShortDescription;
    }

    public void setTheShortDescription(String theShortDescription) {
        this.theShortDescription = theShortDescription;
    }

    public String getTheDescription() {
        return theDescription;
    }

    public void setTheDescription(String theDescription) {
        this.theDescription = theDescription;
    }

    public String getTheVideoURL() {
        return theVideoURL;
    }

    public void setTheVideoURL(String theVideoURL) {
        this.theVideoURL = theVideoURL;
    }

    public String getTheThumbnailURL() {
        return theThumbnailURL;
    }

    public void setTheThumbnailURL(String theThumbnailURL) {
        this.theThumbnailURL = theThumbnailURL;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(theID);
        parcel.writeStringArray(new String[]{
                this.theShortDescription,
                this.theDescription,
                this.theVideoURL,
                this.theThumbnailURL
        });
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

}
