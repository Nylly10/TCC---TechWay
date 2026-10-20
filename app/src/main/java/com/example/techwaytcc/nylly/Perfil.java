package com.example.techwaytcc.nylly;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techwaytcc.R;

public class Perfil extends AppCompatActivity {

    ImageView fotoPerfil;
    Spinner spinnerGenero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.perfil_activity);

        // Referências dos componentes
        fotoPerfil = findViewById(R.id.fotoPerfil);
        spinnerGenero = findViewById(R.id.spinnerGenero);

        TextView txtNome = findViewById(R.id.txtNome);
        TextView txtEmail = findViewById(R.id.txtEmail);
        TextView tvNome = findViewById(R.id.tvNome);
        TextView tvEmail = findViewById(R.id.tvEmail);

        txtNome.setText(R.string.nome_perfil);
        txtEmail.setText(R.string.email_perfil);
        tvNome.setText(R.string.nome_perfil);
        tvEmail.setText(R.string.email_perfil);

        // =====================================================
        // SPINNER DE GÊNERO
        // =====================================================
        String[] opcoesGenero = getResources()
                .getStringArray(R.array.opcoes_genero);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opcoesGenero
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerGenero.setAdapter(adapter);

        spinnerGenero.setAdapter(adapter);

        // =====================================================
        // CONFIGURAÇÃO DA ÁREA DA TELA
        // =====================================================
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.perfil),
                (v, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );
    }
}