package com.example.techwaytcc.lari.bueno;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techwaytcc.R;
import com.example.techwaytcc.lari.bueno.model.Curso;

import java.util.ArrayList;
import java.util.List;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.ViewHolder> {

    public interface Acoes {
        boolean ehFavorito(Curso curso);
        void aoFavoritar(Curso curso);
        void aoVerDetalhes(Curso curso);
    }

    private final List<Curso> lista = new ArrayList<>();
    private final Acoes acoes;

    public CursoAdapter(Acoes acoes) {
        this.acoes = acoes;
    }

    public void atualizar(List<Curso> novos) {
        lista.clear();
        lista.addAll(novos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_curso, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {

        Curso c = lista.get(position);

        // Etiqueta da área
        if (c.getArea().isEmpty()) {
            h.cardArea.setVisibility(View.GONE);
        } else {
            h.cardArea.setVisibility(View.VISIBLE);
            h.tvArea.setText(c.getArea());
        }

        h.tvNome.setText(c.getNome());

        mostrar(h.tvEscola, juntar(c.getInstituicao(), c.getCidade()));
        mostrar(h.tvMeta, juntar(c.getDuracao(), c.getModalidade()));

        h.btnFavorito.setImageResource(
                acoes.ehFavorito(c) ? R.drawable.ic_heart_filled : R.drawable.ic_heart
        );

        h.btnFavorito.setOnClickListener(v -> acoes.aoFavoritar(c));
        h.btnDetalhes.setOnClickListener(v -> acoes.aoVerDetalhes(c));
        h.itemView.setOnClickListener(v -> acoes.aoVerDetalhes(c));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    private String juntar(String a, String b) {
        if (a.isEmpty()) return b;
        if (b.isEmpty()) return a;
        return a + " · " + b;
    }

    private void mostrar(TextView tv, String texto) {
        if (texto.isEmpty()) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(texto);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View cardArea;
        TextView tvArea, tvNome, tvEscola, tvMeta;
        ImageButton btnFavorito;
        View btnDetalhes;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardArea = itemView.findViewById(R.id.cardArea);
            tvArea = itemView.findViewById(R.id.tvArea);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvEscola = itemView.findViewById(R.id.tvEscola);
            tvMeta = itemView.findViewById(R.id.tvMeta);
            btnFavorito = itemView.findViewById(R.id.btnFavorito);
            btnDetalhes = itemView.findViewById(R.id.btnDetalhes);
        }
    }
}