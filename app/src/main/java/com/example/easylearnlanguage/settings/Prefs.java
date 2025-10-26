package com.example.easylearnlanguage.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private final SharedPreferences sp;

    public Prefs(Context c) {
        Context app = c.getApplicationContext();              // <- гарантовано не null
        sp = app.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public void setLangTag(String tag){ sp.edit().putString("lang_tag", tag).apply(); }
    public String getLangTag(){ return sp.getString("lang_tag","system"); }

    public void setNightMode(int mode){ sp.edit().putInt("night", mode).apply(); }
    public int getNightMode(){ return sp.getInt("night", -100); } // або MODE_NIGHT_FOLLOW_SYSTEM
}
