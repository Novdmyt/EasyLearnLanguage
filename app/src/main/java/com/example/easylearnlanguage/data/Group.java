package com.example.easylearnlanguage.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "groups")
public class Group {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String title;        // назва набору
    public String fromLang;     // вихідна мова (напр., "DE")
    public String toLang;       // цільова мова (напр., "UK")
    public long createdAt;      // System.currentTimeMillis()
    public int color;           // ARGB колір плитки (опц.)

    public Group(String title, String fromLang, String toLang, long createdAt, int color) {
        this.title = title;
        this.fromLang = fromLang;
        this.toLang = toLang;
        this.createdAt = createdAt;
        this.color = color;
    }
}
