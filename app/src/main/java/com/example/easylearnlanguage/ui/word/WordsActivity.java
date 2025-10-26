package com.example.easylearnlanguage.ui.word;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easylearnlanguage.R;
import com.example.easylearnlanguage.data.Word;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WordsActivity extends AppCompatActivity {

    public static final String EXTRA_GROUP_ID = "group_id";
    public static final String EXTRA_GROUP_TITLE = "group_title";

    private WordsViewModel vm;
    private long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        Intent i = getIntent();
        groupId = i.getLongExtra(EXTRA_GROUP_ID, -1);
        String title = i.getStringExtra(EXTRA_GROUP_TITLE);
        if (groupId <= 0) finish();

        // Toolbar
        MaterialToolbar bar = findViewById(R.id.bar);
        bar.setTitle(title != null ? title : getString(R.string.title_new_words));
        bar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // List + VM
        RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        WordAdapter adapter = new WordAdapter();
        list.setAdapter(adapter);

        vm = new ViewModelProvider(this).get(WordsViewModel.class);
        vm.wordsByGroup(groupId).observe(this, adapter::submit);

        // FAB add
        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        var view = LayoutInflater.from(this).inflate(R.layout.dialog_add_word, null, false);
        EditText etFront = view.findViewById(R.id.etFront);
        EditText etBack  = view.findViewById(R.id.etBack);

        new AlertDialog.Builder(this)
                .setTitle(R.string.add_word)
                .setView(view)
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    String front = etFront.getText().toString().trim();
                    String back  = etBack.getText().toString().trim();
                    if (front.isEmpty() || back.isEmpty()) return;
                    vm.add(groupId, front, back);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
