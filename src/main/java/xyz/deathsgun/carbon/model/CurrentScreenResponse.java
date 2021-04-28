package xyz.deathsgun.carbon.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CurrentScreenResponse {

    public boolean isIngame;
    public String title;
    public String className;
    @SerializedName("class")
    public String clazz;
    public ArrayList<Element> children;
}
