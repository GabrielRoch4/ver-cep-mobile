package com.example.vercep
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity() : AppCompatActivity(), Parcelable {

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainActivity> {
        override fun createFromParcel(parcel: Parcel): MainActivity {
            return MainActivity(parcel)
        }

        override fun newArray(size: Int): Array<MainActivity?> {
            return arrayOfNulls(size)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Elementos da interface
        val editLogradouro = findViewById<EditText>(R.id.edit_logradouro)
        val editBairro = findViewById<EditText>(R.id.edit_bairro)
        val editNumero = findViewById<EditText>(R.id.edit_numero)
        val editCidade = findViewById<EditText>(R.id.edit_cidade)
        val buscarCepButton = findViewById<Button>(R.id.buscarCep)
        val cepTitle = findViewById<TextView>(R.id.cepTitle)
        val cep = findViewById<TextView>(R.id.cep)
        val copiarCepButton = findViewById<Button>(R.id.copiarCep)
        val spinnerUf = findViewById<Spinner>(R.id.spinner_uf)

        // Definir os elementos "CEP" como invisíveis inicialmente
        cepTitle.visibility = View.GONE
        cep.visibility = View.GONE
        copiarCepButton.visibility = View.GONE

        // Configura o adaptador para o Spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.brazilian_states,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerUf.adapter = adapter
        }

        // Configura um listener para verificar a seleção
        spinnerUf.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedState = parent.getItemAtPosition(position).toString()
                if (selectedState == "Selecione um estado") {
                    // Nenhum estado foi selecionado, mostra uma dica ou faz algo
                } else {
                    // Um estado foi selecionado
                    Toast.makeText(
                        this@MainActivity,
                        "Estado selecionado: $selectedState",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nenhuma ação necessária aqui
            }
        }

        buscarCepButton.setOnClickListener {
            // Verificar se todos os campos estão preenchidos
            val logradouro = editLogradouro.text.toString()
            val bairro = editBairro.text.toString()
            val numero = editNumero.text.toString()
            val cidade = editCidade.text.toString()

            if (logradouro.isNotEmpty() && bairro.isNotEmpty() && numero.isNotEmpty() && cidade.isNotEmpty()) {
                // Simular a busca do CEP (aqui você implementaria a busca real)

                // Exibir os elementos "CEP" após encontrar o CEP
                cepTitle.visibility = View.VISIBLE
                cep.visibility = View.VISIBLE
                copiarCepButton.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
