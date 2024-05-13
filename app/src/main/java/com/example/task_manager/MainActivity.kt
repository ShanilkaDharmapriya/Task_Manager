package com.example.task_manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var notesViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
    }

    private fun setupViewModel(){
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelProviderFactory = ViewModelFactory(application, noteRepository)
        notesViewModel = ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]
    }
}