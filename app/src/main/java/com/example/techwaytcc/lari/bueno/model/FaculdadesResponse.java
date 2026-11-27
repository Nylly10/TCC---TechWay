package com.example.techwaytcc.lari.bueno.model;

import java.util.List;

public class FaculdadesResponse {

    private boolean sucesso;
    private String mensagem;
    private List<Faculdade> faculdades;

    public boolean isSucesso() { return sucesso; }
    public String getMensagem() { return mensagem; }
    public List<Faculdade> getFaculdades() { return faculdades; }
}
