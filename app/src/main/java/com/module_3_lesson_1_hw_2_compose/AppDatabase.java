package com.module_3_lesson_1_hw_2_compose;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Pet.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PetDao getPetDao();
}
