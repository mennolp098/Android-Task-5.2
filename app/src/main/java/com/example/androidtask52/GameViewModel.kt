package com.example.androidtask52

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val gameRepository = GameRepository(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.Main)
    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()
    val gamesList = gameRepository.getAllGames()

    fun addGame(title: String, platform: String, releaseDate: Date) {
        val newGame = Game(
            title = title,
            platform = platform,
            releaseDate = releaseDate
        )

        if(isGameValid(newGame)) {
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    gameRepository.insertGame(newGame)
                }
                success.value = true
            }
        }
    }

    fun deleteAllGames() {
       mainScope.launch {
           withContext(Dispatchers.IO) {
               gameRepository.deleteAllGames()
           }
           success.value = true
       }
    }

    private fun isGameValid(game: Game): Boolean {
        return when {
            game.title.isBlank() -> {
                error.value = "Title must not be empty"
                false
            }
            else -> true
        }
    }

}