package com.oman.allinone.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Khiemvx on 5/13/2015.
 */
@DatabaseTable(tableName = "FAVOURITE")
public class Favourite {
    public transient static final String TABLE_NAME = "FAVOURITE";
    @DatabaseField(columnName = "CATEGORY_NAME")
    protected String categoryName;
    @DatabaseField(columnName = "SUB_CATEGORY_NAME")
    protected String subCategoryName;
    @DatabaseField(columnName = "FILE_NAME")
    protected String fileName;
    @DatabaseField(columnName = "_ID", generatedId = true)
    private int id;
    @DatabaseField(columnName = "FILE_ID")
    private int fileID;
    @DatabaseField(columnName = "POSITION")
    private int position;
    @DatabaseField(columnName = "URL")
    private String url;
    @DatabaseField(columnName = "IS_FAVOURITE")
    private boolean isFavourite;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public interface Properties {
        public static final String ID = "_ID";
        public static final String FILE_ID = "FILE_ID";
        public static final String URL = "URL";
        public static final String IS_FAVOURITE = "IS_FAVOURITE";
        public static final String POSITION = "POSITION";
        public static final String CATEGORY_NAME = "CATEGORY_NAME";
        public static final String SUB_CATEGORY_NAME = "SUB_CATEGORY_NAME";
        public static final String FILE_NAME = "FILE_NAME";
    }
}
