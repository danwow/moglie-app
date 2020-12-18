package com.example.moglie.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.RenderNode;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.moglie.R;
import com.example.moglie.adapter.AdapterAnotacao;
import com.example.moglie.config.ConfiguracaoFirebase;
import com.example.moglie.helper.Base64Custom;
import com.example.moglie.helper.DateCustom;
import com.example.moglie.model.Anotacao;
import com.example.moglie.model.DataPoints;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.List;

import static com.example.moglie.R.id.editData;

public class GraficoActivity extends AppCompatActivity {

    public Button botaoDiario, botaoGlicemia, botaoMenu;
    public LineChart grafico;
    public LineDataSet lineDataSet = new LineDataSet(null,null);
    public ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    public LineData lineData;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference graficoRef;
    private ValueEventListener valueEventListenerGrafico;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        botaoDiario = findViewById(R.id.button6);
        botaoMenu = findViewById(R.id.button4);
        botaoGlicemia = findViewById(R.id.button5);

        botaoDiario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDiario();
            }
        });
        botaoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMenu();
            }
        });
        botaoGlicemia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGlicemia();
            }
        });

        grafico = findViewById(R.id.grafico);
        grafico.setDrawGridBackground(true);
        grafico.setDrawBorders(true);
        grafico.setBorderColor(Color.BLACK);
        grafico.setVisibleXRange(4,4);


        lineDataSet.setLineWidth(4);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setCircleColor(Color.RED);
        lineDataSet.setCircleColorHole(Color.WHITE);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setValueTextSize(15);

        lineDataSet.setValueFormatter(new MyValueFormatter());



        XAxis xAxis = grafico.getXAxis();
        YAxis yAxisLeft = grafico.getAxisLeft();
        YAxis yAxisRight = grafico.getAxisRight();

        xAxis.setValueFormatter(new MyAxisValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
       // xAxis.setAxisMaximum(10f);

        grafico.setNoDataText("Carregando dados...");



        Description description = new Description();
        description.setText("Concentração de glicose x Data da aferição");
        grafico.setDescription(description);

    }



    public void recuperarDadosGrafico(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        graficoRef = firebaseRef.child("grafico").child( idUsuario );

        valueEventListenerGrafico = graficoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Entry> dataVals = new ArrayList<>();

                if(snapshot.hasChildren()){
                    for (DataSnapshot myDataSnapshot : snapshot.getChildren()){
                        DataPoints dataPoints = myDataSnapshot.getValue(DataPoints.class);
                        dataVals.add(new Entry(dataPoints.getValorX(), dataPoints.getValorY()));
                    }

                    showChart (dataVals);
                }else {
                    grafico.clear();
                    grafico.invalidate();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private class MyValueFormatter implements IValueFormatter{

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return value + " mg/dL";
        }
    }

    private class MyAxisValueFormatter implements  IAxisValueFormatter{

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            axis.setLabelCount(4,true);
            String data = DateCustom.dataGrafico(value);
            String hora = DateCustom.horaGrafico(value);
            String valueFormatted = data + " às " + hora;
            return valueFormatted;
        }
    }



    private void showChart(ArrayList<Entry> dataVals) {
        lineDataSet.setValues(dataVals);
        lineDataSet.setLabel("Concentração de Glicose (mg/dL)");
        iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);
        lineData = new LineData(iLineDataSets);
        grafico.clear();
        grafico.setData(lineData);
        grafico.invalidate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarDadosGrafico();
    }

    @Override
    protected void onStop() {
        super.onStop();
        graficoRef.removeEventListener(valueEventListenerGrafico);
    }

    public void abrirDiario(){
        startActivity(new Intent(this, NotasActivity.class));
    }
    public void abrirMenu(){
        startActivity(new Intent(this, MenuActivity.class));
    }
    public void abrirGlicemia(){
        startActivity(new Intent(this, GlicemiaActivity.class));
    }

}