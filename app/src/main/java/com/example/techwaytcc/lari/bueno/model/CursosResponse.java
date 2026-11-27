package com.example.techwaytcc.lari.bueno.model;

import java.util.List;

public class CursosResponse {

    private boolean sucesso;
    private String mensagem;
    private List<Curso> cursos;

    public boolean isSucesso() { return sucesso; }
    public String getMensagem() { return mensagem; }
    public List<Curso> getCursos() { return cursos; }
}