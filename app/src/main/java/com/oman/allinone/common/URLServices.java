package com.oman.allinone.common;

/**
 * Created by Khiemvx on 5/7/2015.
 */
public class URLServices {
    private final static String URL = "http://duhocnhatban-homare.com/api/";
    public static URLServices instance;

    public static URLServices getInstance() {
        if (instance == null) {
            instance = new URLServices();
        }
        return instance;
    }

    public String getURLGetListSounds() {
        return URL + "sound_category";
    }
}
