package com.example.b3tempoduffard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TempoDate {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("couleur")
    @Expose
    private TempoColor couleur;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TempoColor getCouleur() {
        return couleur;
    }

    public void setCouleur(TempoColor couleur) {
        this.couleur = couleur;
    }

}
