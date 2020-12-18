package com.example.moglie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.moglie.R;
import com.example.moglie.activity.CadastroActivity;
import com.example.moglie.activity.LoginActivity;
import com.example.moglie.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onStart(){
        super.onStart();
        verificaUsuarioLogado();
    }

    public void btEntrar (View view){
       startActivity(new Intent(this, LoginActivity.class));
    }


    public void btCadastrar (View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void verificaUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if ( autenticacao.getCurrentUser() != null){
        abrirMenu();
        }
    }
    public void abrirMenu(){
        startActivity(new Intent(this, MenuActivity.class));
    }



}