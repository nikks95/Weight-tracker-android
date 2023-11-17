package com.overfitstudios.weighttracker.dbhandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.overfitstudios.weighttracker.model.Weight;
import com.overfitstudios.weighttracker.model.WeightEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WeightItemDBHandler{
    private static  final String DBLOG = "WEIGHTDBLOG";
    DBHandler handler;

    private static final String DB_NAME="weightdb";
    private static  final int DB_VERSION = 1;

    /*Weight Table*/
    private static final String WEIGHT_TABLE = "weight_items";
    private static final String WEIGHTITEM_ID = "id";
    private static final String WEIGHTUNIT = "weight";
    private static final String WEIGHTTIME = "time";
    /*end*/

    private List<String> getTableNames(){
        /*Add more tables names here if new is added*/
        String names[] = {WEIGHT_TABLE};
        return Arrays.asList(names);
    }
    private List<String> getOnCreateQuesriesForTable(){
        String queries[] = {
                "CREATE TABLE IF NOT EXISTS "+WEIGHT_TABLE+" ("
                        + WEIGHTITEM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + WEIGHTUNIT+" REAL, "
                        + WEIGHTTIME+" INTEGER);"
        };
        return Arrays.asList(queries);
    }
    public WeightItemDBHandler(Context context) {
        this.handler = new DBHandler(context,DB_NAME,getOnCreateQuesriesForTable(),DB_VERSION,getTableNames());
    }
    public DBHandler getDBHandler(){
        return this.handler;
    }

    public boolean addToWeightTable(WeightEntry entry){
        boolean status = false;
        SQLiteDatabase db = this.handler.getWritableDatabase();
        if(!isDateEntryExistsTimeStamp(db,entry.getTimestamp().getTime())){
            ContentValues values = new ContentValues();
            double weight = entry.getWeight().getWeightinKg();
            Log.d("ENTTTT",""+weight);
            long timestamp = entry.getTimestamp().getTime();
            values.put(WEIGHTUNIT,weight);
            values.put(WEIGHTTIME,timestamp);

            long newRow = db.insert(WEIGHT_TABLE,null,values);
            db.close();
            status  = newRow!=-1;
            if(status){
                Log.d(DBLOG,"Inserted to database successfully!!");
            }
            return status;
        }
        else{
            db.close();
            return status;
        }

    }
    public List<WeightEntry> getAllWeightEntries(){
        SQLiteDatabase db = this.handler.getReadableDatabase();
        List<WeightEntry> entries = new ArrayList<>();
        Cursor cursor = db.query(WEIGHT_TABLE,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            int weightTimeIndex = cursor.getColumnIndex(WEIGHTTIME);
            int weightUnitIndex = cursor.getColumnIndex(WEIGHTUNIT);
            if(weightUnitIndex!=-1&&weightTimeIndex!=-1) {
                long timestamp = cursor.getLong(weightTimeIndex);
                double weight = cursor.getDouble(weightUnitIndex);
                Log.d("DBENTRY",weight+"   "+timestamp);
                Date date = new Date(timestamp);
                WeightEntry entry = new WeightEntry(date,new Weight(weight,Weight.KG));
                entries.add(entry);
            }
        }
        cursor.close();
        db.close();
        return entries;
    }
    public boolean updateWeightEntry(WeightEntry entry){
        SQLiteDatabase db = this.handler.getWritableDatabase();
        ContentValues values = new ContentValues();
        double weight = entry.getWeight().getWeightinKg();
        values.put(WEIGHTUNIT,weight);
        int rowCount  = db.update(WEIGHT_TABLE, values, WEIGHTTIME+" = ?", new String[]{String.valueOf(entry.getTimestamp().getTime())});
        db.close();
        return rowCount!=-1;
    }
    public void deleteWeightEntry(Date timeStamp){
        SQLiteDatabase db = this.handler.getWritableDatabase();
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(timeStamp);
        db.delete(WEIGHT_TABLE,"strftime('%Y-%m-%d',"+WEIGHTTIME+"/1000, 'unixepoch') = ?",new String[]{dateString});
        db.close();
    }
    private boolean isDateEntryExists(SQLiteDatabase db, Date date){
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
        Cursor cursor = db.query(WEIGHT_TABLE,null,"strftime('%Y-%m-%d',"+WEIGHTTIME+"/1000, 'unixepoch') = ?",new String[]{dateString},null,null,null);
        int count = cursor.getCount();
        return count>0;
    }
    private boolean isDateEntryExistsTimeStamp(SQLiteDatabase db, long timestamp) {
        Cursor cursor = db.query(
                WEIGHT_TABLE,
                null,
                WEIGHTTIME + " = ?",
                new String[]{String.valueOf(timestamp)},
                null,
                null,
                null
        );

        int count = cursor.getCount();
        cursor.close(); // Close the cursor to avoid resource leaks
        return count > 0;
    }
}
