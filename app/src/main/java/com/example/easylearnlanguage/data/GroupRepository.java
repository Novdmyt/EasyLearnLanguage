package com.example.easylearnlanguage.data;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroupRepository {
    private final GroupDao dao;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    public GroupRepository(Context c){
        dao = AppDatabase.get(c).groupDao();
    }

    public LiveData<List<Group>> observeAll(){ return dao.observeAll(); }

    public void add(String title, String from, String to, int color){
        io.execute(() ->
                dao.insert(new Group(title, from, to, System.currentTimeMillis(), color))
        );
    }

    public void delete(Group g){
        io.execute(() -> dao.delete(g));
    }
}
