package db;

/**
 * Created by jennadeng on 2016-01-23.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.waterloohacks2015.foodbox.R;

public class ListDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "foodlist.db";
    public static final String TABLE_NAME = "food_list_T";
    public static final String KEY_ID = "_id";
    public static final String COL_2 = "FOOD_NAME";
    public static final String COL_3 = "EXPIRATION_DATE";

    private static final String TAG = "FoodListDbAdapter";


    public ListDBHelper (Context context) {
        super(context, DATABASE_NAME, null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE if not exists  " + TABLE_NAME +" (" + KEY_ID +
                        " integer PRIMARY KEY autoincrement, " +
                        COL_2 + " ," +
                        COL_3 + ");");

        Log.d("ListDBHelper","Query to form table: "+sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqlDB);
    }

    public boolean insertData(String foodName, String expirationDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, foodName);
        contentValues.put(COL_3, expirationDate);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor displayData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, COL_2, COL_3},
                null, null, null, null, COL_3 + " ASC");
        /*Cursor cursor = db.rawQuery("select " + _ID + " AS _id, " + "," + COL_2 + "," + COL_3 +
                " from " + TABLE_NAME, null);*/
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


}