package com.example.easylearnlanguage.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import com.example.easylearnlanguage.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class SettingsActivity extends AppCompatActivity {

    private Prefs prefs;
    private MaterialAutoCompleteTextView dropLang;
    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = new Prefs(this);
        root  = findViewById(android.R.id.content);

        // Toolbar back
        MaterialToolbar bar = findViewById(R.id.topAppBar);
        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed()
        );

        // Language dropdown
        dropLang = findViewById(R.id.drop_lang);

        String[] labels = new String[] {
                getString(R.string.system_default),
                getString(R.string.ukrainian),
                getString(R.string.german),
                getString(R.string.english)
        };
        String[] tags = new String[] {"system", "uk", "de", "en"};

        dropLang.setSimpleItems(labels);

        // current value
        String savedTag = prefs.getLangTag();
        int idx = 0; for (int i = 0; i < tags.length; i++) if (tags[i].equals(savedTag)) { idx = i; break; }
        dropLang.setText(labels[idx], false);

        dropLang.setOnItemClickListener((parent, view, position, id) -> {
            // 1) закрити список і зняти фокус
            dropLang.dismissDropDown();
            dropLang.clearFocus();
            root.requestFocus();

            // 2) зберегти й застосувати локаль
            String tag = tags[position];
            prefs.setLangTag(tag);
            if ("system".equals(tag)) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList());
            } else {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag));
            }

            // 3) м'який рестарт цього екрана без анімацій (щоб не блимало)
            restartWithoutAnimation();
        });
    }

    private void restartWithoutAnimation() {
        Intent i = getIntent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(i);
        overridePendingTransition(0, 0);
    }
}
