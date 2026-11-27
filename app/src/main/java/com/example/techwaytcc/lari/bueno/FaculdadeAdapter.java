package com.example.techwaytcc.lari.bueno;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techwaytcc.R;
import com.example.techwaytcc.lari.bueno.model.Faculdade;

import java.util.ArrayList;
import java.util.List;

public class FaculdadeAdapter extends RecyclerView.Adapter<FaculdadeAdapter.ViewHolder> {

    public interface Acoes {
        boolean ehFavorito(Faculdade f);
        void aoFavoritar(Faculdade f);
        void aoVerCursos(Faculdade f);
        void aoAbrirSite(Faculdade f);
    }

    private final List<Faculdade> lista = new ArrayList<>();
    private final Acoes acoes;

    public FaculdadeAdapter(Acoes acoes) {
        this.acoes = acoes;
    }

    public void atualizar(List<Faculdade> novas) {
        lista.clear();
        lista.addAll(novas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_faculdade, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {

        Faculdade f = lista.get(position);

        h.tvSigla.setText(f.getMonograma());
        h.tvNome.setText(f.getNome());

        String local = f.getCidade();
        if (!f.getEstado().isEmpty()) {
            local = local.isEmpty() ? f.getEstado() : local + " · " + f.getEstado();
        }

        if (local.isEmpty()) {
            h.tvLocal.setVisibility(View.GONE);
        } else {
            h.tvLocal.setVisibility(View.VISIBLE);
            h.tvLocal.setText(local);
        }

        if (f.getTipo().isEmpty()) {
            h.cardTipo.setVisibility(View.GONE);
        } else {
            h.cardTipo.setVisibility(View.VISIBLE);
            h.tvTipo.setText(f.getTipo());
        }

        int n = f.getTotalCursos();
        h.tvCursos.setText(n == 1 ? "1 curso de TI" : n + " cursos de TI");

        h.btnFavorito.setImageResource(
                acoes.ehFavorito(f) ? R.drawable.ic_heart_filled : R.drawable.ic_heart
        );

        h.btnFavorito.setOnClickListener(v -> acoes.aoFavoritar(f));
        h.btnVerCursos.setOnClickListener(v -> acoes.aoVerCursos(f));
        h.btnSite.setOnClickListener(v -> acoes.aoAbrirSite(f));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View cardTipo, btnVerCursos, btnSite;
        TextView tvSigla, tvNome, tvLocal, tvTipo, tvCursos;
        ImageButton btnFavorito;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTipo = itemView.findViewById(R.id.cardTipo);
            btnVerCursos = itemView.findViewById(R.id.btnVerCursos);
            btnSite = itemView.findViewById(R.id.btnSite);
            tvSigla = itemView.findViewById(R.id.tvSigla);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvLocal = itemView.findViewById(R.id.tvLocal);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvCursos = itemView.findViewById(R.id.tvCursos);
            btnFavorito = itemView.findViewById(R.id.btnFavorito);
        }
    }
}