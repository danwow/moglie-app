package com.example.moglie.activity;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.moglie.adapter.AdapterAnotacao;
import com.example.moglie.config.ConfiguracaoFirebase;
import com.example.moglie.helper.Base64Custom;
import com.example.moglie.model.Anotacao;
import com.example.moglie.model.DataPoints;
import com.example.moglie.model.Usuario;
import com.github.mikephil.charting.data.Entry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moglie.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.List;

public class NotasActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView textoSaudacao;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private DatabaseReference graficoRef;
    private ValueEventListener valueEventListenerGrafico;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerAnotacoes;

    private RecyclerView recyclerView;
    private AdapterAnotacao adapterAnotacao;
    private List<Anotacao> anotacoes = new ArrayList<>();
    private Anotacao anotacao;
    private DataPoints dataPoint;
    private DatabaseReference anotacaoRef;
    private String mesAnoSelecionado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        textoSaudacao = findViewById(R.id.textSaudacao);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerAnotacoes);
        configuraCalendarView();
        swipe();

        // configurar adapter
        adapterAnotacao = new AdapterAnotacao(anotacoes, this);
        //configurar recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( adapterAnotacao );
    }

    public void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirAnotacao( viewHolder);

            }
        };

        new ItemTouchHelper ( itemTouch ).attachToRecyclerView(recyclerView);

    }

    public void excluirAnotacao(final RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Anotação do Diário");
        alertDialog.setMessage("Tem certeza que deseja excluir essa anotação?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                anotacao = anotacoes.get(position);


                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64( emailUsuario );
                anotacaoRef = firebaseRef.child("anotacao")
                        .child ( idUsuario )
                        .child (mesAnoSelecionado);
                anotacaoRef.child(anotacao.getKey()).removeValue();

                /*
                graficoRef = firebaseRef.child("grafico")
                        .child (idUsuario);
                graficoRef.child(anotacao.getKey()).removeValue();
                */

                adapterAnotacao.notifyItemRemoved(position);

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(NotasActivity.this,"Cancelado",Toast.LENGTH_SHORT).show();
            adapterAnotacao.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();

    }

    public void recuperarAnotacoes(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        anotacaoRef = firebaseRef.child("anotacao")
                                 .child ( idUsuario )
                                 .child (mesAnoSelecionado);

        valueEventListenerAnotacoes = anotacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                anotacoes.clear();
                for(DataSnapshot dados: snapshot.getChildren()){
                    Anotacao anotacao = dados.getValue(Anotacao.class);
                    anotacao.setKey( dados.getKey());
                    anotacoes.add(anotacao);

                }
                adapterAnotacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    public void recuperarResumo(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        usuarioRef = firebaseRef.child("usuarios").child( idUsuario );

        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Usuario usuario = snapshot.getValue( Usuario.class);
                textoSaudacao.setText("Bem-vindo de volta, " + usuario.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    public void adicionarGlicemia(View view){
        startActivity(new Intent(this, GlicemiaActivity.class));
    }
    public void adicionarMedicamento(View view){
        startActivity(new Intent(this, MedicamentoActivity.class));
    }
    public void adicionarRefeicao(View view){
        startActivity(new Intent(this, RefeicaoActivity.class));
    }

    public void configuraCalendarView(){
        CharSequence meses[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        calendarView.setTitleMonths(meses);

        CalendarDay dataAtual = calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth() + 1));
        mesAnoSelecionado = String.valueOf( mesSelecionado + "" + dataAtual.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", (date.getMonth() + 1));
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear());

                anotacaoRef.removeEventListener(valueEventListenerAnotacoes);
                recuperarAnotacoes();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarAnotacoes();
    }
    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener( valueEventListenerUsuario );
        anotacaoRef.removeEventListener( valueEventListenerAnotacoes);
    }
}