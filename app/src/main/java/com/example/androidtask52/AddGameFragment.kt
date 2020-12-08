package com.example.androidtask52

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_add_game.*
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddGameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()

    private var gamesList: List<Game> = arrayListOf<Game>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabSave.setOnClickListener {
            Log.w("TAG", "Inside click listener")
            saveGame()
        }

        observeGame()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item)
    }

    private fun observeGame() {
        //fill the text fields with the current text and title from the viewmodel
        viewModel.gamesList.observe(viewLifecycleOwner, Observer {
                games  ->
            games?.let {
                gamesList = games
            }

        })

        viewModel.error.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.success.observe(viewLifecycleOwner, Observer {     success ->
            //"pop" the backstack, this means we destroy this fragment and go back to the RemindersFragment
            findNavController().popBackStack()
        })
    }

    private fun saveGame() {
        Log.w("TAG", "Inside saveGame()")
        if (etYear.text.toString() == "" || etMonth.text.toString() == "" || etDay.text.toString() == "") {
            viewModel.error.value = "No valid date"
        } else {
            viewModel.addGame(etGameTitle.text.toString(), etPlatform.text.toString(), Date(etYear.text.toString().toInt(),etMonth.text.toString().toInt(),etDay.text.toString().toInt()))
        }
    }

    private fun onBackPressed() {
        findNavController().popBackStack()
    }

}