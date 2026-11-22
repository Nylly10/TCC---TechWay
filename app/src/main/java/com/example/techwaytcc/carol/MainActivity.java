package com.example.techwaytcc.carol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.techwaytcc.R;
import com.example.techwaytcc.nylly.Perfil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Carrega somente a tela de suporte
        setContentView(R.layout.suporte_main);

        // Inicializar os botões do menu
        ImageButton navHome = findViewById(R.id.navHome);
        ImageButton navSupport = findViewById(R.id.navSupport);
        ImageButton navProfile = findViewById(R.id.navProfile);

        // Botão Início
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BlogActivity.class);
            startActivity(intent);
        });

        // Botão Suporte
        navSupport.setOnClickListener(v -> {
            // O usuário já está na tela de suporte
        });

        // Botão Perfil
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Perfil.class);
            startActivity(intent);
        });

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}