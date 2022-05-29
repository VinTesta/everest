package com.climber.everest.activity;

import static android.content.ContentValues.TAG;
import static com.climber.everest.activity.LoginActivity._usuarioLogado;
import static com.climber.everest.activity.LoginActivity.apiConfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.climber.everest.fragment.HomeFragment;
import com.climber.everest.fragment.PerfilFragment;
import com.climber.everest.fragment.SocialFragment;
import com.climber.everest.model.Evento;
import com.climber.everest.model.Resultado;
import com.climber.everest.model.Usuario;
import com.climber.everest.services.ApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity
                extends AppCompatActivity {

    // region Parâmetros privados
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

        // region Instanciando a retrofit
        retrofit = RetrofitConfig.getRetrofit();
        // endregion

        configuraBottomNavigation();
    }
    // endregion


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    public void configuraBottomNavigation()
    {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);

//        bottomNavigationViewEx.setTextVisibility(false);
//        bottomNavigationViewEx.enableAnimation(false);
//        bottomNavigationViewEx.enableItemShiftingMode(true);
//        bottomNavigationViewEx.enableShiftingMode(false);

        habilitarNavegacao(bottomNavigationViewEx);
    }

    /*
     *
     * @param viewEx
     */
    private void habilitarNavegacao(BottomNavigationViewEx viewEx)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction mainFragment = fragmentManager.beginTransaction();

        mainFragment.replace(R.id.mainFrameLayout, new HomeFragment()).commit();
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId())
                {
                    case R.id.ic_home:
                        fragmentTransaction.replace(R.id.mainFrameLayout, new HomeFragment()).commit();
                        break;
                    case R.id.ic_perfil:
                        fragmentTransaction.replace(R.id.mainFrameLayout, new PerfilFragment()).commit();
                        break;
                    case R.id.ic_social:
                        fragmentTransaction.replace(R.id.mainFrameLayout, new SocialFragment()).commit();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_logout:
                logout();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout()
    {
        try
        {
            mAuth.signOut();
            apiConfig.token = null;
            _usuarioLogado = null;
        }
        catch(Exception ex)
        {

        }
    }

    private void validaDadosUsuarioLogado(View view)
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
}