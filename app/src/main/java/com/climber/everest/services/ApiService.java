package com.climber.everest.services;

import com.climber.everest.model.Evento;
import com.climber.everest.model.Resultado;
import com.climber.everest.model.Usuario;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("evento/buscaEvento")
    Call<Resultado> buscaEventos();

    @GET("ws/13483110/json/")
    Call<Resultado> buscaCep();

    @GET("evento/testeConexao")
    Call<Resultado> testeConexao();
}
