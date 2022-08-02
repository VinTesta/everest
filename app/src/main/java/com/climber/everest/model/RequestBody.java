package com.climber.everest.model;

public class RequestBody {

    public RequestBody()
    {
        evento = new Evento();
        endereco = new Endereco('0', "0", "0");
    }
    public Evento evento;
    public Endereco endereco;
}
