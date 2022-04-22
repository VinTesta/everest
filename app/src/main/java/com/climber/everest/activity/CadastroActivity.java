package com.climber.everest.activity;

import static android.content.ContentValues.TAG;
import static com.climber.everest.activity.LoginActivity.apiConfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.climber.everest.R;
import com.climber.everest.config.ApiConfig;
import com.climber.everest.config.RetrofitConfig;
import com.climber.everest.model.Resultado;
import com.climber.everest.model.Usuario;
import com.climber.everest.repository.UsuarioRepository;
import com.climber.everest.services.ApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CadastroActivity extends AppCompatActivity {

    private Button btnCadastrarUsuario;
    private EditText emailCadastroUsuario;
    private EditText senhaCadastroUsuario;
    private EditText confirmarSenhaCadastroUsuario;
    private EditText nomeCadastroUsuario;
    private ProgressBar progressBarCadastro;
    private Retrofit retrofit;
    private Resultado resReq;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private UsuarioRepository ur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        btnCadastrarUsuario = findViewById(R.id.btnCadastrarUsuario);
        emailCadastroUsuario = findViewById(R.id.emailCadastroUsuario);
        senhaCadastroUsuario = findViewById(R.id.senhaCadastroUsuario);
        confirmarSenhaCadastroUsuario = findViewById(R.id.confirmarSenhaCadastroUsuario);
        nomeCadastroUsuario = findViewById(R.id.nomeCadastroUsuario);
        progressBarCadastro = findViewById(R.id.progressBarCadastro);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        retrofit = RetrofitConfig.getRetrofit();
        ur = new UsuarioRepository();
        resReq = new Resultado();

        btnCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    progressBarCadastro.setVisibility(View.VISIBLE);

                    if(!emailCadastroUsuario.getText().toString().isEmpty() &&
                        !senhaCadastroUsuario.getText().toString().isEmpty() &&
                        !nomeCadastroUsuario.getText().toString().isEmpty() &&
                        senhaCadastroUsuario.getText().toString().equals(confirmarSenhaCadastroUsuario.getText().toString())
                    )
                    {
                        Usuario usuario = new Usuario();
                        usuario.setEmailusuario(emailCadastroUsuario.getText().toString());
                        usuario.setSenhausuario(senhaCadastroUsuario.getText().toString());
                        usuario.setNomeusuario(nomeCadastroUsuario.getText().toString());

                        cadastrarUsuario(usuario);
                    }
                    else
                    {
                        Toast.makeText(CadastroActivity.this.getApplicationContext(), "A campos vazis ou as senhas não coincidem! Tente novamente!", Toast.LENGTH_SHORT).show();

                        progressBarCadastro.setVisibility(View.GONE);
                    }
                }
                catch (Exception exception)
                {
                    progressBarCadastro.setVisibility(View.GONE);
                    Toast.makeText(CadastroActivity.this.getApplicationContext(), "Error!!! "+ exception.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirLogin(View view)
    {
        Intent i =  new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(i);

        finish();
    }

    public void cadastrarUsuario(Usuario usuario)
    {
        ApiService apiService = retrofit.create(ApiService.class);

        apiService.cadastroUsuario("application/json", usuario)
                .enqueue(new Callback<Resultado>() {
                    @Override
                    public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                        if (response.isSuccessful()) {
                            resReq = response.body();

                            if (resReq.status.equals("200")) {
                                // region CRIA USUARIO FIREBASE
                                mAuth.createUserWithEmailAndPassword(usuario.getEmailusuario(), usuario.getSenhausuario())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "createUserWithEmail:success");

                                                    FirebaseUser usuarioFb = FirebaseAuth.getInstance().getCurrentUser();
                                                    if (usuarioFb != null) {
                                                        mDatabase.child("token").child(usuarioFb.getUid()).setValue(resReq.token);

                                                        Intent i = new Intent(CadastroActivity.this, MainActivity.class);
                                                        startActivity(i);
                                                        finish();

                                                        Toast.makeText(CadastroActivity.this.getApplicationContext(), resReq.mensagem, Toast.LENGTH_SHORT).show();

                                                        progressBarCadastro.setVisibility(View.GONE);

                                                        apiConfig.token = resReq.token;
                                                    }
                                                } else {

                                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                }
                                            }
                                        });
                                //endregion

                            }
                        }
                        else
                        {
                            Log.e("Houve um erro", "erro: "+response.toString());
                            Toast.makeText(CadastroActivity.this.getApplicationContext(), "Houve um erro na comunicação com a API", Toast.LENGTH_SHORT).show();

                            progressBarCadastro.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Resultado> call, Throwable t) {
                        Log.e("Error", "O erro foi: " + t.toString());
                        Toast.makeText(CadastroActivity.this.getApplicationContext(), "Houve um erro na comunicação com a API", Toast.LENGTH_SHORT).show();

                        progressBarCadastro.setVisibility(View.GONE);
                    }
                });
    }
}