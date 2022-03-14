package com.climber.everest.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.metrics.Event;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.climber.everest.R;
import com.climber.everest.adapter.AdapterEvento;
import com.climber.everest.config.ApiConfig;
import com.climber.everest.config.RetrofitConfig;
import com.climber.everest.model.Evento;
import com.climber.everest.model.Resultado;
import com.climber.everest.model.Usuario;
import com.climber.everest.services.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity
                extends AppCompatActivity
                implements SearchView.OnQueryTextListener {

    // region Parâmetros privados
    private RecyclerView recyclerCardEvento;
    private List<Evento> eventos = new ArrayList<>();
    private AdapterEvento adapterEvento;
    private SearchView txtSearch;
    private AppCompatImageView btnOpenSearch;
    private Retrofit retrofit;
    // endregion

    // region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // region Instanciando o serchview
        txtSearch = (SearchView) findViewById(R.id.txtSearch);
        btnOpenSearch = (AppCompatImageView) findViewById(R.id.btnOpenSearch);
        //endregion

        // region Instanciando os parametros do recycler
        recyclerCardEvento = (RecyclerView) findViewById(R.id.recyclerCardEvento);
        recyclerCardEvento.setHasFixedSize(true);
        recyclerCardEvento.setLayoutManager(new LinearLayoutManager(this));
        recyclerCardEvento.setAdapter(adapterEvento);
        //endregion

        // region Instanciando a retrofit
        retrofit = RetrofitConfig.getRetrofit();
        // endregion

        // region Buscando os eventos


        // endregion

        // region Abrir a busca
        btnOpenSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOpenSearch.setVisibility(View.GONE);
                txtSearch.setVisibility(View.VISIBLE);
            }
        });

        // endregion

        // region Fechar a busca
        txtSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                btnOpenSearch.setVisibility(View.VISIBLE);
                txtSearch.setVisibility(View.GONE);
                return false;
            }
        });

        txtSearch.setOnQueryTextListener(this);

        // endregion

        // region AdapterEvento

        Evento eTeste = new Evento();
        eTeste.tituloevento = "Titulo de teste";
        eventos.add(eTeste);

        adapterEvento = new AdapterEvento(eventos, this);

        adapterEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        // endregion

        buscaEventos();

    }

    // endregion

    // region Busca eventos
    private ArrayList buscaEventos()
    {
        ApiService apiService = retrofit.create(ApiService.class);

        apiService.testeConexao()
            .enqueue(new Callback<Resultado>() {
                @Override
                public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                    Log.d("Resultado busca", "Resultado da busca foi: " + response.toString());
                }

                @Override
                public void onFailure(Call<Resultado> call, Throwable t) {
                    Log.e("Error", "O erro foi: " + t.toString());
                }
            });
        return new ArrayList();
    }
    // endregion

    // region Funções do searchview
    @Override
    public boolean onQueryTextSubmit(String s) {
        adapterEvento.filtrar(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapterEvento.filtrar(s);
        return false;
    }
    // endregion
}