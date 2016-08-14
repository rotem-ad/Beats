package com.example.rotem.beats.Model.ModelSql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rotem.beats.Model.Playlist;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Rotem on 09/08/2016.
 */

public class PlaylistSql {
    final static String PLAYLIST_TABLE = "playlists";
    final static String PLAYLIST_TABLE_ID = "_id";
    final static String PLAYLIST_TABLE_TITLE = "title";
    final static String PLAYLIST_TABLE_AUTHOR = "author";
    final static String PLAYLIST_TABLE_CREATION_DATE = "creationDate";
    final static String PLAYLIST_TABLE_PHOTO = "photo";
    final static String PLAYLIST_TABLE_TAGS = "tags";
    final static String PLAYLIST_TABLE_RATERS_SUM = "ratersSum";
    final static String PLAYLIST_TABLE_RATERS_CNT = "ratersCount";
    final static String PLAYLIST_TABLE_USER_ID = "userId";


    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + PLAYLIST_TABLE + " (" +
                PLAYLIST_TABLE_ID + " TEXT PRIMARY KEY," +
                PLAYLIST_TABLE_TITLE + " TEXT," +
                PLAYLIST_TABLE_AUTHOR + " TEXT," +
                PLAYLIST_TABLE_CREATION_DATE + " TEXT," +
                PLAYLIST_TABLE_PHOTO + " TEXT," +
                PLAYLIST_TABLE_TAGS + " TEXT," +
                PLAYLIST_TABLE_RATERS_SUM + " INTEGER," +
                PLAYLIST_TABLE_RATERS_CNT + " INTEGER," +
                PLAYLIST_TABLE_USER_ID + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + PLAYLIST_TABLE + ";");
    }

    public static List<Playlist> getAllPlaylists(SQLiteDatabase db) {
        Cursor cursor = db.query(PLAYLIST_TABLE, null, null , null, null, null, null);
        List<Playlist> playlists = new LinkedList<Playlist>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(PLAYLIST_TABLE_ID);
            int titleIndex = cursor.getColumnIndex(PLAYLIST_TABLE_TITLE);
            int authorIndex = cursor.getColumnIndex(PLAYLIST_TABLE_AUTHOR);
            int createDateIndex = cursor.getColumnIndex(PLAYLIST_TABLE_CREATION_DATE);
            int photoIndex = cursor.getColumnIndex(PLAYLIST_TABLE_PHOTO);
            int tagsIndex = cursor.getColumnIndex(PLAYLIST_TABLE_TAGS);
            int ratersSumIndex = cursor.getColumnIndex(PLAYLIST_TABLE_RATERS_SUM);
            int ratersCountIndex = cursor.getColumnIndex(PLAYLIST_TABLE_RATERS_CNT);
            int userIdIndex = cursor.getColumnIndex(PLAYLIST_TABLE_USER_ID);
            do {
                String id = cursor.getString(idIndex);
                String title = cursor.getString(titleIndex);
                String author = cursor.getString(authorIndex);
                String createDate = cursor.getString(createDateIndex);
                String photo = cursor.getString(photoIndex);
                String tags = cursor.getString(tagsIndex);
                int ratersSum = cursor.getInt(ratersSumIndex);
                int ratersCount = cursor.getInt(ratersCountIndex);
                String userId = cursor.getString(userIdIndex);
                Playlist pl = new Playlist(title,author);
                pl.setId(id);
                pl.setPhoto(photo);
                pl.setCreationDate(createDate);
                pl.setRatersCount(ratersCount);
                pl.setRatingSum(ratersSum);
                List<String> tagList = Arrays.asList(tags.split(","));
                pl.setTags(tagList);
                // pl.setUserId(userId)
                playlists.add(pl);
            } while (cursor.moveToNext());
        }
        return playlists;
    }

    public static Playlist getPlaylistById(SQLiteDatabase db, String id) {
        String where = PLAYLIST_TABLE_ID + " = ?";
        String[] args = {id};
        Cursor cursor = db.query(PLAYLIST_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(PLAYLIST_TABLE_ID);
            int titleIndex = cursor.getColumnIndex(PLAYLIST_TABLE_TITLE);
            int authorIndex = cursor.getColumnIndex(PLAYLIST_TABLE_AUTHOR);
            int createDateIndex = cursor.getColumnIndex(PLAYLIST_TABLE_CREATION_DATE);
            int photoIndex = cursor.getColumnIndex(PLAYLIST_TABLE_PHOTO);
            int tagsIndex = cursor.getColumnIndex(PLAYLIST_TABLE_TAGS);
            int ratersSumIndex = cursor.getColumnIndex(PLAYLIST_TABLE_RATERS_SUM);
            int ratersCountIndex = cursor.getColumnIndex(PLAYLIST_TABLE_RATERS_CNT);
            int userIdIndex = cursor.getColumnIndex(PLAYLIST_TABLE_USER_ID);
            String _id = cursor.getString(idIndex);
            String title = cursor.getString(titleIndex);
            String author = cursor.getString(authorIndex);
            String createDate = cursor.getString(createDateIndex);
            String photo = cursor.getString(photoIndex);
            String tags = cursor.getString(tagsIndex);
            int ratersSum = cursor.getInt(ratersSumIndex);
            int ratersCount = cursor.getInt(ratersCountIndex);
            String userId = cursor.getString(userIdIndex);
            Playlist pl = new Playlist(title,author);
            pl.setId(id);
            pl.setPhoto(photo);
            pl.setCreationDate(createDate);
            pl.setRatersCount(ratersCount);
            pl.setRatingSum(ratersSum);
            List<String> tagList = Arrays.asList(tags.split(","));
            pl.setTags(tagList);
            // pl.setUserId(userId)
            return pl;
        }
        return null;
    }

    // TODO: add getPlaylistByUser

    public static void add(SQLiteDatabase db, Playlist pl) {
        ContentValues values = new ContentValues();
        values.put(PLAYLIST_TABLE_ID, pl.getId());
        values.put(PLAYLIST_TABLE_TITLE, pl.getTitle());
        values.put(PLAYLIST_TABLE_AUTHOR, pl.getAuthor());
        values.put(PLAYLIST_TABLE_CREATION_DATE, pl.getCreationDate());
        values.put(PLAYLIST_TABLE_PHOTO, pl.getPhoto());
        String tags = concatStringsWSep(pl.getTags(),",");
        values.put(PLAYLIST_TABLE_TAGS, tags);
        values.put(PLAYLIST_TABLE_RATERS_SUM, pl.getRatingSum());
        values.put(PLAYLIST_TABLE_RATERS_CNT, pl.getRatersCount());
        //values.put(PLAYLIST_TABLE_USER_ID,pl.getUserId());

        db.insertWithOnConflict(PLAYLIST_TABLE, PLAYLIST_TABLE_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static String getLastUpdateDate(SQLiteDatabase db){
        return LastUpdateSql.getLastUpdate(db,PLAYLIST_TABLE);
    }
    public static void setLastUpdateDate(SQLiteDatabase db, String date){
        LastUpdateSql.setLastUpdate(db,PLAYLIST_TABLE, date);
    }

    private static String concatStringsWSep(Iterable<String> strings, String separator) {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for(String s: strings) {
            sb.append(sep).append(s);
            sep = separator;
        }
        return sb.toString();
    }

}
