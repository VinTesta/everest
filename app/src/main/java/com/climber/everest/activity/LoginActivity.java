package com.climber.everest.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.climber.everest.R;
import com.climber.everest.config.ApiConfig;
import com.climber.everest.config.RetrofitConfig;
import com.climber.everest.model.Resultado;
import com.climber.everest.model.Usuario;
import com.climber.everest.services.ApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private Resultado resReq;
    private EditText inputEmailUsuario;
    private EditText inputSenhaUsuario;
    private Button btnLogar;
    private ProgressBar barraLoad;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public static ApiConfig apiConfig = new ApiConfig();
    private TextView btnAbrirCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        inputEmailUsuario = findViewById(R.id.inputEmailUsuario);
        inputSenhaUsuario = findViewById(R.id.inputSenhaUsuario);
        btnLogar = findViewById(R.id.btnLogar);
        barraLoad = findViewById(R.id.barraLoad);
        btnAbrirCadastro = findViewById(R.id.btnAbrirCadastro);

        retrofit = RetrofitConfig.getRetrofit();

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barraLoad.setVisibility(View.VISIBLE);

                logar();
            }
        });

        verificaLogin();
    }

    public void verificaLogin()
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        barraLoad.setVisibility(View.VISIBLE);

        if(currentUser != null)
        {
            currentUser.reload();

            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("token").child(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful())
                    {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else
                    {
                        apiConfig.token = String.valueOf(task.getResult().getValue());

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }
            });
        }
        else
        {
            barraLoad.setVisibility(View.GONE);
            inputEmailUsuario.setVisibility(View.VISIBLE);
            inputSenhaUsuario.setVisibility(View.VISIBLE);
            btnLogar.setVisibility(View.VISIBLE);
            btnAbrirCadastro.setVisibility(View.VISIBLE);
        }
    }

    public void logar()
    {
        if(!inputEmailUsuario.getText().toString().isEmpty() && !inputSenhaUsuario.getText().toString().isEmpty()) {
            ApiService apiService = retrofit.create(ApiService.class);

            Usuario user = new Usuario();

            user.setEmailusuario(inputEmailUsuario.getText().toString());
            user.setSenhausuario(inputSenhaUsuario.getText().toString());

            apiService.logar("application/json", user)
                    .enqueue(new Callback<Resultado>() {
                        @Override
                        public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                            if (response.isSuccessful()) {
                                resReq = response.body();

                                barraLoad.setVisibility(View.GONE);
                                Log.e(TAG, resReq.status);
                                if (resReq.status.equals("200")) {
                                    // region CRIA USUARIO FIREBASE
                                    mAuth.signInWithEmailAndPassword(user.getEmailusuario(), user.getSenhausuario())
                                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "createUserWithEmail:success");

                                                        FirebaseUser usuarioFb = FirebaseAuth.getInstance().getCurrentUser();
                                                        if (usuarioFb != null) {
                                                            mDatabase.child("token").child(usuarioFb.getUid()).setValue(resReq.token);

                                                            apiConfig.token = resReq.token;
                                                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    } else {

                                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                                Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                    //endregion
                                } else {
                                    Toast toast = Toast.makeText(LoginActivity.this.getApplicationContext(), "Houve um erro ao fazer login", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                            else
                            {
                                Log.e("Houve um erro", "erro: "+response.toString());
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

            Toast.makeText(LoginActivity.this.getApplicationContext(), "Preencha todos os campos para continuar", Toast.LENGTH_SHORT).show();

            barraLoad.setVisibility(View.GONE);
        }

    }

    public void abrirCadastro(View view)
    {
        Intent i =  new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(i);
        finish();
    }
}