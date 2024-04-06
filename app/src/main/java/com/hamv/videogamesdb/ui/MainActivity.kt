package com.hamv.videogamesdb.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
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

    private lateinit var gameAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as VideoGamesDBApp).repository

        //Iniciar adapter
        gameAdapter= GameAdapter(){ selectedGame ->
            //Click para actualizar o borrar un elemento
            val dialog = GameDialog(
                newGame = false,
                game=selectedGame,
                updateUI = {
                    updateUI()
                },
                message = { action ->
                    message(action)
                }
                )

            dialog.show(supportFragmentManager, "updatedIALOG")
        }

        binding.rvGames.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = gameAdapter
        }
        updateUI()
    }

    private fun updateUI(){
        lifecycleScope.launch() {
            games = repository.getAllGames()

            binding.tvSinRegistros.visibility=
                if (games.isEmpty()){
                    View.VISIBLE
                }else{
                    View.INVISIBLE
                }
            gameAdapter.updateList(games)

        }
    }

    fun click(view: View) {
        //Manejo el click del FAB
        //Paso de lambda como parametro
        val dialog = GameDialog(
            updateUI = {
                updateUI()
            },
            message = {action ->
                message(action)
            })
        dialog.show(supportFragmentManager, "insertDialog")

    }

    //Utilización de Toast o Sanckbar
    private fun message(text: String){
        //Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(getColor(R.color.white))
            .setBackgroundTint(Color.parseColor("#9E1734"))
            .show()
    }
}