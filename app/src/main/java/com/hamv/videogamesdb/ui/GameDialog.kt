package com.hamv.videogamesdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.hamv.videogamesdb.application.VideoGamesDBApp
import com.hamv.videogamesdb.data.GameRepository
import com.hamv.videogamesdb.data.db.model.GameEntity
import com.hamv.videogamesdb.databinding.GameDialogBinding
import kotlinx.coroutines.launch
import java.io.IOException

class GameDialog(
    private val newGame: Boolean = true,
    private var game: GameEntity = GameEntity(
    title = "",
    genere = "",
    developer = ""),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
    ): DialogFragment() {
    /*Los gragments siempre tienen que ir ligados a un activity
    * no pueden ir solitos. Se pueden ligar fragments ligados a otros
    * fragments pero el principal siempre debe estar ligado a un
    * activity*/

    //Por si aun no se termina de inflar, nos preparamos para el null
    private var _binding: GameDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: GameRepository

    //Se crea y configura el Dialog de forma inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = GameDialogBinding.inflate(requireActivity().layoutInflater)

        builder=AlertDialog.Builder(requireContext())

        repository = (requireContext().applicationContext as VideoGamesDBApp).repository

        binding.apply {
            binding.tietTitle.setText(game.title)
            binding.tietGenre.setText(game.genere)
            binding.tietDeveloper.setText(game.developer)
        }

        //Funcionamiento del dialog para insercion, actualizacion y borrado
        dialog = if (newGame)
            buildDialog(
                "Guardar",
                "Cancelar",
                {
                    //Accion de guardar
                    game.apply {
                        title=binding.tietTitle.text.toString().trim()
                        genere=binding.tietGenre.text.toString().trim()
                        developer=binding.tietDeveloper.text.toString().trim()
                    }
                    try {
                        lifecycleScope.launch {
                            repository.insertGame(game)
                        }
                        message("Juego guardado exitosamente")
                        updateUI()
                    }catch (e: IOException){
                        e.printStackTrace()
                        message("Error al guardar el juego")
                    }
                },
                {
                    //Acccion de cancelar

                })
        else
            buildDialog(
                "Actualizar",
                "Borrar",
                {
                    //Accion de actualizar
                    game.apply {
                        title=binding.tietTitle.text.toString().trim()
                        genere=binding.tietGenre.text.toString().trim()
                        developer=binding.tietDeveloper.text.toString().trim()
                    }
                    try {
                        lifecycleScope.launch {
                            repository.updateGame(game)
                        }
                        message("Juego actualizado exitosamente")
                        updateUI()
                    }catch (e: IOException){
                        e.printStackTrace()
                        message("Error al guardar el juego")
                    }
                },
                {
                    //Alert de confirmación
                    AlertDialog.Builder(requireContext())
                        .setTitle("Confirmación")
                        .setMessage("¿Realmente deseas eliminar el juego ${game.title}?")
                        .setPositiveButton("Aceptar"){_, _ ->
                            //Acción de borrar Juego
                            try {
                                lifecycleScope.launch {
                                    repository.deleteGame(game)
                                }
                                message("Juego eliminado exitosamente")
                                updateUI()
                            }catch (e: IOException){
                                e.printStackTrace()
                                message("Error al borrar el juego")
                            }
                        }
                        .setNegativeButton("Cancelar"){_, _ ->
                        }
                        .create()
                        .show()
                })
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Se va a llamar despues de que se muestra el dialogo en pantalla
    override fun onStart() {
        super.onStart()
        //Castear para obtener propiedades del dialog
        val alertDialog = dialog as AlertDialog
        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        binding.tietTitle.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.tietGenre.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.tietDeveloper.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

    }

    private fun validateFields(): Boolean =
        (binding.tietTitle.text.toString().isNotEmpty() &&
                binding.tietGenre.text.toString().isNotEmpty() &&
                binding.tietDeveloper.text.toString().isNotEmpty())

    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle("Juego")
            .setPositiveButton(btn1Text){_,_ ->
                //Acción para el boton positivo
                positiveButton()
            }
            .setNegativeButton(btn2Text){_,_ ->
                //Acción para el boton negativo
                negativeButton()
            }
            .create()


}