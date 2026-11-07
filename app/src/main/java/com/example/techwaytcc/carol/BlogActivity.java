package com.example.techwaytcc.carol;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techwaytcc.Navegacao;
import com.example.techwaytcc.R;
import com.google.android.material.chip.Chip;

public class BlogActivity extends AppCompatActivity {

    private ImageButton btnMenu;
    private Chip chipTeste;
    private Chip chipResultado;
    private Chip chipAreas;
    private Chip chipCursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.blog_main);

        // Inicializar componentes
        btnMenu = findViewById(R.id.btnMenu);
        chipTeste = findViewById(R.id.chipTeste);
        chipResultado = findViewById(R.id.chipResultado);
        chipAreas = findViewById(R.id.chipAreas);
        chipCursos = findViewById(R.id.chipCursos);

        // Botões de cima: o Blog não é uma das abas, por isso a "tela atual" é -1
        Navegacao.ligar(this, -1, chipTeste, Navegacao.TESTE);
        Navegacao.ligar(this, -1, chipResultado, Navegacao.RESULTADO);
        Navegacao.ligar(this, -1, chipAreas, Navegacao.AREAS);
        Navegacao.ligar(this, -1, chipCursos, Navegacao.CURSOS);

        // Menu
        btnMenu.setOnClickListener(v -> {
            // Conectar quando criar a tela de menu
        });
    }
}