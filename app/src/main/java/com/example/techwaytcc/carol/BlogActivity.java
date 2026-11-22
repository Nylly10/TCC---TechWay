package com.example.techwaytcc.carol;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techwaytcc.Navegacao;
import com.example.techwaytcc.R;
import com.example.techwaytcc.nylly.Perfil;
import com.google.android.material.chip.Chip;

public class BlogActivity extends AppCompatActivity {

    private Chip chipTeste;
    private Chip chipResultado;
    private Chip chipAreas;
    private Chip chipCursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_main);

        // Inicializar os botões
        ImageButton navHome = findViewById(R.id.navHome);
        ImageButton navSupport = findViewById(R.id.navSupport);
        ImageButton navProfile = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            // Já está na tela inicial
        });

        // Suporte
        navSupport.setOnClickListener(v -> {
            Intent intent = new Intent(BlogActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Perfil
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(BlogActivity.this, Perfil.class);
            startActivity(intent);
        });



        // Inicializar Chips
        chipTeste = findViewById(R.id.chipTeste);
        chipResultado = findViewById(R.id.chipResultado);
        chipAreas = findViewById(R.id.chipAreas);
        chipCursos = findViewById(R.id.chipCursos);

        // Navegação dos Chips
        Navegacao.ligar(this, -1, chipTeste, Navegacao.TESTE);
        Navegacao.ligar(this, -1, chipResultado, Navegacao.RESULTADO);
        Navegacao.ligar(this, -1, chipAreas, Navegacao.AREAS);
        Navegacao.ligar(this, -1, chipCursos, Navegacao.CURSOS);
    }
}