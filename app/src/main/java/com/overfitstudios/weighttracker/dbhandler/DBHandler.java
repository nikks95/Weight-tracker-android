package com.overfitstudios.weighttracker.dbhandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private String dbName;
    private int version;
    private List<String> tablesNames;
    private List<String> onCreateQueries;
    public DBHandler(@Nullable Context context, @Nullable String name, List<String> onCreateQuery,int dbVersion, List<String> tablesNames) {
        super(context, name, null, dbVersion);
        this.onCreateQueries = onCreateQuery;
        this.dbName = name;
        this.version = dbVersion;
        this.tablesNames = tablesNames;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for(String query:this.onCreateQueries){
            sqLiteDatabase.execSQL(query);
        }

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if(oldVersion<newVersion && this.tablesNames!=null){
            for(String tableName:this.tablesNames){
                String query = "Drop TABLE IF EXISTS "+tableName+";";
                sqLiteDatabase.execSQL(query);
            }
        }
    }
}
