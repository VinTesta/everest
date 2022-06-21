package com.climber.everest.services;

import com.climber.everest.model.Evento;
import com.google.android.gms.maps.model.LatLng;

public interface InterfaceComunicacaoFragment {
    void setLocalizacao(LatLng localizacao);

    void setEvento(Evento evento);

    void setEnableEndereco(int i);
}
