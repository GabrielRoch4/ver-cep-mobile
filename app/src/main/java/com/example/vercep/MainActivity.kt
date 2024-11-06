package com.example.vercep
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinnerUf = findViewById<Spinner>(R.id.spinner_uf)

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
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedState = parent.getItemAtPosition(position).toString()
                if (selectedState == "Selecione um estado") {
                    // Nenhum estado foi selecionado, mostra uma dica ou faz algo
                } else {
                    // Um estado foi selecionado
                    Toast.makeText(this@MainActivity, "Estado selecionado: $selectedState", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nenhuma ação necessária aqui
            }
        }
    }
}
