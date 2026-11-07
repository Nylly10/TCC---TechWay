package com.example.techwaytcc.manu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techwaytcc.Navegacao;
import com.example.techwaytcc.R;

public class ResultadoTesteActivity extends AppCompatActivity {

    private ResultadoGraficoView grafico;

    private TextView txtAreaPrincipal;

    private LinearLayout btnRefazer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_resultado_teste);

        // COMPONENTES DA TELA
        grafico = findViewById(R.id.graficoResultado);
        txtAreaPrincipal = findViewById(R.id.txtAreaPrincipal);
        btnRefazer = findViewById(R.id.btnRefazer);

        // Botões de cima
        Navegacao.ligar(this, Navegacao.RESULTADO, findViewById(R.id.menuTeste), Navegacao.TESTE);
        Navegacao.ligar(this, Navegacao.RESULTADO, findViewById(R.id.menuResultado), Navegacao.RESULTADO);
        Navegacao.ligar(this, Navegacao.RESULTADO, findViewById(R.id.menuAreas), Navegacao.AREAS);

        // RECEBER RESPOSTAS
        int[] respostas = getIntent().getIntArrayExtra("respostas");

        // Caso não tenha recebido respostas
        if (respostas == null) {
            respostas = new int[18];
        }

        // CALCULAR RESULTADO
        calcularResultado(respostas);

        // BOTÃO REFAZER: volta para o teste
        btnRefazer.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ResultadoTesteActivity.this,
                    MainActivity.class
            );

            startActivity(intent);

            finish();
        });
    }

    private void calcularResultado(int[] respostas) {

        /*
         * Cada posição representa uma área:
         *
         * 0 = Design UI/UX
         * 1 = Programação
         * 2 = Ciência de Dados
         * 3 = Segurança
         * 4 = Sistemas Embarcados
         * 5 = Banco de Dados
         */

        float[] pontos = new float[6];
        int[] quantidade = new int[6];

        /*
         * As 18 perguntas são divididas
         * em 6 áreas, 3 perguntas para cada.
         */
        int[] areas = {
                Pergunta.UI_UX, Pergunta.UI_UX, Pergunta.UI_UX,
                Pergunta.PROGRAMACAO, Pergunta.PROGRAMACAO, Pergunta.PROGRAMACAO,
                Pergunta.CIENCIA_DADOS, Pergunta.CIENCIA_DADOS, Pergunta.CIENCIA_DADOS,
                Pergunta.SEGURANCA, Pergunta.SEGURANCA, Pergunta.SEGURANCA,
                Pergunta.SISTEMAS_EMBARCADOS, Pergunta.SISTEMAS_EMBARCADOS, Pergunta.SISTEMAS_EMBARCADOS,
                Pergunta.BANCO_DADOS, Pergunta.BANCO_DADOS, Pergunta.BANCO_DADOS
        };

        // SOMAR RESPOSTAS
        for (int i = 0; i < respostas.length && i < areas.length; i++) {

            // 0 significa que não respondeu
            if (respostas[i] == 0) {
                continue;
            }

            int area = areas[i];

            pontos[area] += respostas[i];

            quantidade[area]++;
        }

        // CALCULAR PORCENTAGENS
        float[] porcentagens = new float[6];

        for (int i = 0; i < 6; i++) {

            if (quantidade[i] > 0) {

                float media = pontos[i] / quantidade[i];

                /*
                 * Converte a escala:
                 * 1 = 0%, 2 = 25%, 3 = 50%, 4 = 75%, 5 = 100%
                 */
                porcentagens[i] = ((media - 1) / 4f) * 100f;
            }
        }

        // ENVIAR PARA O GRÁFICO
        grafico.definirValores(porcentagens);

        // MOSTRAR MAIOR AFINIDADE
        mostrarMaiorArea(porcentagens);
    }

    private void mostrarMaiorArea(float[] porcentagens) {

        int maior = 0;

        for (int i = 1; i < 6; i++) {

            if (porcentagens[i] > porcentagens[maior]) {
                maior = i;
            }
        }

        String[] nomes = {
                "Design UI/UX",
                "Programação",
                "Ciência de Dados",
                "Segurança da Informação",
                "Sistemas Embarcados",
                "Banco de Dados"
        };

        txtAreaPrincipal.setText(
                "Sua maior afinidade é com\n" + nomes[maior] + "."
        );
    }
}