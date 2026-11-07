package com.example.techwaytcc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.example.techwaytcc.lari.bueno.CursoActivity;
import com.example.techwaytcc.lari.bueno.FaculdadesActivity;

import com.example.techwaytcc.manu.ResultadoTesteActivity;
import com.example.techwaytcc.nylly.AreaTI;

public class Navegacao {

    public static final int TESTE = 0;
    public static final int RESULTADO = 1;
    public static final int AREAS = 2;
    public static final int CURSOS = 3;
    public static final int FACULDADES = 4;

    private static final String PREFS = "techway_prefs";

    // Liga uma aba (botãozinho de cima) ao destino dela
    public static void ligar(Activity atual, int telaAtual, View aba, int destino) {

        if (aba == null) {
            return;
        }

        aba.setOnClickListener(v -> abrir(atual, telaAtual, destino));
    }

    private static void abrir(Activity atual, int telaAtual, int destino) {

        // Já está nessa tela
        if (destino == telaAtual) {
            return;
        }

        Intent intent;

        switch (destino) {

            case TESTE:
                intent = new Intent(
                        atual,
                        com.example.techwaytcc.manu.MainActivity.class
                );
                break;

            case RESULTADO:
                int[] respostas = lerRespostas(atual);

                if (respostas == null) {
                    Toast.makeText(
                            atual,
                            "Responda o teste para ver seu resultado",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                intent = new Intent(atual, ResultadoTesteActivity.class);
                intent.putExtra("respostas", respostas);
                break;

            case AREAS:
                intent = new Intent(atual, AreaTI.class);
                break;

            case CURSOS:
                intent = new Intent(atual, CursoActivity.class);
                break;

            case FACULDADES:
                intent = new Intent(atual, FaculdadesActivity.class);
                break;

            default:
                return;
        }

        atual.startActivity(intent);
    }

    // Guarda as respostas do último teste, para a aba "Resultado"
    public static void salvarRespostas(Context contexto, int[] respostas) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < respostas.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(respostas[i]);
        }

        contexto.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString("respostas", sb.toString())
                .apply();
    }

    public static int[] lerRespostas(Context contexto) {

        String texto = contexto
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getString("respostas", null);

        if (texto == null || texto.isEmpty()) {
            return null;
        }

        String[] partes = texto.split(",");
        int[] respostas = new int[partes.length];

        try {
            for (int i = 0; i < partes.length; i++) {
                respostas[i] = Integer.parseInt(partes[i].trim());
            }
        } catch (NumberFormatException e) {
            return null;
        }

        return respostas;
    }
}