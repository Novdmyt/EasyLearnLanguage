package com.example.easylearnlanguage.temp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easylearnlanguage.R;
import com.example.easylearnlanguage.data.Group;
import com.example.easylearnlanguage.ui.group.GroupAdapter;
import com.example.easylearnlanguage.ui.group.GroupViewModel;
import com.example.easylearnlanguage.ui.word.WordsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

        // Показуємо актуальні групи
        vm.groups().observe(this, adapter::submit);

        // FAB додає НОВУ КАРТКУ-ГРУПУ
        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> showAddDialog());

        // Свайп-видалення з UNDO
        attachSwipeToDelete(list);
    }

    private void onGroupClick(Group g){
        Intent it = new Intent(this, WordsActivity.class);
        it.putExtra(WordsActivity.EXTRA_GROUP_ID, g.id);
        it.putExtra(WordsActivity.EXTRA_GROUP_TITLE, g.title);
        startActivity(it);
    }

    private void showAddDialog(){
        var view = LayoutInflater.from(this).inflate(R.layout.dialog_add_group, null, false);
        EditText etTitle = view.findViewById(R.id.etTitle);
        MaterialAutoCompleteTextView dropColor = view.findViewById(R.id.dropColor);

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

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.title_new_group)
                .setView(view)
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    String title = etTitle.getText().toString().trim();
                    if (title.isEmpty()) { etTitle.setError(getString(R.string.title_new_group)); return; }

                    int idx = 0;
                    String chosen = dropColor.getText().toString();
                    for (int i = 0; i < labels.length; i++) if (labels[i].equals(chosen)) { idx = i; break; }
                    int color = values[idx];

                    // fromLang/toLang тимчасово порожні — ми їх ще не використовуємо
                    vm.add(title, "", "", color);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void attachSwipeToDelete(RecyclerView rv){
        ItemTouchHelper.SimpleCallback cb = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override public boolean onMove(RecyclerView r, RecyclerView.ViewHolder v, RecyclerView.ViewHolder t){ return false; }
            @Override public void onSwiped(RecyclerView.ViewHolder vh, int dir) {
                int pos = vh.getBindingAdapterPosition();
                Group g = adapter.getItem(pos);
                if (g == null) return;

                adapter.removeAt(pos);
                vm.deleteCascade(g);

                Snackbar.make(rv, R.string.group_deleted, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, v -> vm.add(g.title, g.fromLang, g.toLang, g.color))
                        .show();
            }
        };
        new ItemTouchHelper(cb).attachToRecyclerView(rv);
    }
}
