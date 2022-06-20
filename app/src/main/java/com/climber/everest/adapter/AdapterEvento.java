package com.climber.everest.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.climber.everest.R;
import com.climber.everest.model.Evento;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AdapterEvento
        extends RecyclerView.Adapter<AdapterEvento.MyViewHolder>
        implements View.OnClickListener {

    private List<Evento> eventos = new ArrayList<>();
    private List<Evento> listaOriginalEventos = new ArrayList<>();
    private Context con;
    private View.OnClickListener cardClick;

    public AdapterEvento(List<Evento> eventos, Context con) {
        this.eventos = eventos;
        this.con = con;
        this.listaOriginalEventos.addAll(eventos);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_evento, parent, false);

        view.setOnClickListener(this);

        return new AdapterEvento.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Evento evento = eventos.get( position );
        holder.titulo.setText( evento.getTituloevento() );
        holder.descricao.setText( evento.getDescevento() );
    }

    public void filtrar(String txtSearch)
    {
        //  int tam = txtSearch.length();
        eventos.clear();
        eventos.addAll(listaOriginalEventos);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            List<Evento> listaFiltrada = eventos.stream()
                    .filter(evento -> evento.getTituloevento().toUpperCase()
                            .contains(txtSearch.toUpperCase())).collect(Collectors.toList());

            eventos.clear();
            eventos.addAll(listaFiltrada);
        }
        else
        {
            for (Evento e: listaOriginalEventos)
            {
                if (e.getTituloevento().toUpperCase().contains(txtSearch.toUpperCase()))
                {
                    eventos.add(e);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public void setOnClickListener(View.OnClickListener cardClick)
    {
        this.cardClick = cardClick;
    }

    @Override
    public void onClick(View view) {
        if(cardClick != null)
        {
            cardClick.onClick(view);

            ConstraintLayout constraintDescCardAdapter = (ConstraintLayout) view.findViewById(R.id.constraintDescCardAdapter);
            CardView cardAdapter = (CardView) view.findViewById(R.id.cardAdapter);
            ImageView expandingIconCardAdapter = (ImageView) view.findViewById(R.id.expandingIconCardAdapter);

            TransitionManager.beginDelayedTransition(cardAdapter, new AutoTransition());
            constraintDescCardAdapter.setVisibility((constraintDescCardAdapter.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
            expandingIconCardAdapter.setImageResource((constraintDescCardAdapter.getVisibility() != View.GONE) ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView titulo;
        TextView descricao;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            titulo = itemView.findViewById(R.id.titleCardAdpter);
            descricao = itemView.findViewById(R.id.descCardAdapter);
        }
    }
}
