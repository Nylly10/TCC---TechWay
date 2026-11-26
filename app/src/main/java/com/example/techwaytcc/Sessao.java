package com.example.techwaytcc;

import android.content.Context;
import android.content.SharedPreferences;

// Guarda os dados de quem está logado
public class Sessao {

    private static final String PREFS = "techway_sessao";

    private static SharedPreferences prefs(Context contexto) {
        return contexto.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static void salvar(Context contexto, int id, String nome, String email) {
        prefs(contexto)
                .edit()
                .putInt("id", id)
                .putString("nome", nome == null ? "" : nome)
                .putString("email", email == null ? "" : email)
                .apply();
    }

    public static String getNome(Context contexto) {
        return prefs(contexto).getString("nome", "");
    }

    public static String getEmail(Context contexto) {
        return prefs(contexto).getString("email", "");
    }

    public static int getId(Context contexto) {
        return prefs(contexto).getInt("id", 0);
    }

    public static void sair(Context contexto) {
        prefs(contexto).edit().clear().apply();
    }
}
