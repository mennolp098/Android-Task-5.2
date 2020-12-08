package com.example.androidtask52

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_games.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GamesFragment : Fragment() {
    private lateinit var gameRepository: GameRepository
    private val viewModel: GameViewModel by viewModels()

    private var gamesList = arrayListOf<Game>()
    private var gamesAdapter = GamesAdapter(gamesList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_games, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameRepository = GameRepository(requireContext())

        observeAddGameResult()
        initViews()
        gamesAdapter.notifyDataSetChanged()
    }

    private fun observeAddGameResult() {
        viewModel.gamesList.observe(viewLifecycleOwner, Observer{ games ->
            games?.let {
                gamesList.clear()
                gamesList.addAll(games)
                gamesAdapter.notifyDataSetChanged()
            }
            gamesAdapter.notifyDataSetChanged()
        })
        gamesAdapter.notifyDataSetChanged()
    }

    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter
        rvGames.layoutManager = LinearLayoutManager(context)
        rvGames.adapter = gamesAdapter
        gamesAdapter.notifyDataSetChanged()

        createItemTouchHelper().attachToRecyclerView(rvGames)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.w("Delete", "Inside onOptionsItemSelected")
        return when (item.itemId) {
            R.id.action_clear -> {
                viewModel.deleteAllGames()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val gameToDelete = gamesList[position]
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        gameRepository.deleteGame(gameToDelete)
                    }
                    gamesAdapter.notifyDataSetChanged()
                }
            }
        }
        return ItemTouchHelper(callback)
    }
}