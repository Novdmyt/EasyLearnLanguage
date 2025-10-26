package com.example.easylearnlanguage.temp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easylearnlanguage.R;
import com.example.easylearnlanguage.data.Group;
import com.example.easylearnlanguage.ui.group.GroupAdapter;
import com.example.easylearnlanguage.ui.group.GroupViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class NewGroupActivity extends AppCompatActivity {

    private GroupViewModel vm;
    private GroupAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        vm = new ViewModelProvider(this).get(GroupViewModel.class);
        RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupAdapter(this::onGroupClick);
        list.setAdapter(adapter);

        vm.groups().observe(this, adapter::submit);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> showAddDialog());
    }

    private void onGroupClick(Group g){
        Toast.makeText(this, "Open group: " + g.title, Toast.LENGTH_SHORT).show();
        // TODO: перейти на екран слів певної групи
    }

    private void showAddDialog(){
        var view = LayoutInflater.from(this).inflate(R.layout.dialog_add_group, null, false);
        EditText etTitle = view.findViewById(R.id.etTitle);
        MaterialAutoCompleteTextView dropColor = view.findViewById(R.id.dropColor);

        // Мітки та кольори (перший пункт — "Без кольору")
        String[] labels = new String[] {
                getString(R.string.color_none),
                getString(R.string.color_blue),
                getString(R.string.color_yellow),
                getString(R.string.color_red),
                getString(R.string.color_green)
        };
        int[] values = new int[] {
                0,
                getColor(R.color.blue),
                getColor(R.color.yellow),
                getColor(R.color.red),
                getColor(R.color.green)
        };
        dropColor.setSimpleItems(labels);
        dropColor.setText(labels[0], false);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_new_group))
                .setView(view)
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    String title = etTitle.getText().toString().trim();
                    if (title.isEmpty()) {
                        Toast.makeText(this, R.string.title_new_group, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // вибраний індекс
                    int idx = 0;
                    String chosen = dropColor.getText().toString();
                    for (int i = 0; i < labels.length; i++) if (labels[i].equals(chosen)) { idx = i; break; }

                    int color = values[idx];

                    // тимчасово не питаємо мови — кладемо порожні значення
                    vm.add(title, "", "", color);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

}
