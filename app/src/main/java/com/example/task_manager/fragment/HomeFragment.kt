package com.example.task_manager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView

import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.task_manager.MainActivity
import com.example.task_manager.R
import com.example.task_manager.adapter.NoteAdapter
import com.example.task_manager.databinding.FragmentHomeBinding
import com.example.task_manager.Note
import com.example.task_manager.NoteViewModel

class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener,MenuProvider {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }


   override fun onViewCreated(view: View, savedInstanceState: Bundle?){

       super.onViewCreated(view,savedInstanceState)

       val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner,Lifecycle.State.RESUMED)

       notesViewModel = (activity as MainActivity).notesViewModel
        setUpHomeRecyclerView()


       binding.addNoteFab.setOnClickListener {
           it.findNavController().navigate(R.id.action_homeFragment3_to_addNoteFragment3)
       }

    }

    private fun updateUI(note: List<Note>?){

        if(note != null){

            if(note.isNotEmpty()){

                binding.homeRecyclerView.visibility = View.VISIBLE
            }else{

                binding.homeRecyclerView.visibility = View.GONE

            }
        }
    }

    private fun setUpHomeRecyclerView(){
        noteAdapter = NoteAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        activity?.let{
            notesViewModel.getAllNotes().observe(viewLifecycleOwner){ note ->

              noteAdapter.differ.submitList(note)
                updateUI(note)

            }
        }
    }

    private fun searchNote(query: String?){

        val searchQuery = "%$query"

        notesViewModel.searchNote(searchQuery).observe(this){ list ->
            noteAdapter.differ.submitList(list)
        }
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        if(newText != null ){
            searchNote(newText)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu,menu)

        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)

    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

}

