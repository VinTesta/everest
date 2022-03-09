package com.climber.everest.model;

import java.lang.reflect.Array;

public class Evento
{
    public Integer idevento = 0;
    public String tituloevento = "";
    public String descevento = "";
    public String dataevento = "";
    public String horaevento = "";

    public Integer getIdevento() {
        return idevento;
    }

    public void setIdevento(Integer idevento) {
        this.idevento = idevento;
    }

    public String getTituloevento() {
        return tituloevento;
    }

    public void setTituloevento(String tituloevento) {
        this.tituloevento = tituloevento;
    }

    public String getDescevento() {
        return descevento;
    }

    public void setDescevento(String descevento) {
        this.descevento = descevento;
    }

    public String getDataevento() {
        return dataevento;
    }

    public void setDataevento(String dataevento) {
        this.dataevento = dataevento;
    }

    public String getHoraevento() {
        return horaevento;
    }

    public void setHoraevento(String horaevento) {
        this.horaevento = horaevento;
    }
}
