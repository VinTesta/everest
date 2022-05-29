package com.climber.everest.repository;

import static android.content.ContentValues.TAG;

import static com.climber.everest.activity.LoginActivity._usuarioLogado;
import static com.climber.everest.activity.LoginActivity.apiConfig;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.climber.everest.activity.LoginActivity;
import com.climber.everest.activity.MainActivity;
import com.climber.everest.config.RetrofitConfig;
import com.climber.everest.model.Resultado;
import com.climber.everest.model.Usuario;
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

public class UsuarioRepository {

    private Retrofit retrofit;
    private Resultado resReq;
    private DatabaseReference mDatabase;

    public UsuarioRepository() {
        this.retrofit = RetrofitConfig.getRetrofit();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public Usuario geraUsuario(String idusuario, String nomeusuario, String emailusuario, String senhausuario, String statususuario, String perfilusuario) {
        Usuario user = new Usuario();
        user.idusuario = idusuario;
        user.nomeusuario = nomeusuario;
        user.emailusuario = emailusuario;
        user.senhausuario = senhausuario;
        user.statususuario = statususuario;
        user.perfilusuario = perfilusuario;

        return user;
    }

    public void loadToken(String email, String senha)
    {
        if(!email.toString().isEmpty() && !senha.toString().isEmpty()) {
            ApiService apiService = retrofit.create(ApiService.class);

            Usuario user = new Usuario();

            user.setEmailusuario(email.toString());
            user.setSenhausuario(senha.toString());

            apiService.logar("application/json", user)
                    .enqueue(new Callback<Resultado>() {
                        @Override
                        public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                            if (response.isSuccessful()) {
                                resReq = response.body();

                                Log.e(TAG, resReq.status);
                                if (resReq.status.equals("200")) {
                                    FirebaseUser usuarioFb = FirebaseAuth.getInstance().getCurrentUser();
                                    apiConfig.token = resReq.token;
                                    mDatabase.child("token").child(usuarioFb.getUid()).setValue(resReq.token);
                                    _usuarioLogado = resReq.usuario;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Resultado> call, Throwable t) {
                        }
                    });
        }

    }
}
