package com.climber.everest.services;

import com.climber.everest.model.Evento;
import com.climber.everest.model.Resultado;
import com.climber.everest.model.Usuario;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("/evento/buscaEventos")
    Call<Resultado> buscaEventos(@Header("Authorization") String auth);

    @POST("/logar")
    Call<Resultado> logar(@Header("Content-type") String content_type, @Body Usuario usuario);
}
