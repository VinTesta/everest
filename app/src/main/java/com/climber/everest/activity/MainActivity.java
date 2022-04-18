package com.climber.everest.activity;

import static com.climber.everest.activity.LoginActivity.apiConfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private Resultado resReq;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // endregion

    // region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.mainToolbar);
        toolbar.setTitle("Everest");
        setSupportActionBar(toolbar);
        // region Instanciando o serchview
        txtSearch = (SearchView) findViewById(R.id.txtSearch);
        btnOpenSearch = (AppCompatImageView) findViewById(R.id.btnOpenSearch);
        //endregion

        // region Instanciando a retrofit
        retrofit = RetrofitConfig.getRetrofit();
        buscaEventos();
        // endregion

    }
    // endregion


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_logout:
                logout();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout()
    {
        try
        {
            mAuth.signOut();
        }
        catch(Exception ex)
        {

        }
    }


    // region Busca eventos
    private void buscaEventos()
    {
        ApiService apiService = retrofit.create(ApiService.class);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();

            if(apiConfig.token != "")
            {
                apiService.buscaEventos(apiConfig.token)
                        .enqueue(new Callback<Resultado>() {
                            @Override
                            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                                if(response.isSuccessful())
                                {
                                    resReq = response.body();
                                    configRecyclerViewEventos(resReq.eventos);
                                }
                            }

                            @Override
                            public void onFailure(Call<Resultado> call, Throwable t) {
                                Log.e("Error", "O erro foi: " + t.toString());
                            }
                        });
            }
            else
            {
                Toast toast = Toast.makeText(MainActivity.this.getApplicationContext(), "Houve um erro ao buscar os eventos", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
    // endregion

    // region CONFIGURAR RECYCLER VIEW
    public void configRecyclerViewEventos(ArrayList<Evento> eventos)
    {
        adapterEvento = new AdapterEvento(eventos, this);
        adapterEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        recyclerCardEvento = (RecyclerView) findViewById(R.id.recyclerCardEvento);
        recyclerCardEvento.setHasFixedSize(true);
        recyclerCardEvento.setLayoutManager(new LinearLayoutManager(this));
        recyclerCardEvento.setAdapter(adapterEvento);

        // region SEARCH VIEW EVENTOS
        btnOpenSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOpenSearch.setVisibility(View.GONE);
                txtSearch.setVisibility(View.VISIBLE);
            }
        });

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