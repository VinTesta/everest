package com.climber.everest.model;

public class Endereco {

    private String latitude;
    private String longitude;
    private Character status;

    public Endereco(Character status, String latitude, String longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
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

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }
}
