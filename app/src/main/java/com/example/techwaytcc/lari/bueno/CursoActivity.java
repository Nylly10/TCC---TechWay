package com.example.techwaytcc.lari.bueno;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techwaytcc.Navegacao;
import com.example.techwaytcc.R;

public class CursoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_curso_tecnico);

        Navegacao.ligar(this, Navegacao.CURSOS, findViewById(R.id.tabTeste), Navegacao.TESTE);
        Navegacao.ligar(this, Navegacao.CURSOS, findViewById(R.id.tabResultado), Navegacao.RESULTADO);
        Navegacao.ligar(this, Navegacao.CURSOS, findViewById(R.id.tabFaculdades), Navegacao.FACULDADES);
        Navegacao.ligar(this, Navegacao.CURSOS, findViewById(R.id.tabCursos), Navegacao.CURSOS);
    }
}
