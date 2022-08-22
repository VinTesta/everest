package com.climber.everest.activity;

import static android.content.ContentValues.TAG;
import static com.climber.everest.activity.LoginActivity.apiConfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.climber.everest.R;
import com.climber.everest.config.RetrofitConfig;
import com.climber.everest.fragment.AddressEventFragment;
import com.climber.everest.fragment.BasicInfoEventFragment;
import com.climber.everest.fragment.HomeFragment;
import com.climber.everest.fragment.PerfilFragment;
import com.climber.everest.fragment.SocialFragment;
import com.climber.everest.model.Endereco;
import com.climber.everest.model.Evento;
import com.climber.everest.model.RequestBody;
import com.climber.everest.model.Resultado;
import com.climber.everest.services.ApiService;
import com.climber.everest.services.InterfaceComunicacaoFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventInfoActivity extends AppCompatActivity implements InterfaceComunicacaoFragment {

    private FrameLayout tabFrameLayout;
    private TabLayout tabLayout;
    private Fragment basicInfo;
    private Fragment addressInfo;
    private ImageView btnConfirmarEvento;
    private ImageView btnFecharEvento;
    private LatLng localizacaoEvento = new LatLng(0, 0);
    private Evento infoEvento = new Evento();
    private Retrofit retrofit;
    private Resultado resReq;
    private int statusEndereco;
    private ProgressBar progressBar2;
    private Intent intent;

    @Override
    public void setLocalizacao(LatLng latLng)
    {
        localizacaoEvento = latLng;
    }

    public void setEvento(Evento evento)
    {
        infoEvento = evento;
    }

    public void setEnableEndereco(int i){statusEndereco = i;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        basicInfo = new BasicInfoEventFragment();
        addressInfo = new AddressEventFragment();

        Bundle dados = getIntent().getExtras();
        if(dados != null) {
            String idevento = dados.getString("idevento");

            infoEvento.setIdevento(Integer.parseInt(idevento));
            basicInfo = BasicInfoEventFragment.newInstance(idevento);
            addressInfo = AddressEventFragment.newInstance(idevento);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.tabFrameLayout, addressInfo);
        fragmentTransaction.add(R.id.tabFrameLayout, basicInfo);
        fragmentTransaction.hide(addressInfo);
        fragmentTransaction.commit();

        progressBar2 = findViewById(R.id.progressBar2);

        tabLayout = findViewById(R.id.tabLayout);
        tabFrameLayout = findViewById(R.id.tabFrameLayout);

        ArrayList<Fragment> arrayFragment = new ArrayList<Fragment>();
        arrayFragment.add(basicInfo);
        arrayFragment.add(addressInfo);

        // region Instanciando a retrofit
        retrofit = RetrofitConfig.getRetrofit();
        // endregion

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.show(arrayFragment.get(tab.getPosition()));
                fragmentTransaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.hide(arrayFragment.get(tab.getPosition()));
                fragmentTransaction.commit();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });

        btnFecharEvento = findViewById(R.id.btnFecharEvento);
        btnConfirmarEvento = findViewById(R.id.btnConfirmarEvento);

        btnFecharEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnConfirmarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(infoEvento.idevento == null || infoEvento.idevento == 0) {
                    adicionaEvento(infoEvento);
                }
                else
                {
                    alterarEvento(infoEvento);
                }
            }
        });
    }

    private void adicionaEvento(Evento evento)
    {
        ApiService apiService = retrofit.create(ApiService.class);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();

            if(
                    apiConfig.token != "" &&
                    (evento.getDataFimEvento() != "" && evento.getDataFimEvento() != null) &&
                    (evento.getDataInicioEvento() != "" && evento.getDataInicioEvento() != null) &&
                    evento.getTituloevento() != ""
                )
            {

                RequestBody req = new RequestBody();
                req.evento = infoEvento;
                req.evento.endereco = new Endereco((char) statusEndereco, String.valueOf(localizacaoEvento.latitude), String.valueOf(localizacaoEvento.longitude));

                btnConfirmarEvento.setVisibility(View.GONE);
                progressBar2.setVisibility(View.VISIBLE);

                apiService.adicionarEvento("application/json", apiConfig.token, req)
                        .enqueue(new Callback<Resultado>() {
                            @Override
                            public void onResponse(Call<Resultado> call, Response<Resultado> response) {

                                if(response.isSuccessful())
                                {
                                    resReq = response.body();

                                    Toast toast = Toast.makeText(getApplicationContext(), resReq.mensagem, Toast.LENGTH_SHORT);
                                    toast.show();

                                    finish();
                                }
                                btnConfirmarEvento.setVisibility(View.VISIBLE);
                                progressBar2.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<Resultado> call, Throwable t) {
                                btnConfirmarEvento.setVisibility(View.VISIBLE);
                                progressBar2.setVisibility(View.GONE);
                            }
                        });
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Preencha todos os dados para prosseguir", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void alterarEvento(Evento evento)
    {
        ApiService apiService = retrofit.create(ApiService.class);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();

            if(
                    apiConfig.token != "" &&
                            (evento.getDataFimEvento() != "" && evento.getDataFimEvento() != null) &&
                            (evento.getDataInicioEvento() != "" && evento.getDataInicioEvento() != null) &&
                            evento.getTituloevento() != ""
            )
            {

                RequestBody req = new RequestBody();
                req.evento = infoEvento;
                req.evento.endereco = new Endereco((char) statusEndereco, String.valueOf(localizacaoEvento.latitude), String.valueOf(localizacaoEvento.longitude));

                btnConfirmarEvento.setVisibility(View.GONE);
                progressBar2.setVisibility(View.VISIBLE);

                apiService.alterarEvento("application/json", apiConfig.token, req.evento)
                        .enqueue(new Callback<Resultado>() {
                            @Override
                            public void onResponse(Call<Resultado> call, Response<Resultado> response) {

                                if(response.isSuccessful())
                                {
                                    resReq = response.body();

                                    Toast toast = Toast.makeText(getApplicationContext(), resReq.mensagem, Toast.LENGTH_SHORT);
                                    toast.show();
                                    finish();
                                }
                                btnConfirmarEvento.setVisibility(View.VISIBLE);
                                progressBar2.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<Resultado> call, Throwable t) {
                                btnConfirmarEvento.setVisibility(View.VISIBLE);
                                progressBar2.setVisibility(View.GONE);
                            }
                        });
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Preencha todos os dados para prosseguir", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}