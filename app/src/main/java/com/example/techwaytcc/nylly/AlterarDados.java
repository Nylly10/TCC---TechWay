
package com.example.techwaytcc.nylly;

import android.os.Bundle;
import android.util.Patterns;
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

import com.example.techwaytcc.Navegacao;
import com.example.techwaytcc.R;
import com.example.techwaytcc.Sessao;
import com.example.techwaytcc.lari.bueno.api.ApiService;
import com.example.techwaytcc.lari.bueno.api.RetrofitCliente;
import com.example.techwaytcc.lari.bueno.model.PerfilDados;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlterarDados extends AppCompatActivity {

    EditText edtNome;
    EditText edtDataNascimento;
    EditText edtEmail;
    EditText edtTelefone;
    EditText edtLocalizacao;

    Spinner spinnerGenero;

    Button btnSalvarDados;
    Button btnAlterarFoto;

    ImageView fotoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.alterar_dados_activity);
        Navegacao.configurarMenuInferior(this, Navegacao.PERFIL);

        edtNome = findViewById(R.id.edtNome);
        edtDataNascimento = findViewById(R.id.edtDataNascimento);
        edtEmail = findViewById(R.id.edtEmail);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtLocalizacao = findViewById(R.id.edtLocalizacao);

        spinnerGenero = findViewById(R.id.spinnerGenero);

        btnSalvarDados = findViewById(R.id.btnSalvarDados);
        btnAlterarFoto = findViewById(R.id.btn_alterarFoto);

        fotoPerfil = findViewById(R.id.fotoPerfil);

        // Spinner de gênero (mesmas opções da tela de perfil)
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

        carregarDados();

        btnSalvarDados.setOnClickListener(v -> salvarDados());

        btnAlterarFoto.setOnClickListener(v ->
                Toast.makeText(
                        AlterarDados.this,
                        "Função de alterar foto será adicionada depois.",
                        Toast.LENGTH_SHORT
                ).show()
        );

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

    // Preenche os campos com os dados que estão no banco
    private void carregarDados() {

        edtNome.setText(Sessao.getNome(this));
        edtEmail.setText(Sessao.getEmail(this));

        int id = Sessao.getId(this);

        if (id <= 0) {
            return;
        }

        ApiService api = RetrofitCliente
                .getClient()
                .create(ApiService.class);

        api.buscarPerfil(new PerfilDados(id)).enqueue(new Callback<PerfilDados>() {

            @Override
            public void onResponse(
                    Call<PerfilDados> call,
                    Response<PerfilDados> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isSucesso()) {

                    PerfilDados p = response.body();

                    edtNome.setText(p.getNome());
                    edtEmail.setText(p.getEmail());
                    edtDataNascimento.setText(p.getDataNascimento());
                    edtTelefone.setText(p.getTelefone());
                    edtLocalizacao.setText(p.getLocalizacao());

                    selecionarGenero(p.getGenero());
                }
            }

            @Override
            public void onFailure(Call<PerfilDados> call, Throwable t) {
                Toast.makeText(
                        AlterarDados.this,
                        "Não foi possível carregar seus dados",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void selecionarGenero(String genero) {

        ArrayAdapter<?> adapter = (ArrayAdapter<?>) spinnerGenero.getAdapter();

        for (int i = 0; i < adapter.getCount(); i++) {
            if (String.valueOf(adapter.getItem(i)).equals(genero)) {
                spinnerGenero.setSelection(i);
                return;
            }
        }

        spinnerGenero.setSelection(0);
    }

    // Valida e envia para o banco
    private void salvarDados() {

        String nome = edtNome.getText().toString().trim();
        String dataNascimento = edtDataNascimento.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String telefone = edtTelefone.getText().toString().trim();
        String localizacao = edtLocalizacao.getText().toString().trim();

        // "Selecione" (posição 0) vira vazio
        String genero = spinnerGenero.getSelectedItemPosition() == 0
                ? ""
                : spinnerGenero.getSelectedItem().toString();

        if (nome.isEmpty()) {
            edtNome.setError("Digite seu nome");
            edtNome.requestFocus();
            return;
        }

        if (email.isEmpty()
                || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Digite um e-mail válido");
            edtEmail.requestFocus();
            return;
        }

        if (!dataNascimento.isEmpty()
                && !dataNascimento.matches("\\d{2}/\\d{2}/\\d{4}")) {
            edtDataNascimento.setError("Use o formato 01/01/2000");
            edtDataNascimento.requestFocus();
            return;
        }

        int id = Sessao.getId(this);

        if (id <= 0) {
            Toast.makeText(
                    this,
                    "Faça login novamente para salvar seus dados",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        btnSalvarDados.setEnabled(false);

        ApiService api = RetrofitCliente
                .getClient()
                .create(ApiService.class);

        PerfilDados dados = new PerfilDados(
                id, nome, email, dataNascimento,
                telefone, localizacao, genero
        );

        api.atualizarPerfil(dados).enqueue(new Callback<PerfilDados>() {

            @Override
            public void onResponse(
                    Call<PerfilDados> call,
                    Response<PerfilDados> response) {

                btnSalvarDados.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {

                    PerfilDados resultado = response.body();

                    Toast.makeText(
                            AlterarDados.this,
                            resultado.getMensagem(),
                            Toast.LENGTH_LONG
                    ).show();

                    if (resultado.isSucesso()) {
                        Sessao.salvar(AlterarDados.this, id, nome, email);
                        finish(); // volta ao Perfil, que recarrega os dados
                    }

                } else {
                    Toast.makeText(
                            AlterarDados.this,
                            "Erro no servidor (HTTP " + response.code() + ")",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilDados> call, Throwable t) {

                btnSalvarDados.setEnabled(true);

                Toast.makeText(
                        AlterarDados.this,
                        "Erro de conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}