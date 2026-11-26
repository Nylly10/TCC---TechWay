package com.example.techwaytcc.lari.bueno.model;

import com.google.gson.annotations.SerializedName;

// Usada para pedir e para receber os dados do perfil
public class PerfilDados {

    private boolean sucesso;
    private String mensagem;

    private int id;
    private String nome;
    private String email;

    @SerializedName("data_nascimento")
    private String dataNascimento;

    private String telefone;
    private String localizacao;
    private String genero;

    // Só o id: usada para buscar
    public PerfilDados(int id) {
        this.id = id;
    }

    // Todos os campos: usada para salvar
    public PerfilDados(int id, String nome, String email,
                       String dataNascimento, String telefone,
                       String localizacao, String genero) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.localizacao = localizacao;
        this.genero = genero;
    }

    public boolean isSucesso() { return sucesso; }
    public String getMensagem() { return mensagem; }
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getDataNascimento() { return dataNascimento; }
    public String getTelefone() { return telefone; }
    public String getLocalizacao() { return localizacao; }
    public String getGenero() { return genero; }
}