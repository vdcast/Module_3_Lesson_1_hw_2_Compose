package com.module_3_lesson_1_hw_2_compose;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Pet {
    @PrimaryKey
    public int id;
    public String type;
    public String name;
    public String height;
    public String weight;

    public Pet(int id, String type, String name, String height, String weight) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.height = height;
        this.weight = weight;
    }
}
