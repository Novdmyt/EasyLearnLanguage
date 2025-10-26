package com.example.easylearnlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import com.example.easylearnlanguage.settings.Prefs;
import com.example.easylearnlanguage.settings.SettingsActivity;
import com.example.easylearnlanguage.temp.CorrectActivity;
import com.example.easylearnlanguage.temp.NewGroupActivity;
import com.example.easylearnlanguage.temp.NewWordsActivity;
import com.example.easylearnlanguage.temp.PlayActivity;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1) Ініціалізуємо Prefs після super.onCreate
        prefs = new Prefs(this);

        // 2) Застосовуємо тему
        AppCompatDelegate.setDefaultNightMode(prefs.getNightMode());

        // 3) Застосовуємо мову
        String tag = prefs.getLangTag();
        if ("system".equals(tag)) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList());
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag));
        }

        // 4) Підключаємо розмітку тільки після застосування теми/мови
        setContentView(R.layout.activity_main);

        // 5) Прив'язуємо в'ю і клік-логіку
        MaterialCardView tileNewGroup  = findViewById(R.id.tile_new_group);
        MaterialCardView tilePlay      = findViewById(R.id.tile_play);
        MaterialCardView tileNewWords  = findViewById(R.id.tile_new_words);
        MaterialCardView tileCorrect   = findViewById(R.id.tile_correct);
        ImageButton btnSettings        = findViewById(R.id.btn_settings);

        View.OnClickListener nav = v -> {
            int id = v.getId();
            Class<?> target;
            if (id == R.id.tile_new_group) {
                target = NewGroupActivity.class;
            } else if (id == R.id.tile_play) {
                target = PlayActivity.class;
            } else if (id == R.id.tile_new_words) {
                target = NewWordsActivity.class;
            } else {
                target = CorrectActivity.class;
            }
            startActivity(new Intent(MainActivity.this, target));
        };

        tileNewGroup.setOnClickListener(nav);
        tilePlay.setOnClickListener(nav);
        tileNewWords.setOnClickListener(nav);
        tileCorrect.setOnClickListener(nav);

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SettingsActivity.class))
        );
    }
}
