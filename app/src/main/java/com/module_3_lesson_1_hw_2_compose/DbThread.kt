package com.module_3_lesson_1_hw_2_compose

import android.util.Log

class DbThread(name: String): Runnable {


    init {
        Thread(this).start()
    }

    override fun run() {
        // var in example, I put in val
        val db = App.instance.database
        val petDao = db.petDao


        // code to add item in Database
//        val person = Person(2, "Valeron Johnson", 8000)
//        personDao.insert(person)


        val pets = petDao.allPet

        if (pets.isNotEmpty()){
            Log.d("MYLOG", "${pets[0].name} ${pets[1].name}")
        } else{
            Log.d("MYLOG", "DB is empty :)")
        }
    }

}