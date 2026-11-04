package com.example.techwaytcc.lari.bueno.api;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techwaytcc.R;
import com.example.techwaytcc.carol.BlogActivity;
import com.example.techwaytcc.carol.MainActivity1;
import com.example.techwaytcc.lari.bueno.model.LoginRequest;
import com.example.techwaytcc.lari.bueno.model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnEntrar;
    private TextView txtCliqueAqui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Inicialização dos componentes
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        txtCliqueAqui = findViewById(R.id.txtCliqueAqui);

        // Botão de login
        btnEntrar.setOnClickListener(v -> fazerLogin());

        // Acesso à tela de cadastro
        txtCliqueAqui.setOnClickListener(v -> {
            Intent intent = new Intent(
                    LoginActivity.this, MainActivity1.class);
            startActivity(intent);
        });
    }

    // Método responsável por realizar o login
    private void fazerLogin() {

        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();

        // Validação do e-mail
        if (email.isEmpty()) {
            edtEmail.setError("Digite seu e-mail");
            edtEmail.requestFocus();
            return;
        }

        // Validação da senha
        if (senha.isEmpty()) {
            edtSenha.setError("Digite sua senha");
            edtSenha.requestFocus();
            return;
        }

        // Criação da requisição
        LoginRequest request = new LoginRequest(email, senha);

        // Configuração da API
        ApiService api = RetrofitCliente
                .getClient()
                .create(ApiService.class);

        Call<LoginResponse> chamada = api.login(request);

        // Envio da requisição
        chamada.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(
                    Call<LoginResponse> call,
                    Response<LoginResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null) {

                    LoginResponse resultado = response.body();

                    if (resultado.isSucesso()) {

                        Toast.makeText(
                                LoginActivity.this,
                                "Login realizado!",
                                Toast.LENGTH_SHORT
                        ).show();

                        abrirPrincipal();

                    } else {

                        Toast.makeText(
                                LoginActivity.this,
                                resultado.getMensagem(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                } else {

                    Toast.makeText(
                            LoginActivity.this,
                            "Erro no servidor",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<LoginResponse> call,
                    Throwable t) {

                Toast.makeText(
                        LoginActivity.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    // Método para abrir a tela principal
    private void abrirPrincipal() {

        Intent intent = new Intent(
                LoginActivity.this,
                BlogActivity.class
        );

        startActivity(intent);
        finish();
    }
}
