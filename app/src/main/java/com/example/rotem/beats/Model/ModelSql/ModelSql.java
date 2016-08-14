package com.example.rotem.beats.Model.ModelSql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ModelSql {
    final static int VERSION = 1;

    Helper sqlDb;


    public ModelSql(Context context){
        sqlDb = new Helper(context);
    }

    public SQLiteDatabase getWritableDB(){
        return sqlDb.getWritableDatabase();
    }

    public SQLiteDatabase getReadbleDB(){
        return sqlDb.getReadableDatabase();
    }

    class Helper extends SQLiteOpenHelper {
        public Helper(Context context) {
            super(context, "database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            UsersSql.create(db);
            PlaylistSql.create(db);
            SongsSql.create(db);
            LastUpdateSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            SongsSql.drop(db);
            PlaylistSql.drop(db);
            UsersSql.drop(db);
            LastUpdateSql.drop(db);
            onCreate(db);
        }
    }
}
