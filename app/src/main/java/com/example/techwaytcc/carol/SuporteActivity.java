package com.example.techwaytcc.carol;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techwaytcc.Navegacao;
import com.example.techwaytcc.R;

public class SuporteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.suporte_main);

        // Botões de cima (só funcionam se tiverem id no layout)
        Navegacao.ligar(this, Navegacao.SUPORTE, findViewById(R.id.chipTeste), Navegacao.TESTE);
        Navegacao.ligar(this, Navegacao.SUPORTE, findViewById(R.id.chipResultado), Navegacao.RESULTADO);
        Navegacao.ligar(this, Navegacao.SUPORTE, findViewById(R.id.chipAreas), Navegacao.AREAS);
        Navegacao.ligar(this, Navegacao.SUPORTE, findViewById(R.id.chipCursos), Navegacao.CURSOS);

        // Menu de baixo
        Navegacao.configurarMenuInferior(this, Navegacao.SUPORTE);
    }
}
