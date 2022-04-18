package com.climber.everest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.climber.everest.R;

public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
    }

    public void abrirLogin(View view)
    {
        Intent i =  new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(i);
    }
}