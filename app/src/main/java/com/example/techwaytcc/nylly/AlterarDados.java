package com.example.techwaytcc.nylly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techwaytcc.R;

public class AlterarDados extends AppCompatActivity {

    // Campos de texto
    EditText edtNome;
    EditText edtDataNascimento;
    EditText edtEmail;
    EditText edtTelefone;
    EditText edtLocalizacao;

    // Spinner
    Spinner spinnerGenero;

    // Botões
    Button btnSalvarDados;
    Button btnAlterarFoto;

    // Foto de perfil
    ImageView fotoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.alterar_dados_activity);

        // ==========================================
        // CONECTANDO OS ELEMENTOS DO XML
        // ==========================================

        edtNome = findViewById(R.id.edtNome);
        edtDataNascimento = findViewById(R.id.edtDataNascimento);
        edtEmail = findViewById(R.id.edtEmail);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtLocalizacao = findViewById(R.id.edtLocalizacao);

        spinnerGenero = findViewById(R.id.spinnerGenero);

        btnSalvarDados = findViewById(R.id.btnSalvarDados);
        btnAlterarFoto = findViewById(R.id.btn_alterarFoto);

        fotoPerfil = findViewById(R.id.fotoPerfil);


        // ==========================================
        // CONFIGURANDO O SPINNER
        // ==========================================

        String[] opcoesGenero = {
                "Feminino",
                "Masculino"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opcoesGenero
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerGenero.setAdapter(adapter);


        // ==========================================
        // CARREGAR DADOS JÁ SALVOS
        // ==========================================

        carregarDados();


        // ==========================================
        // BOTÃO SALVAR
        // ==========================================

        btnSalvarDados.setOnClickListener(v -> salvarDados());


        // ==========================================
        // BOTÃO ALTERAR FOTO
        // ==========================================

        btnAlterarFoto.setOnClickListener(v -> {

            Toast.makeText(
                    AlterarDados.this,
                    "Função de alterar foto será adicionada depois.",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // ==========================================
        // AJUSTE DA TELA
        // ==========================================

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.alterarDados),
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


    // =================================================
    // FUNÇÃO PARA SALVAR OS DADOS
    // =================================================

    private void salvarDados() {

        String nome = edtNome.getText().toString().trim();

        String dataNascimento =
                edtDataNascimento.getText().toString().trim();

        String email =
                edtEmail.getText().toString().trim();

        String telefone =
                edtTelefone.getText().toString().trim();

        String localizacao =
                edtLocalizacao.getText().toString().trim();

        String genero =
                spinnerGenero.getSelectedItem().toString();


        // ==========================================
        // VALIDAÇÃO DO NOME
        // ==========================================

        if (nome.isEmpty()) {

            edtNome.setError("Digite seu nome");
            edtNome.requestFocus();

            return;
        }


        // ==========================================
        // VALIDAÇÃO DO E-MAIL
        // ==========================================

        if (email.isEmpty()) {

            edtEmail.setError("Digite seu e-mail");
            edtEmail.requestFocus();

            return;
        }


        // ==========================================
        // SALVAR NO SHAREDPREFERENCES
        // ==========================================

        SharedPreferences prefs =
                getSharedPreferences("Usuario", MODE_PRIVATE);

        SharedPreferences.Editor editor =
                prefs.edit();


        editor.putString("nome", nome);

        editor.putString(
                "dataNascimento",
                dataNascimento
        );

        editor.putString(
                "email",
                email
        );

        editor.putString(
                "telefone",
                telefone
        );

        editor.putString(
                "localizacao",
                localizacao
        );

        editor.putString(
                "genero",
                genero
        );

        editor.apply();


        // ==========================================
        // MENSAGEM DE SUCESSO
        // ==========================================

        Toast.makeText(
                AlterarDados.this,
                "Dados alterados com sucesso!",
                Toast.LENGTH_SHORT
        ).show();


        // ==========================================
        // VOLTAR PARA O PERFIL
        // ==========================================

        Intent intent = new Intent(
                AlterarDados.this,
                Perfil.class
        );

        startActivity(intent);

        finish();
    }


    // =================================================
    // FUNÇÃO PARA CARREGAR OS DADOS SALVOS
    // =================================================

    private void carregarDados() {

        SharedPreferences prefs =
                getSharedPreferences("Usuario", MODE_PRIVATE);


        String nome =
                prefs.getString("nome", "");

        String dataNascimento =
                prefs.getString("dataNascimento", "");

        String email =
                prefs.getString("email", "");

        String telefone =
                prefs.getString("telefone", "");

        String localizacao =
                prefs.getString("localizacao", "");

        String genero =
                prefs.getString("genero", "Feminino");


        // ==========================================
        // COLOCAR OS DADOS NOS CAMPOS
        // ==========================================

        edtNome.setText(nome);

        edtDataNascimento.setText(
                dataNascimento
        );

        edtEmail.setText(email);

        edtTelefone.setText(
                telefone
        );

        edtLocalizacao.setText(
                localizacao
        );


        // ==========================================
        // SELECIONAR O GÊNERO NO SPINNER
        // ==========================================

        ArrayAdapter<String> adapter =
                (ArrayAdapter<String>) spinnerGenero.getAdapter();

        int posicao =
                adapter.getPosition(genero);

        if (posicao >= 0) {

            spinnerGenero.setSelection(posicao);
        }
    }
}
