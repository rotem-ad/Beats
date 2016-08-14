package com.example.rotem.beats.Model.ModelSql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rotem.beats.Model.Song;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Rotem on 09/08/2016.
 */

public class SongsSql {
    final static String SONG_TABLE = "songs";
    final static String SONG_TABLE_ID = "_id";
    final static String SONG_TABLE_TITLE = "title";
    final static String SONG_TABLE_ARTIST = "artist";
    final static String SONG_TABLE_PLAYLIST_ID = "playlistId";


    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + SONG_TABLE + " (" +
                SONG_TABLE_ID + " TEXT PRIMARY KEY," +
                SONG_TABLE_TITLE + " TEXT," +
                SONG_TABLE_ARTIST + " TEXT," +
                SONG_TABLE_PLAYLIST_ID + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + SONG_TABLE + ";");
    }


    public static List<Song> getAllSongsByPlaylist(SQLiteDatabase db, String playlistId) {
        String where = SONG_TABLE_ID + " = ?";
        String[] args = {playlistId};
        Cursor cursor = db.query(SONG_TABLE, null, where, args, null, null, null);
        List<Song> songs = new LinkedList<Song>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(SONG_TABLE_ID);
            int titleIndex = cursor.getColumnIndex(SONG_TABLE_TITLE);
            int artistIndex = cursor.getColumnIndex(SONG_TABLE_ARTIST);
            int plIdIndex = cursor.getColumnIndex(SONG_TABLE_PLAYLIST_ID);
            do {
                String id = cursor.getString(idIndex);
                String title = cursor.getString(titleIndex);
                String artist = cursor.getString(artistIndex);
                String plId = cursor.getString(plIdIndex);
                Song song = new Song();
                song.setArtist(artist);
                song.setTitle(title);
                song.setId(id);
                // song.setPlaylistId(plId)
                songs.add(song);
            } while (cursor.moveToNext());
        }
        return songs;
    }

    public static void add(SQLiteDatabase db, Song song) {
        ContentValues values = new ContentValues();
        values.put(SONG_TABLE_ID, song.getId());
        values.put(SONG_TABLE_TITLE, song.getTitle());
        values.put(SONG_TABLE_ARTIST, song.getArtist());
        //values.put(SONG_TABLE_PLAYLIST_ID,pl.getPlaylistId());
        db.insertWithOnConflict(SONG_TABLE, SONG_TABLE_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static String getLastUpdateDate(SQLiteDatabase db){
        return LastUpdateSql.getLastUpdate(db,SONG_TABLE);
    }
    public static void setLastUpdateDate(SQLiteDatabase db, String date){
        LastUpdateSql.setLastUpdate(db,SONG_TABLE, date);
    }
}
