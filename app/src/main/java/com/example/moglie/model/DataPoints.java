package com.example.moglie.model;

import com.example.moglie.config.ConfiguracaoFirebase;
import com.example.moglie.helper.Base64Custom;
import com.example.moglie.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class DataPoints {
    float valorY;
    long valorX;
    String key;


    public DataPoints() {
    }

    public DataPoints(float valorY, long valorX) {
        this.valorY = valorY;
        this.valorX = valorX;
    }


    public void salvarPontos() {

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("grafico")
                .child(idUsuario)
                .push()
                .setValue(this);
    }

    public float getValorY() {
        return valorY;
    }

    public void setValorY(float valorY) {
        this.valorY = valorY;
    }

    public long getValorX() {
        return valorX;
    }

    public void setValorX(long valorX) {
        this.valorX = valorX;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}


