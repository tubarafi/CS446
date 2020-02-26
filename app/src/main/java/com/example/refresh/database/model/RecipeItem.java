package com.example.refresh.database.model;

public class RecipeItem {

    private String name;

    private String sourceURL;

    public RecipeItem(String name, String sourceURL) {
        this.name = name;
        this.sourceURL = sourceURL;
    }

    public String getName() { return name; }

    public String getSourceURL() {return sourceURL; }

}
