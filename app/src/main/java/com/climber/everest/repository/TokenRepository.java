package com.climber.everest.repository;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class TokenRepository {

    public String decoded(String JWTEncoded) {
        try {
            String[] split = JWTEncoded.split("\\.");
            return getJson(split[1]);
        } catch (UnsupportedEncodingException e) {
            //Error
            return e.toString();
        }
    }

    private String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
