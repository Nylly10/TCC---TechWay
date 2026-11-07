
package com.example.techwaytcc.carol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

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

        // Abrir Teste Vocacional
        chipTeste.setOnClickListener(v -> {
            Intent intent = new Intent(
                    BlogActivity.this,
                    TesteActivity.class
            );
            startActivity(intent);
        });

        // Resultado
        chipResultado.setOnClickListener(v -> {
            // Conectar quando criar ResultadoActivity
        });

        // Áreas em TI
        chipAreas.setOnClickListener(v -> {
            // Conectar quando criar AreasActivity
        });

        // Cursos
        chipCursos.setOnClickListener(v -> {
            // Conectar quando criar CursosActivity
        });

        // Menu
        btnMenu.setOnClickListener(v -> {
            // Conectar quando criar a tela de menu
        });
    }
}
