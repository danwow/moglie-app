package com.example.moglie.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moglie.R;
import com.example.moglie.model.Anotacao;

import java.util.List;

/**
 * Created by Danillo
 */

public class AdapterAnotacao extends RecyclerView.Adapter<AdapterAnotacao.MyViewHolder> {

    List<Anotacao> anotacoes;
    Context context;

    public AdapterAnotacao(List<Anotacao> anotacoes, Context context) {
        this.anotacoes = anotacoes;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_anotacao, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Anotacao anotacao = anotacoes.get(position);

        holder.titulo.setText(anotacao.getDescricao());
        holder.valor.setText(anotacao.getValor());
        holder.marcador.setText(anotacao.getMarcador());
        holder.data.setText(anotacao.getData());

        if (anotacao.getTipo().equals("g")) {
            holder.valor.setTextColor(context.getResources().getColor(R.color.colorAccentGlicemia));
            holder.marcador.setTextColor(context.getResources().getColor(R.color.colorAccentGlicemia));
            holder.valor.setText(anotacao.getValor() + " mg/dL");
            holder.marcador.setText("Concentração de Glicose");
        }
        if (anotacao.getTipo().equals("m")) {
            holder.valor.setTextColor(context.getResources().getColor(R.color.colorAccentMedicamento));
            holder.marcador.setTextColor(context.getResources().getColor(R.color.colorAccentMedicamento));
            holder.valor.setText(anotacao.getValor() + " doses");
        }
        if (anotacao.getTipo().equals("r")) {
            holder.valor.setTextColor(context.getResources().getColor(R.color.colorAccentRefeicao));
            holder.marcador.setTextColor(context.getResources().getColor(R.color.colorAccentRefeicao));
            holder.valor.setText(anotacao.getValor() + " g (ml)");
        }
    }


    @Override
    public int getItemCount() {
        return anotacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, valor, marcador, data;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textAdapterTitulo);
            valor = itemView.findViewById(R.id.textAdapterValor);
            marcador = itemView.findViewById(R.id.textAdapterMarcador);
            data = itemView.findViewById(R.id.textAdapterData);
        }

    }

}
