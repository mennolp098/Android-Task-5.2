package com.example.androidtask52

import android.content.Context
import androidx.lifecycle.LiveData

class GameRepository(context: Context) {

    private var gameDao: GameDao

    init {
        val database = GameRoomDatabase.getDatabase(context)
        gameDao = database!!.gameDao()
    }

    fun getAllGames(): LiveData<List<Game>?> {
        return gameDao.getAllGames()
    }

    suspend fun insertGame(game: Game) {
        gameDao.insertGame(game)
    }

    suspend fun deleteAllGames() {
        gameDao.deleteAllGames()
    }

    suspend fun deleteGame(game: Game) {
        gameDao.deleteGame(game)
    }
}