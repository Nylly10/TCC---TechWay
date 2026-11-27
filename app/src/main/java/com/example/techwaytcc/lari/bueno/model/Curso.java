package com.example.techwaytcc.lari.bueno.model;

public class Curso {

    private int id;
    private String nome;
    private String instituicao;
    private String cidade;
    private String duracao;
    private String modalidade;
    private String area;
    private String descricao;

    public int getId() { return id; }
    public String getNome() { return nome == null ? "" : nome; }
    public String getInstituicao() { return instituicao == null ? "" : instituicao; }
    public String getCidade() { return cidade == null ? "" : cidade; }
    public String getDuracao() { return duracao == null ? "" : duracao; }
    public String getModalidade() { return modalidade == null ? "" : modalidade; }
    public String getArea() { return area == null ? "" : area; }
    public String getDescricao() { return descricao == null ? "" : descricao; }
}