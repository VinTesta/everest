package com.climber.everest.activity;

import static com.climber.everest.activity.LoginActivity._usuarioLogado;
import static com.climber.everest.activity.LoginActivity.apiConfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.climber.everest.R;
import com.climber.everest.config.RetrofitConfig;
import com.climber.everest.fragment.HomeFragment;
import com.climber.everest.fragment.PerfilFragment;
import com.climber.everest.fragment.SocialFragment;
import com.climber.everest.model.Resultado;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

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
                    case R.id.ic_add_evento:
                        Intent i = new Intent(MainActivity.this, EventInfoActivity.class);
                        startActivity(i);
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