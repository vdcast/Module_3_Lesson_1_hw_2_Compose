package com.module_3_lesson_1_hw_2_compose;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PetDao {
    @Query("SELECT * FROM Pet")
    List<Pet> getAllPet();

    @Query("SELECt * FROM Pet WHERE id = :id")
    Pet getById(int id);

    @Query("SELECt * FROM Pet WHERE name = :name")
    Pet getByName(String name);

    @Insert
    void insert(Pet pet);

    @Update
    void update(Pet pet);

    @Delete
    void delete(Pet pet);
}
