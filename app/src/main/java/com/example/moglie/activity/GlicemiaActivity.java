package com.example.moglie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moglie.R;
import com.example.moglie.helper.DateCustom;
import com.example.moglie.model.Anotacao;
import com.example.moglie.model.DataPoints;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GlicemiaActivity extends AppCompatActivity {

    private Button botaoSinc, botaoAferir;
    private TextInputEditText campoData, campoDescricao;
    private EditText campoValor;
    private Anotacao anotacao;
    private DataPoints dataPoints;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glicemia);

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoDescricao = findViewById(R.id.editDescricao);
        botaoSinc = findViewById(R.id.button7);
        botaoAferir = findViewById(R.id.button8);

        botaoSinc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double glicoseDouble = (new Random().nextDouble()*150)+50;
                double glicoseArrendondado = (double) Math.round(glicoseDouble*100.0)/100.0;
                String glicose = String.valueOf(glicoseArrendondado);
                campoValor.setText(glicose);

            }
        });

        botaoAferir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double glicoseDouble = (new Random().nextDouble()*150)+50;
                double glicoseArrendondado = (double) Math.round(glicoseDouble*100.0)/100.0;
                String glicose = String.valueOf(glicoseArrendondado);
                campoValor.setText(glicose);
            }
        });
        campoData.setText(DateCustom.dataAtual());
    }

    public void salvarGlicemia (View view){

        if (validarCamposGlicemia()){
            anotacao = new Anotacao();
            String data = campoData.getText().toString();
            String valor = campoValor.getText().toString();
            anotacao.setValor(valor);
            anotacao.setDescricao(campoDescricao.getText().toString());
            anotacao.setData( data );
            anotacao.setTipo("g");

            anotacao.salvar(data);


            dataPoints = new DataPoints();
            long eixoXdata = new Date().getTime();
            float eixoYglicemia = Float.parseFloat(campoValor.getText().toString());
            dataPoints.setValorY(eixoYglicemia);
            dataPoints.setValorX(eixoXdata);

            dataPoints.salvarPontos();

            abrirGrafico();
        }


    }

    public Boolean validarCamposGlicemia(){

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if (!textoValor.isEmpty()){
            if (!textoData.isEmpty()) {
                if(!textoDescricao.isEmpty()){
                    return true;
                } else {
                    Toast.makeText(GlicemiaActivity.this, "PREENCHA A DESCRIÇÃO!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(GlicemiaActivity.this, "PREENCHA A DATA!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(GlicemiaActivity.this, "PREENCHA O VALOR!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void abrirGrafico(){
        startActivity(new Intent(this, GraficoActivity.class));
    }
}