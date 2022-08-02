package com.climber.everest.model;

public class Endereco {

    private int ideventoendereco;
    private String latitude;
    private String longitude;
    private int status;

    public Endereco(Character status, String latitude, String longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public int getIdeventoendereco() {
        return ideventoendereco;
    }

    public void setIdeventoendereco(int ideventoendereco) {
        this.ideventoendereco = ideventoendereco;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
