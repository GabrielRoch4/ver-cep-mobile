package com.example.vercep

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ViaCepService {
    @GET("ws/{uf}/{cidade}/{logradouro}/json/")
    fun buscarCep(
        @Path("uf") uf: String,
        @Path("cidade") cidade: String,
        @Path("logradouro") logradouro: String
    ): Call<List<CepResponse>>
}