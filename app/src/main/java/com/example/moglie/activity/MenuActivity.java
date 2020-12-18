package com.example.moglie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.moglie.R;
import com.example.moglie.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.ref.ReferenceQueue;

public class MenuActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private Button botaoDeslogar, botaoPerfil, botaoAferir, botaoGraficosRelatorios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        botaoPerfil = findViewById(R.id.buttonPerfil);
        botaoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirPerfil();
            }
        });

        botaoAferir = findViewById(R.id.buttonAferir);
        botaoAferir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGlicemia();
            }
        });

        botaoGraficosRelatorios = findViewById(R.id.buttonGraficosRelatorios);
        botaoGraficosRelatorios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGraficosRelatorios();
            }
        });


        botaoDeslogar = findViewById(R.id.buttonDeslogar);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

    }

    public void abrirPerfil(){
        startActivity(new Intent(this, NotasActivity.class));
    }

    public void abrirGlicemia(){
        startActivity(new Intent(this, GlicemiaActivity.class));
    }

    public void abrirGraficosRelatorios(){
        startActivity(new Intent(this, GraficoActivity.class));
    }

    public void deslogarApp(View view){
        autenticacao.signOut();
        startActivity(new Intent(this, MainActivity.class));
    }

}