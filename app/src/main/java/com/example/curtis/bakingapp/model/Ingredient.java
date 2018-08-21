package com.example.curtis.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Ingredient implements Parcelable {
    private int theQuantity;
    private String theMeasurement;
    private String theIngredient;

    //getters and setters
    public int getTheQuantity() {
        return theQuantity;
    }

    public void setTheQuantity(int theQuantity) {
        this.theQuantity = theQuantity;
    }

    public String getTheMeasurement() {
        return theMeasurement;
    }

    public void setTheMeasurement(String theMeasurement) {
        this.theMeasurement = theMeasurement;
    }

    public String getTheIngredient() {
        return theIngredient;
    }

    public void setTheIngredient(String theIngredient) {
        this.theIngredient = theIngredient;
    }

    //constructors
    public Ingredient(int theQuantity, String theMeasurement, String theIngredient) {

        this.theQuantity = theQuantity;
        this.theMeasurement = theMeasurement;
        this.theIngredient = theIngredient;
    }

    public Ingredient(JSONObject jsonIngredientObj){
        if(jsonIngredientObj != null){
            try{
                this.theQuantity = jsonIngredientObj.getInt("quantity");
                this.theMeasurement = jsonIngredientObj.getString("measure");
                this.theIngredient = jsonIngredientObj.getString("ingredient");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private Ingredient(Parcel in) {
        this.theQuantity = in.readInt();
        this.theMeasurement = in.readString();
        this.theIngredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(theQuantity);
        parcel.writeString(theMeasurement);
        parcel.writeString(theIngredient);
    }

    public String getInfo(){
        return theQuantity + " " + theMeasurement + " of " + theIngredient;
    }
}
