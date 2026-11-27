package com.example.techwaytcc.carol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techwaytcc.Navegacao;
import com.example.techwaytcc.R;
import com.example.techwaytcc.Sessao;
import com.example.techwaytcc.nylly.Perfil;

public class BlogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_main);

        // ---------- Saudação com o nome de quem entrou ----------
        TextView txtSaudacao = findViewById(R.id.txtSaudacao);
        String nome = Sessao.getNome(this).trim();

        if (nome.isEmpty()) {
            txtSaudacao.setText("Olá!");
        } else {
            txtSaudacao.setText("Olá, " + nome.split(" ")[0] + "!");
        }

        // ---------- Menu de baixo ----------
        ImageButton navHome = findViewById(R.id.navHome);
        ImageButton navSupport = findViewById(R.id.navSupport);
        ImageButton navProfile = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            // Já está na tela inicial
        });

        navSupport.setOnClickListener(v ->
                startActivity(new Intent(BlogActivity.this, SuporteActivity.class))
        );

        navProfile.setOnClickListener(v ->
                startActivity(new Intent(BlogActivity.this, Perfil.class))
        );

        // ---------- Menu de cima (chips) ----------
        Navegacao.ligar(this, -1, findViewById(R.id.chipTeste), Navegacao.TESTE);
        Navegacao.ligar(this, -1, findViewById(R.id.chipResultado), Navegacao.RESULTADO);
        Navegacao.ligar(this, -1, findViewById(R.id.chipAreas), Navegacao.AREAS);
        Navegacao.ligar(this, -1, findViewById(R.id.chipCursos), Navegacao.CURSOS);

        // ---------- Botão do cartão de destaque ----------
        Navegacao.ligar(this, -1, findViewById(R.id.btnFazerTeste), Navegacao.TESTE);

        // ---------- Áreas em alta: todas abrem a tela Áreas em TI ----------
        LinearLayout linhaAreas = findViewById(R.id.linhaAreas);

        for (int i = 0; i < linhaAreas.getChildCount(); i++) {
            View area = linhaAreas.getChildAt(i);
            Navegacao.ligar(this, -1, area, Navegacao.AREAS);
        }
    }
}