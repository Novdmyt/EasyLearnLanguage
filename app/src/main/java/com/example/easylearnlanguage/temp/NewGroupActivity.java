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
        EditText etFrom  = view.findViewById(R.id.etFrom);
        EditText etTo    = view.findViewById(R.id.etTo);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_new_group))
                .setView(view)
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    String title = etTitle.getText().toString().trim();
                    String from  = etFrom.getText().toString().trim();
                    String to    = etTo.getText().toString().trim();
                    if (title.isEmpty()) { etTitle.setError("Required"); return; }
                    if (from.isEmpty())  from = "DE";
                    if (to.isEmpty())    to = "UK";
                    vm.add(title, from, to, 0xFF4BA3E3); // тимчасовий колір
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
