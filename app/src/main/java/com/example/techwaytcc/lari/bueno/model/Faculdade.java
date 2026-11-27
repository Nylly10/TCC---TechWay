package com.example.techwaytcc.lari.bueno.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Faculdade {

    private int id;
    private String nome;
    private String sigla;
    private String cidade;
    private String estado;
    private String site;
    private String tipo;

    @SerializedName("total_cursos")
    private int totalCursos;

    // Quantidade de cursos por área, ex.: {"Programação": 2}
    private Map<String, Integer> areas;

    public int getId() { return id; }
    public String getNome() { return nome == null ? "" : nome; }
    public String getSigla() { return sigla == null ? "" : sigla; }
    public String getCidade() { return cidade == null ? "" : cidade; }
    public String getEstado() { return estado == null ? "" : estado; }
    public String getSite() { return site == null ? "" : site; }
    public String getTipo() { return tipo == null ? "" : tipo; }
    public int getTotalCursos() { return totalCursos; }
    public Map<String, Integer> getAreas() { return areas; }

    // Mesmo texto que aparece em "instituicao" na lista de cursos
    public String getChave() {
        return getSigla().isEmpty() ? getNome() : getSigla();
    }

    // Texto do quadradinho: a sigla, ou as iniciais do nome
    public String getMonograma() {

        if (!getSigla().isEmpty()) {
            return getSigla();
        }

        StringBuilder sb = new StringBuilder();

        for (String parte : getNome().split(" ")) {
            if (parte.length() > 2 && sb.length() < 4) {
                sb.append(Character.toUpperCase(parte.charAt(0)));
            }
        }

        return sb.length() == 0 ? "?" : sb.toString();
    }
}