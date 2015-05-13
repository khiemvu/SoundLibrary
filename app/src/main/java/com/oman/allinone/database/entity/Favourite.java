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
    @DatabaseField(columnName = "_ID", id = true)
    private String id;
    @DatabaseField(columnName = "FILE_ID")
    private String fileID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
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

    public interface Properties {
        public static final String ID = "_ID";
        public static final String FILE_ID = "FILE_ID";
        public static final String CATEGORY_NAME = "CATEGORY_NAME";
        public static final String SUB_CATEGORY_NAME = "SUB_CATEGORY_NAME";
        public static final String FILE_NAME = "FILE_NAME";
    }
}
