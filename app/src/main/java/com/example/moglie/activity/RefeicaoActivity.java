package com.example.moglie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moglie.R;
import com.example.moglie.helper.DateCustom;
import com.example.moglie.model.Anotacao;
import com.google.android.material.textfield.TextInputEditText;

public class RefeicaoActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoMarcador, campoDescricao;
    private EditText campoValor;
    private Anotacao anotacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refeicao);

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoMarcador = findViewById(R.id.editMarcador);
        campoDescricao = findViewById(R.id.editDescricao);

        campoData.setText(DateCustom.dataAtual());

    }

    public void salvarRefeicao (View view){

        if (validarCamposRefeicao()){
            anotacao = new Anotacao();
            String data = campoData.getText().toString();
            String valor = campoValor.getText().toString();
            anotacao.setValor(valor);
            anotacao.setMarcador(campoMarcador.getText().toString());
            anotacao.setDescricao(campoDescricao.getText().toString());
            anotacao.setData( data );
            anotacao.setTipo("r");

            anotacao.salvar(data);
            finish();
        }


    }

    public Boolean validarCamposRefeicao(){

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoMarcador = campoMarcador.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if (!textoValor.isEmpty()){
            if (!textoData.isEmpty()) {
                if(!textoMarcador.isEmpty()){
                    if(!textoDescricao.isEmpty()){
                        return true;
                    } else {
                        Toast.makeText(RefeicaoActivity.this, "PREENCHA A DESCRIÇÃO!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else {
                    Toast.makeText(RefeicaoActivity.this, "PREENCHA O MARCADOR!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(RefeicaoActivity.this, "PREENCHA A DATA!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(RefeicaoActivity.this, "PREENCHA O VALOR!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}