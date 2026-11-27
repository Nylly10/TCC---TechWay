package com.example.techwaytcc.nylly;

import android.content.Intent;
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
import com.example.techwaytcc.Navegacao;

import com.example.techwaytcc.R;
import com.example.techwaytcc.Sessao;
import com.example.techwaytcc.lari.bueno.api.ApiService;
import com.example.techwaytcc.lari.bueno.api.RetrofitCliente;
import com.example.techwaytcc.lari.bueno.model.PerfilDados;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Perfil extends AppCompatActivity {

    ImageView fotoPerfil;
    Spinner spinnerGenero;

    TextView tvNome;
    TextView tvEmail;
    TextView tvDataNascimento;
    TextView tvTelefone;
    TextView tvLocalizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.perfil_activity);
        Navegacao.configurarMenuInferior(this, Navegacao.PERFIL);

        // Referências dos componentes

        fotoPerfil = findViewById(R.id.fotoPerfil);
        // Menu de cima: leva para as outras telas
        Navegacao.ligar(this, Navegacao.PERFIL, findViewById(R.id.chipTeste), Navegacao.TESTE);
        Navegacao.ligar(this, Navegacao.PERFIL, findViewById(R.id.chipResultado), Navegacao.RESULTADO);
        Navegacao.ligar(this, Navegacao.PERFIL, findViewById(R.id.chipAreas), Navegacao.AREAS);
        Navegacao.ligar(this, Navegacao.PERFIL, findViewById(R.id.chipCursos), Navegacao.CURSOS);
        spinnerGenero = findViewById(R.id.spinnerGenero);

        tvNome = findViewById(R.id.tvNome);
        tvEmail = findViewById(R.id.tvEmail);
        tvDataNascimento = findViewById(R.id.tvDataNascimento);
        tvTelefone = findViewById(R.id.tvTelefone);
        tvLocalizacao = findViewById(R.id.tvLocalizacao);

        // Mostra já o que está guardado do login
        tvNome.setText(Sessao.getNome(this));
        tvEmail.setText(Sessao.getEmail(this));

        // =====================================================
        // SPINNER DE GÊNERO (só mostra; quem altera é a outra tela)
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
        spinnerGenero.setEnabled(false);

        // =====================================================
        // BOTÃO ALTERAR DADOS
        // =====================================================
        findViewById(R.id.btnAlterarDados).setOnClickListener(v ->
                startActivity(new Intent(Perfil.this, AlterarDados.class))
        );

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

    // Recarrega os dados toda vez que a tela volta (depois de salvar)
    @Override
    protected void onResume() {
        super.onResume();
        carregarPerfil();
    }

    private void carregarPerfil() {

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

                    mostrar(response.body());
                }
            }

            @Override
            public void onFailure(Call<PerfilDados> call, Throwable t) {
                // Sem conexão: continua mostrando o que já está na tela
            }
        });
    }

    private void mostrar(PerfilDados p) {

        tvNome.setText(texto(p.getNome()));
        tvEmail.setText(texto(p.getEmail()));
        tvDataNascimento.setText(texto(p.getDataNascimento()));
        tvTelefone.setText(texto(p.getTelefone()));
        tvLocalizacao.setText(texto(p.getLocalizacao()));

        // Seleciona o gênero salvo
        ArrayAdapter<?> adapter = (ArrayAdapter<?>) spinnerGenero.getAdapter();

        for (int i = 0; i < adapter.getCount(); i++) {
            if (String.valueOf(adapter.getItem(i)).equals(p.getGenero())) {
                spinnerGenero.setSelection(i);
                return;
            }
        }

        spinnerGenero.setSelection(0);
    }

    private String texto(String valor) {
        return valor == null ? "" : valor;
    }
}