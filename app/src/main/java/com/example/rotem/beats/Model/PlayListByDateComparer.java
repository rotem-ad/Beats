package com.example.rotem.beats.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

/**
 * Created by Rotem on 11/08/2016.
 */
public class PlayListByDateComparer implements Comparator<Playlist> {

    @Override
    public int compare(Playlist pl1, Playlist pl2) {
        return compareStrDates(pl1.getCreationDate(),pl2.getCreationDate());
    }

    private int compareStrDates(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            if (sdf.parse(date1).after(sdf.parse(date2))) {
                return -1;
            }
            else if (sdf.parse(date1).before(sdf.parse(date2))) {
                return 1;
            }
            else {
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -2;
    }
}
