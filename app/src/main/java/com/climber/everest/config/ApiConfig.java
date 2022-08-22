package com.climber.everest.config;

import static com.climber.everest.activity.LoginActivity.apiConfig;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.climber.everest.activity.LoginActivity;
import com.climber.everest.activity.MainActivity;
import com.climber.everest.model.Resultado;
import com.climber.everest.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiConfig {
    public static String URL_BASE = "https://everest-api-v2.herokuapp.com/";
    public static String token = "";
    public static Usuario usuarioApi = null;
    public DatabaseReference mDatabase;
}
