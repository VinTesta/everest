package com.climber.everest.services;

import com.climber.everest.model.Evento;
import com.climber.everest.model.RequestBody;
import com.climber.everest.model.Resultado;
import com.climber.everest.model.Usuario;
import com.google.gson.JsonArray;

import org.json.JSONObject;

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

    @POST("/cadastrarUsuario")
    Call<Resultado> cadastroUsuario(@Header("Content-type") String content_type, @Body Usuario usuario);

    @POST("/usuario/alterarUsuario")
    Call<Resultado> alterarUsuario(@Header("Content-type") String content_type, @Header("Authorization") String auth, @Body Usuario infoAlt);

    @POST("/evento/adicionaEvento")
    Call<Resultado> adicionarEvento(@Header("Content-type") String content_type, @Header("Authorization") String auth, @Body RequestBody infoEvento);

    @POST("/evento/buscaInfoEvento")
    Call<Resultado> buscaInfoEvento(@Header("Content-type") String content_type, @Header("Authorization") String auth, @Body RequestBody infoEvento);
}
