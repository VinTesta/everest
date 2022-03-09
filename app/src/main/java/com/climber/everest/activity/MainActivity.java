package com.climber.everest.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.climber.everest.R;
import com.climber.everest.adapter.AdapterEvento;
import com.climber.everest.model.Evento;

import java.util.ArrayList;
import java.util.List;

public class MainActivity
                extends AppCompatActivity
                implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerCardEvento;
    private List<Evento> eventos = new ArrayList<>();
    private AdapterEvento adapterEvento;
    private SearchView txtSearch;
    private AppCompatImageView btnOpenSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerCardEvento = (RecyclerView) findViewById(R.id.recyclerCardEvento);
        eventos = buscaEventos();
        adapterEvento = new AdapterEvento(eventos, this);
        txtSearch = (SearchView) findViewById(R.id.txtSearch);
        btnOpenSearch = (AppCompatImageView) findViewById(R.id.btnOpenSearch);

        recyclerCardEvento.setHasFixedSize(true);
        recyclerCardEvento.setLayoutManager(new LinearLayoutManager(this));
        recyclerCardEvento.setAdapter(adapterEvento);

        adapterEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btnOpenSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOpenSearch.setVisibility(View.GONE);
                txtSearch.setVisibility(View.VISIBLE);
            }
        });

        txtSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                btnOpenSearch.setVisibility(View.VISIBLE);
                txtSearch.setVisibility(View.GONE);
                return false;
            }
        });

        txtSearch.setOnQueryTextListener(this);
    }

    private ArrayList buscaEventos()
    {
        ArrayList eventos = new ArrayList<Evento>();

        for (int i = 0; i < 7; i++)
        {
            Evento evento = new Evento();
            evento.setTituloevento("Teste evento " + i);
            evento.setDescevento("Esse teste "  + i + " vai dar certo, confia!");
            evento.setDataevento("10/10/201" + i);
            evento.setHoraevento("00:00:0"+i);

            eventos.add(evento);
        }

        return eventos;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        adapterEvento.filtrar(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapterEvento.filtrar(s);
        return false;
    }
}