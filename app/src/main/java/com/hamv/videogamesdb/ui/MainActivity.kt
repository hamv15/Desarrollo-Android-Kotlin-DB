package com.hamv.videogamesdb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.hamv.videogamesdb.R
import com.hamv.videogamesdb.application.VideoGamesDBApp
import com.hamv.videogamesdb.data.GameRepository
import com.hamv.videogamesdb.data.db.model.GameEntity
import com.hamv.videogamesdb.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var games: List<GameEntity> = emptyList()
    private lateinit var repository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as VideoGamesDBApp).repository

        updateUI()
    }

    private fun updateUI(){
        lifecycleScope.launch() {
            games = repository.getAllGames()
        }
    }
}