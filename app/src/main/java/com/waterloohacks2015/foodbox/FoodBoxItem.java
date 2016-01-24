package com.waterloohacks2015.foodbox;

/**
 * Created by dxu on 16-01-23.
 */
public class FoodBoxItem {

    String foodName;
    long expirationDate;
    boolean isPublic;

    FoodBoxItem() {}

    FoodBoxItem(String aInFoodName, long aInExpirationDate, boolean aInIsPublic)
    {
        foodName = aInFoodName;
        expirationDate = aInExpirationDate;
        isPublic = aInIsPublic;
    }

    public String getFoodName()
    {
        return foodName;
    }

    public long getExpirationDate()
    {
        return expirationDate;
    }

    public boolean getIsPublic()
    {
        return isPublic;
    }
}
