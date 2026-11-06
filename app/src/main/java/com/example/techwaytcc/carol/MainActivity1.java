
package com.example.techwaytcc.carol;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techwaytcc.R;
import com.example.techwaytcc.lari.bueno.api.ApiService;
import com.example.techwaytcc.lari.bueno.api.RetrofitCliente;
import com.example.techwaytcc.lari.bueno.model.CadastroRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity1 extends AppCompatActivity {

    private EditText editname, editemail, editsenha, editconfsenha;
    private Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        editname = findViewById(R.id.editname);
        editemail = findViewById(R.id.editemail);
        editsenha = findViewById(R.id.editsenha);
        editconfsenha = findViewById(R.id.editconfsenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(v -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {

        String nome = editname.getText().toString().trim();
        String email = editemail.getText().toString().trim();
        String senha = editsenha.getText().toString();
        String confirmarSenha = editconfsenha.getText().toString();

        if (nome.isEmpty() || email.isEmpty()
                || senha.isEmpty() || confirmarSenha.isEmpty()) {

            Toast.makeText(this,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editemail.setError("Digite um e-mail válido");
            return;
        }

        if (senha.length() < 6) {
            editsenha.setError("A senha deve ter pelo menos 6 caracteres");
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            editconfsenha.setError("As senhas não coincidem");
            return;
        }

        btnCadastrar.setEnabled(false);

        CadastroRequest request =
                new CadastroRequest(nome, email, senha);

        ApiService apiService =
                RetrofitCliente.getClient().create(ApiService.class);

        apiService.cadastrar(request).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(
                    Call<ResponseBody> call,
                    Response<ResponseBody> response) {

                btnCadastrar.setEnabled(true);

                try {
                    String resposta = "";

                    if (response.body() != null) {
                        resposta = response.body().string();
                    } else if (response.errorBody() != null) {
                        resposta = response.errorBody().string();
                    }

                    new androidx.appcompat.app.AlertDialog.Builder(MainActivity1.this)
                            .setTitle("Resposta do servidor")
                            .setMessage(
                                    "HTTP: " + response.code()
                                            + "\n\nTamanho da resposta: " + resposta.length()
                                            + "\n\nConteúdo:\n[" + resposta + "]"
                            )
                            .setPositiveButton("OK", null)
                            .show();

                } catch (Exception e) {
                    Toast.makeText(
                            MainActivity1.this,
                            "Erro: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<ResponseBody> call,
                    Throwable t) {

                btnCadastrar.setEnabled(true);

                Toast.makeText(
                        MainActivity1.this,
                        "Erro de conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}
