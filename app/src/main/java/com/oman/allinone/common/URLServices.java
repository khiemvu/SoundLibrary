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
    public String getURLGetListVideos() {
        return URL + "video_category";
    }
    public String getURLGetListSubVideos(int parent_id) {
        return URL + "video_child_category?parent_id=" + parent_id;
    }

    public String getURLGetSubSounds(int parent_id) {
        return URL + "sound_child_category?parent_id=" + parent_id;
    }

    public String getURLGetFileSounds(int file_id) {
        return URL + "sound_file?category_id=" + file_id;
    }

    public String getURLGetFileVideos(int file_id) {

        return URL + "video_file?category_id=" + file_id;
    }
}
