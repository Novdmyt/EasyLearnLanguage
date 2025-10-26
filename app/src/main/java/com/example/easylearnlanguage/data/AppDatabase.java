package com.example.easylearnlanguage.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = { Group.class },
        version = 2,                 // підняли версію після зміни схеми
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract GroupDao groupDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase get(Context ctx) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    ctx.getApplicationContext(),
                                    AppDatabase.class,
                                    "easylearn.db"
                            )
                            // На етапі розробки: якщо версія зросте, БД перебудується наново
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
