package com.example.curtis.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.ElementType;
import java.util.ArrayList;

public class Recipe implements Parcelable {
    private int theID;
    private String theName;
    private ArrayList<Ingredient> theIngredients;
    private ArrayList<Step> theSteps;
    private int theServings;
    private String theImage;

    public String getIngredientsDump(){
        if(theIngredients != null && theIngredients.size() > 0){
            StringBuilder tempStr = new StringBuilder();
            for (Ingredient thing:theIngredients){
                tempStr.append(thing.getInfo());
                tempStr.append('\n');
            }
            return tempStr.toString();
        }
        return "";
    }

    public String getStepsDump(){
        if(theSteps != null && theSteps.size() > 0){
            StringBuilder tempStr = new StringBuilder();
            for (Step thing:theSteps){
                tempStr.append(thing.getInfo());
                tempStr.append('\n');
                tempStr.append('\n');
            }
            return tempStr.toString();
        }
        return "";
    }

    //getters and setters
    public int getTheID() {
        return theID;
    }

    public void setTheID(int theID) {
        this.theID = theID;
    }

    public String getTheName() {
        return theName;
    }

    public void setTheName(String theName) {
        this.theName = theName;
    }

    public ArrayList<Ingredient> getTheIngredients() {
        return theIngredients;
    }

    public void setTheIngredients(ArrayList<Ingredient> theIngredients) {
        this.theIngredients = theIngredients;
    }

    public ArrayList<Step> getTheSteps() {
        return theSteps;
    }

    public void setTheSteps(ArrayList<Step> theSteps) {
        this.theSteps = theSteps;
    }

    public int getTheServings() {
        return theServings;
    }

    public void setTheServings(int theServings) {
        this.theServings = theServings;
    }

    public String getTheImage() {
        return theImage;
    }

    public void setTheImage(String theImage) {
        this.theImage = theImage;
    }

    //constructors
    public Recipe(int theID, String theName, ArrayList<Ingredient> theIngredients, ArrayList<Step> theSteps, int theServings, String theImage) {
        this.theID = theID;
        this.theName = theName;
        this.theIngredients = theIngredients;
        this.theSteps = theSteps;
        this.theServings = theServings;
        this.theImage = theImage;
    }

    public Recipe(JSONObject jsonRecipeObj){
        if(jsonRecipeObj != null){
            try{
                this.theID = jsonRecipeObj.getInt("id");
                this.theName = jsonRecipeObj.getString("name");
                ArrayList<Ingredient> ingredientsList = new ArrayList<>();
                JSONArray ingredientsArray = jsonRecipeObj.getJSONArray("ingredients");
                for(int i = 0; i < ingredientsArray.length(); i++) {
                    JSONObject currentObj = ingredientsArray.getJSONObject(i);
                    Ingredient tIng = new Ingredient(currentObj);
                    ingredientsList.add(tIng);
                }
                this.setTheIngredients(ingredientsList);
                ArrayList<Step> stepsList = new ArrayList<>();
                JSONArray stepsArray = jsonRecipeObj.getJSONArray("steps");
                for(int i = 0; i < stepsArray.length(); i++) {
                    JSONObject curStep = stepsArray.getJSONObject(i);
                    Step tStep = new Step(curStep);
                    stepsList.add(tStep);
                }
                this.setTheSteps(stepsList);

                this.theServings = jsonRecipeObj.getInt("servings");
                this.theImage = jsonRecipeObj.getString("image");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    //stuff to make it parcelable
    private Recipe(Parcel in) {
        this.theID = in.readInt();
        this.theName = in.readString();
        ArrayList<Ingredient> ing_temp = new ArrayList<>();
        in.readList(ing_temp, ElementType.class.getClassLoader());
        this.setTheIngredients(ing_temp);
        ArrayList<Step> stp_temp = new ArrayList<>();
        in.readList(stp_temp, ElementType.class.getClassLoader());
        this.setTheSteps(stp_temp);
        this.theServings = in.readInt();
        this.theImage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(theID);
        parcel.writeString(theName);
        parcel.writeList(theIngredients);
        parcel.writeList(theSteps);
        parcel.writeInt(theServings);
        parcel.writeString(theImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
