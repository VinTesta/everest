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
import android.widget.Toast;
import android.widget.Toolbar;

import com.climber.everest.R;
import com.climber.everest.config.RetrofitConfig;
import com.climber.everest.fragment.AddressEventFragment;
import com.climber.everest.fragment.BasicInfoEventFragment;
import com.climber.everest.fragment.HomeFragment;
import com.climber.everest.fragment.PerfilFragment;
import com.climber.everest.fragment.SocialFragment;
import com.climber.everest.model.Evento;
import com.climber.everest.model.Resultado;
import com.climber.everest.services.ApiService;
import com.climber.everest.services.InterfaceComunicacaoFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private LatLng localizacaoEvento;
    private Evento infoEvento;
    private Retrofit retrofit;
    private Resultado resReq;

    @Override
    public void setLocalizacao(LatLng latLng)
    {
        localizacaoEvento = latLng;
    }

    public void setEvento(Evento evento)
    {
        infoEvento = evento;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        basicInfo = new BasicInfoEventFragment();
        addressInfo = new AddressEventFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.tabFrameLayout, addressInfo);
        fragmentTransaction.add(R.id.tabFrameLayout, basicInfo);
        fragmentTransaction.hide(addressInfo);
        fragmentTransaction.commit();

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
                Log.e("Localização atual: ", "Latitude: "+localizacaoEvento.latitude+" Longitude: "+localizacaoEvento.longitude);
                Log.e("Data inicio", "Data inicio: "+infoEvento.dataInicioEvento);
                Log.e("Data inicio", "Data inicio: "+infoEvento.dataFimEvento);
                Log.e("Titulo evento", "Titulo: "+infoEvento.tituloevento);
                Log.e("Desc evento", "Desc: "+infoEvento.descevento);

                adicionaEvento(infoEvento);
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
                    evento.getDataFimEvento() != "" &&
                    evento.getDataInicioEvento() != "" &&
                    evento.getTituloevento() != ""
                )
            {

                apiService.adicionarEvento("application/json", apiConfig.token, evento)
                        .enqueue(new Callback<Resultado>() {
                            @Override
                            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                                Log.e(TAG, response.toString());

                                if(response.isSuccessful())
                                {
                                    resReq = response.body();

                                    Toast toast = Toast.makeText(getApplicationContext(), resReq.mensagem, Toast.LENGTH_SHORT);
                                    toast.show();

                                    finish();
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
                Toast toast = Toast.makeText(getApplicationContext(), "Houve um erro ao buscar os eventos", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}