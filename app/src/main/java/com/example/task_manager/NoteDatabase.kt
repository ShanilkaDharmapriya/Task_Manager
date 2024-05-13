package com.example.task_manager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object{

        @Volatile
        private var instant: NoteDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instant ?:
        synchronized(LOCK){
            instant ?:
            createDatabase(context).also{
                instant = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder (

                context.applicationContext,
                NoteDatabase::class.java,
                "note_db"

        ).build()


    }


}