package com.kucharzyk.diabetictoolboxjetpack.ui_and_data.diary;

import com.kucharzyk.diabetictoolboxjetpack.room_database.Product;

public class DiaryEntry extends Product {
    public DiaryEntry(String productName, Double carbohydrates, Double fat, Double proteins,
                      Double servingSize) {
        super(productName, carbohydrates, fat, proteins, servingSize);
    }
/*    private String mProductName;
    private Double mCarbohydrates;
    private Double mFat;
    private Double mProteins;
    private Double mWeight = 100.0;

    public DiaryEntry(String productName, Double carbohydrates, Double fat, Double proteins){
        mProductName = productName;
        mCarbohydrates = carbohydrates;
        mFat = fat;
        mProteins = proteins;
    }


    public String getProductName() {
        return mProductName;
    }

    public Double getCarbohydrates() {
        return mCarbohydrates;
    }

    public Double getFat() {
        return mFat;
    }

    public Double getProteins() {
        return mProteins;
    }

    public Double getWeight() {
        return mWeight;
    }*/
}
