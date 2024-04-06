package com.hamv.videogamesdb.ui

import androidx.recyclerview.widget.RecyclerView
import com.hamv.videogamesdb.data.db.model.GameEntity
import com.hamv.videogamesdb.databinding.GameElementBinding

class GameViewHolder(private val binding: GameElementBinding):  RecyclerView.ViewHolder(binding.root){

    //Llamar los objetos de la vista para poder efectuar efectos como el clickliSTENER
    val ivIcon = binding.ivIcon

    fun bind(game: GameEntity){
        binding.apply {
            tvTitle.text = game.title
            tvGenre.text = game.genere
            tvDeveloper.text = game.developer
        }
    }
}