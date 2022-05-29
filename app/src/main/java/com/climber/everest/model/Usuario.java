package com.climber.everest.model;

import static android.content.ContentValues.TAG;

import static com.climber.everest.activity.LoginActivity.apiConfig;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import static com.climber.everest.activity.LoginActivity._usuarioLogado;

import com.climber.everest.activity.LoginActivity;
import com.climber.everest.activity.MainActivity;
import com.climber.everest.config.RetrofitConfig;
import com.climber.everest.repository.TokenRepository;
import com.climber.everest.repository.UsuarioRepository;
import com.climber.everest.services.ApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Usuario {
    public String idusuario;
    public String nomeusuario;
    public String emailusuario;
    public String senhausuario;
    public String statususuario;
    public String perfilusuario;

    public String getStatususuario() {
        return statususuario;
    }

    public void setStatususuario(String statususuario) {
        this.statususuario = statususuario;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getNomeusuario() {
        return nomeusuario;
    }

    public void setNomeusuario(String nomeusuario) {
        this.nomeusuario = nomeusuario;
    }

    public String getEmailusuario() {
        return emailusuario;
    }

    public void setEmailusuario(String emailusuario) {
        this.emailusuario = emailusuario;
    }

    public String getSenhausuario() {
        return senhausuario;
    }

    public void setSenhausuario(String senhausuario) {
        this.senhausuario = senhausuario;
    }

    public String getPerfilusuario() {
        return perfilusuario;
    }

    public void setPerfilusuario(String perfilusuario) {
        this.perfilusuario = perfilusuario;
    }


}
