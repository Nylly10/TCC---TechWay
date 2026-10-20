package com.example.techwaytcc.nylly;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techwaytcc.R;

public class AreaTI extends AppCompatActivity {

    LinearLayout btnProgramacao;
    LinearLayout conteudoProgramacao;

    LinearLayout btnCienciasDados;
    LinearLayout conteudoCienciasDados;

    LinearLayout btnInteligenciaArtificial;
    LinearLayout conteudoInteligenciaArtificial;

    LinearLayout btnBancoDados;
    LinearLayout conteudoBancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(
                getWindow(),
                false
        );

        setContentView(R.layout.area_ti_activity);

        View layoutPrincipal = findViewById(R.id.layoutPrincipal);

        ViewCompat.setOnApplyWindowInsetsListener(
                layoutPrincipal,
                (v, insets) -> {

                    Insets barras = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                                    | WindowInsetsCompat.Type.displayCutout()
                    );

                    v.setPadding(
                            v.getPaddingLeft(),
                            barras.top,
                            v.getPaddingRight(),
                            barras.bottom
                    );

                    return insets;
                }
        );

        ViewCompat.requestApplyInsets(layoutPrincipal);

        btnProgramacao = findViewById(R.id.btnDesenvolvimento);
        conteudoProgramacao = findViewById(R.id.conteudoDesenvolvimento);

        btnCienciasDados = findViewById(R.id.btnCienciasDados);
        conteudoCienciasDados = findViewById(R.id.conteudoCienciasDados);

        btnInteligenciaArtificial = findViewById(R.id.btnCiberseguranca);
        conteudoInteligenciaArtificial = findViewById(R.id.conteudoCiebrseguranca);

        btnBancoDados = findViewById(R.id.btnDesing);
        conteudoBancoDados = findViewById(R.id.conteudoDesign);

        btnProgramacao.setOnClickListener(v -> {
            boolean abrir =
                    conteudoProgramacao.getVisibility() == View.GONE;

            fecharTodos();

            if (abrir) {
                conteudoProgramacao.setVisibility(View.VISIBLE);
            }

            configurarAcordeao();
        });

        btnCienciasDados.setOnClickListener(v -> {
            boolean abrir =
                    conteudoCienciasDados.getVisibility() == View.GONE;

            fecharTodos();

            if (abrir) {
                conteudoCienciasDados.setVisibility(View.VISIBLE);
            }
        });

        btnInteligenciaArtificial.setOnClickListener(v -> {
            boolean abrir =
                    conteudoInteligenciaArtificial.getVisibility() == View.GONE;

            fecharTodos();

            if (abrir) {
                conteudoInteligenciaArtificial.setVisibility(View.VISIBLE);
            }
        });

        btnBancoDados.setOnClickListener(v -> {
            boolean abrir =
                    conteudoBancoDados.getVisibility() == View.GONE;

            fecharTodos();

            if (abrir) {
                conteudoBancoDados.setVisibility(View.VISIBLE);
            }
        });
    }

    private void configurarAcordeao() {
        btnProgramacao.setOnClickListener(v ->
                alternarConteudo(conteudoProgramacao));

        btnCienciasDados.setOnClickListener(v ->
                alternarConteudo(conteudoCienciasDados));

        btnInteligenciaArtificial.setOnClickListener(v ->
                alternarConteudo(conteudoInteligenciaArtificial));

        btnBancoDados.setOnClickListener(v ->
                alternarConteudo(conteudoBancoDados));
    }



    private void alternarConteudo(LinearLayout conteudoSelecionado) {
        boolean deveAbrir =
                conteudoSelecionado.getVisibility() != View.VISIBLE;

        fecharTodos();

        if (deveAbrir) {
            conteudoSelecionado.setVisibility(View.VISIBLE);
        }
    }

    private void fecharTodos() {
        conteudoProgramacao.setVisibility(View.GONE);
        conteudoCienciasDados.setVisibility(View.GONE);
        conteudoInteligenciaArtificial.setVisibility(View.GONE);
        conteudoBancoDados.setVisibility(View.GONE);
    }
}