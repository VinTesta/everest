package com.climber.everest.activity;

import static com.climber.everest.activity.LoginActivity._usuarioLogado;
import static com.climber.everest.activity.LoginActivity.apiConfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.climber.everest.R;
import com.climber.everest.config.RetrofitConfig;
import com.climber.everest.model.Resultado;
import com.climber.everest.model.Usuario;
import com.climber.everest.repository.UsuarioRepository;
import com.climber.everest.services.ApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AlterarUsuarioActivity extends AppCompatActivity {

    private Button btnCancelarAlt;
    private Button btnAlterar;
    private Retrofit retrofit;
    private EditText inputNome;
    private EditText inputEmail;
    private Resultado resReq;
    private EditText inputSenha;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_usuario);

        btnCancelarAlt = findViewById(R.id.btnCancelarAlt);
        btnAlterar = findViewById(R.id.btnAlterar);
        inputNome = findViewById(R.id.inputNome);
        inputEmail = findViewById(R.id.inputEmail);
        inputSenha = findViewById(R.id.inputSenha);
        progressBar = findViewById(R.id.progressBar);

        retrofit = RetrofitConfig.getRetrofit();

        btnCancelarAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAlterar.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                if(inputNome.getText().toString() != "" && inputEmail.getText().toString() != "" && inputSenha.getText().toString() != "")
                {
                    ApiService apiService = retrofit.create(ApiService.class);
                    Usuario user = new Usuario();
                    user.setNomeusuario(inputNome.getText().toString());
                    user.setEmailusuario(inputEmail.getText().toString());
                    FirebaseUser usuarioAutenticado = FirebaseAuth.getInstance().getCurrentUser();

                    // Get auth credentials from the user for re-authentication
                    AuthCredential credential = EmailAuthProvider.getCredential(_usuarioLogado.getEmailusuario(), inputSenha.getText().toString()); // Current Login Credentials

                    // Prompt the user to re-provide their sign-in credentials
                    usuarioAutenticado.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                apiService.alterarUsuario("application/json", apiConfig.token, user)
                                .enqueue(new Callback<Resultado>() {
                                    @Override
                                    public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                                        if (response.isSuccessful()) {
                                            resReq = response.body();
                                            if (resReq.status.equals("200")) {

                                                Log.d("value", "User re-authenticated.");

                                                FirebaseUser usuarioAutenticado = FirebaseAuth.getInstance().getCurrentUser();
                                                usuarioAutenticado.updateEmail(user.emailusuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            _usuarioLogado.setNomeusuario(user.nomeusuario);
                                                            _usuarioLogado.setEmailusuario(user.emailusuario);

                                                            UsuarioRepository ur = new UsuarioRepository();
                                                            ur.loadToken(_usuarioLogado.getEmailusuario(), inputSenha.getText().toString());

//                                                            Toast.makeText(AlterarUsuarioActivity.this.getApplicationContext(), resReq.mensagem, Toast.LENGTH_SHORT).show();
                                                            finish();

                                                            btnAlterar.setEnabled(true);
                                                            progressBar.setVisibility(View.GONE);
                                                        }
                                                    }
                                                });

                                                btnAlterar.setEnabled(true);
                                                progressBar.setVisibility(View.GONE);
                                            }

                                        } else {
                                            Toast.makeText(AlterarUsuarioActivity.this.getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Resultado> call, Throwable t) {

                                        btnAlterar.setEnabled(true);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(AlterarUsuarioActivity.this.getApplicationContext(), "Senha incorreta!", Toast.LENGTH_SHORT).show();
                                btnAlterar.setEnabled(true);
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    });
                }
                else
                {
                    Toast toast = Toast.makeText(AlterarUsuarioActivity.this.getApplicationContext(), "Campos vazios, por favor preencha-os para continuar!", Toast.LENGTH_SHORT);
                    toast.show();

                    btnAlterar.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}