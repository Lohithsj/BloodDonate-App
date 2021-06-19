package com.example.blooddonate;

import android.graphics.drawable.Drawable;

public class CategoriesHelperClass {

    Drawable gradient;
    int image;
    String titile ;


    public CategoriesHelperClass(Drawable gradient, int image, String titile ) {
        this.image = image;
        this.titile = titile;
        this.gradient = gradient;

    }

    public int getImage() {
        return image;
    }

    public String getTitile() {
        return titile;
    }

    public Drawable getGradient() {
        return gradient;
    }

}
