package com.waterloohacks2015.foodbox;

/**
 * Created by dxu on 16-01-23.
 */
public class FoodBoxItem {

    String foodName;
    long expirationDate;
    boolean isPublic;
    String userName;

    FoodBoxItem() {}

    FoodBoxItem(String aInFoodName, long aInExpirationDate, boolean aInIsPublic, String aInUserName)
    {
        foodName = aInFoodName;
        expirationDate = aInExpirationDate;
        isPublic = aInIsPublic;
        userName = aInUserName;
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

    public String getUserName() { return userName; }
}
