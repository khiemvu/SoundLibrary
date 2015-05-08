package com.oman.allinone.dto;

/**
 * Created by Khiemvx on 5/7/2015.
 */
public class ListSoundDTO {
    private int id;
    private int parent_id;
    private int lft;
    private int rgt;
    private int level;
    private String title;
    private String alias;

    public ListSoundDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLft() {
        return lft;
    }

    public void setLft(int lft) {
        this.lft = lft;
    }

    public int getRgt() {
        return rgt;
    }

    public void setRgt(int rgt) {
        this.rgt = rgt;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
