package com.example.techwaytcc.lari.bueno;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techwaytcc.Navegacao;
import com.example.techwaytcc.R;

public class FaculdadesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_faculdades);

        Navegacao.ligar(this, Navegacao.FACULDADES, findViewById(R.id.chipTeste), Navegacao.TESTE);
        Navegacao.ligar(this, Navegacao.FACULDADES, findViewById(R.id.chipResultado), Navegacao.RESULTADO);
        Navegacao.ligar(this, Navegacao.FACULDADES, findViewById(R.id.chipAreas), Navegacao.FACULDADES);
        Navegacao.ligar(this, Navegacao.FACULDADES, findViewById(R.id.chipCursos), Navegacao.CURSOS);
    }
}
