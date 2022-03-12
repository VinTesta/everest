package com.climber.everest.services;

import com.climber.everest.model.Evento;
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
    Call<Evento> buscaEventos(@Query("usuario") Usuario usuario, @Header("Authorization") String token);

}
