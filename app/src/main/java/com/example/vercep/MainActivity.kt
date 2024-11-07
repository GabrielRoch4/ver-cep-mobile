package com.example.vercep

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

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

        editCidade.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                esconderTeclado(v)
                v.clearFocus()
                true
            } else {
                false
            }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val viaCepService = retrofit.create(ViaCepService::class.java)

        buscarCepButton.setOnClickListener {
            val logradouro = editLogradouro.text.toString()
            val cidade = editCidade.text.toString()
            val uf = spinnerUf.selectedItem.toString()

            if (logradouro.isNotEmpty() && cidade.isNotEmpty() && uf.isNotEmpty()) {
                val call = viaCepService.buscarCep(uf, cidade, logradouro)

                call.enqueue(object : Callback<List<CepResponse>> {
                    override fun onResponse(call: Call<List<CepResponse>>, response: Response<List<CepResponse>>) {
                        if (response.isSuccessful) {
                            val cepResponse = response.body()?.firstOrNull()
                            if (cepResponse != null) {
                                cep.text = cepResponse.cep
                                cepTitle.visibility = View.VISIBLE
                                cep.visibility = View.VISIBLE
                                copiarCepButton.visibility = View.VISIBLE
                                copiarCepButton.setOnClickListener {
                                    copiarParaAreaDeTransferencia(this@MainActivity, cep.text.toString())
                                }
                            } else {
                                Toast.makeText(this@MainActivity, "CEP não encontrado", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<CepResponse>>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Erro na busca do CEP", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun copiarParaAreaDeTransferencia(context: Context, texto: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("texto", texto)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Texto copiado para a área de transferência", Toast.LENGTH_SHORT).show()
    }

    private fun esconderTeclado(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
