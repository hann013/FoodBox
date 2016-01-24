package com.waterloohacks2015.foodbox;

/**
 * Created by dxu on 16-01-23.
 */
public class FoodBoxItem {

    String foodName;
    int expirationDate;
    boolean isPublic;

    FoodBoxItem(String aInFoodName, int aInExpirationDate, boolean aInIsPublic)
    {
        foodName = aInFoodName;
        expirationDate = aInExpirationDate;
        isPublic = aInIsPublic;
    }

    public String getFoodName()
    {
        return foodName;
    }

    public int getExpirationDate()
    {
        return expirationDate;
    }

    public boolean getIsPublic()
    {
        return isPublic;
    }
}
