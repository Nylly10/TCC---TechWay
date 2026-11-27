package com.example.techwaytcc.lari.bueno;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techwaytcc.Navegacao;
import com.example.techwaytcc.R;
import com.example.techwaytcc.lari.bueno.api.ApiService;
import com.example.techwaytcc.lari.bueno.api.RetrofitCliente;
import com.example.techwaytcc.lari.bueno.model.Faculdade;
import com.example.techwaytcc.lari.bueno.model.FaculdadesResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaculdadesActivity extends AppCompatActivity implements FaculdadeAdapter.Acoes {

    private static final String PREFS = "techway_prefs";
    private static final String CHAVE_FAVORITAS = "faculdades_favoritas";

    private TextView txtContador, txtVazio, txtSalvos, btnLimpar;
    private TextView txtRecomLabel, txtRecomArea, txtRecomTexto;
    private MaterialButton btnRecomAcao;
    private MaterialCardView btnSalvos;
    private ImageView imgSalvos;
    private Spinner spinnerCidade, spinnerTipo;
    private SearchView barraPesquisa;

    private final List<Faculdade> todas = new ArrayList<>();
    private FaculdadeAdapter adapter;

    // Filtros atuais
    private String busca = "";
    private String cidade = "";
    private String tipo = "";
    private boolean soSalvas = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_faculdades);

        txtContador = findViewById(R.id.txtContador);
        txtVazio = findViewById(R.id.txtVazio);
        txtSalvos = findViewById(R.id.txtSalvos);
        btnLimpar = findViewById(R.id.btnLimpar);
        txtRecomLabel = findViewById(R.id.txtRecomLabel);
        txtRecomArea = findViewById(R.id.txtRecomArea);
        txtRecomTexto = findViewById(R.id.txtRecomTexto);
        btnRecomAcao = findViewById(R.id.btnRecomAcao);
        btnSalvos = findViewById(R.id.btnSalvos);
        imgSalvos = findViewById(R.id.imgSalvos);
        spinnerCidade = findViewById(R.id.spinnerCidade);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        barraPesquisa = findViewById(R.id.barra_pesquisa2);

        // Menus
        Navegacao.ligar(this, Navegacao.FACULDADES, findViewById(R.id.chipTeste), Navegacao.TESTE);
        Navegacao.ligar(this, Navegacao.FACULDADES, findViewById(R.id.chipResultado), Navegacao.RESULTADO);
        Navegacao.ligar(this, Navegacao.FACULDADES, findViewById(R.id.chipAreas), Navegacao.AREAS);
        Navegacao.ligar(this, Navegacao.FACULDADES, findViewById(R.id.chipCursos), Navegacao.CURSOS);
        Navegacao.configurarMenuInferior(this, Navegacao.FACULDADES);

        // Lista
        adapter = new FaculdadeAdapter(this);
        RecyclerView lista = findViewById(R.id.listaFaculdades);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adapter);

        txtRecomLabel.setText("ONDE ESTUDAR");
        txtRecomArea.setText("Carregando...");
        txtRecomTexto.setText("");

        configurarBusca();
        configurarBotoes();
        atualizarBotaoSalvas();

        carregarFaculdades();
    }

    // =====================================================
    // BUSCA E BOTÕES
    // =====================================================

    private void configurarBusca() {

        barraPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                busca = query;
                aplicarFiltros();
                barraPesquisa.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String texto) {
                busca = texto;
                aplicarFiltros();
                return true;
            }
        });
    }

    private void configurarBotoes() {

        btnSalvos.setOnClickListener(v -> {
            soSalvas = !soSalvas;
            atualizarBotaoSalvas();
            aplicarFiltros();
        });

        btnLimpar.setOnClickListener(v -> {
            soSalvas = false;
            atualizarBotaoSalvas();
            barraPesquisa.setQuery("", false);
            spinnerCidade.setSelection(0);
            spinnerTipo.setSelection(0);
        });
    }

    private void atualizarBotaoSalvas() {

        int fundo = soSalvas ? 0xFFA3B6E8 : 0xFF1D2740;
        int texto = soSalvas ? 0xFF1E2A5E : 0xFFDCE6FB;

        btnSalvos.setCardBackgroundColor(fundo);
        txtSalvos.setTextColor(texto);
        imgSalvos.setColorFilter(texto);
    }

    // =====================================================
    // CARREGAR DO BANCO
    // =====================================================

    private void carregarFaculdades() {

        ApiService api = RetrofitCliente
                .getClient()
                .create(ApiService.class);

        api.listarFaculdades().enqueue(new Callback<FaculdadesResponse>() {

            @Override
            public void onResponse(
                    Call<FaculdadesResponse> call,
                    Response<FaculdadesResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isSucesso()) {

                    todas.clear();

                    if (response.body().getFaculdades() != null) {
                        todas.addAll(response.body().getFaculdades());
                    }

                    montarFiltros();
                    aplicarFiltros();
                    configurarDestaque();

                } else {
                    mostrarErro("Não foi possível carregar as instituições.");
                }
            }

            @Override
            public void onFailure(Call<FaculdadesResponse> call, Throwable t) {
                mostrarErro("Sem conexão com o servidor.");
            }
        });
    }

    private void mostrarErro(String mensagem) {
        txtContador.setText("Erro ao carregar");
        txtRecomArea.setText("Indisponível");
        txtVazio.setText(mensagem);
        txtVazio.setVisibility(View.VISIBLE);
    }

    // =====================================================
    // FILTROS (as opções vêm dos dados)
    // =====================================================

    private void montarFiltros() {

        TreeSet<String> cidades = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        TreeSet<String> tipos = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        for (Faculdade f : todas) {
            if (!f.getCidade().isEmpty()) cidades.add(f.getCidade());
            if (!f.getTipo().isEmpty()) tipos.add(f.getTipo());
        }

        List<String> listaCidades = new ArrayList<>();
        listaCidades.add("Todas as cidades");
        listaCidades.addAll(cidades);
        configurarSpinner(spinnerCidade, listaCidades, true);

        List<String> listaTipos = new ArrayList<>();
        listaTipos.add("Todos os tipos");
        listaTipos.addAll(tipos);
        configurarSpinner(spinnerTipo, listaTipos, false);
    }

    private void configurarSpinner(Spinner spinner, List<String> itens, boolean ehCidade) {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                R.layout.item_spinner,
                itens
        );
        arrayAdapter.setDropDownViewResource(R.layout.item_spinner);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                String escolhido = pos == 0 ? "" : parent.getItemAtPosition(pos).toString();

                if (ehCidade) {
                    cidade = escolhido;
                } else {
                    tipo = escolhido;
                }

                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void aplicarFiltros() {

        List<Faculdade> resultado = new ArrayList<>();
        String q = normalizar(busca);

        for (Faculdade f : todas) {

            if (soSalvas && !ehFavorito(f)) continue;

            if (!cidade.isEmpty() && !f.getCidade().equalsIgnoreCase(cidade)) continue;

            if (!tipo.isEmpty() && !f.getTipo().equalsIgnoreCase(tipo)) continue;

            if (!q.isEmpty()) {
                String texto = normalizar(
                        f.getNome() + " " + f.getSigla() + " "
                                + f.getCidade() + " " + f.getEstado()
                );
                if (!texto.contains(q)) continue;
            }

            resultado.add(f);
        }

        adapter.atualizar(resultado);

        int n = resultado.size();
        txtContador.setText(n == 1 ? "1 instituição encontrada" : n + " instituições encontradas");

        if (n == 0) {
            txtVazio.setText(
                    soSalvas
                            ? "Você ainda não salvou nenhuma instituição."
                            : "Nenhuma instituição encontrada."
            );
            txtVazio.setVisibility(View.VISIBLE);
        } else {
            txtVazio.setVisibility(View.GONE);
        }
    }

    // =====================================================
    // DESTAQUE: ONDE ESTUDAR A ÁREA DO TESTE
    // =====================================================

    private void configurarDestaque() {

        int[] r = Navegacao.lerRespostas(this);

        // Ainda não fez o teste: convite
        if (r == null || r.length < 18) {

            txtRecomLabel.setText("DESCUBRA ONDE ESTUDAR");
            txtRecomArea.setText("Faça o teste vocacional");
            txtRecomTexto.setText("Responda 18 perguntas e veja a instituição que mais combina com você.");
            btnRecomAcao.setText("Fazer o teste");
            btnRecomAcao.setVisibility(View.VISIBLE);

            Navegacao.ligar(this, Navegacao.FACULDADES, btnRecomAcao, Navegacao.TESTE);
            return;
        }

        String area = areaDoTeste(r);

        txtRecomLabel.setText("ONDE ESTUDAR " + area.toUpperCase(new Locale("pt", "BR")));

        Faculdade melhor = null;
        int maior = 0;

        for (Faculdade f : todas) {
            int n = contarArea(f, area);
            if (n > maior) {
                maior = n;
                melhor = f;
            }
        }

        if (melhor == null) {
            txtRecomArea.setText("Ainda sem instituições");
            txtRecomTexto.setText("Não há cursos de " + area + " cadastrados por enquanto.");
            btnRecomAcao.setVisibility(View.GONE);
            return;
        }

        Faculdade destaque = melhor;

        txtRecomArea.setText(destaque.getNome());
        txtRecomTexto.setText(
                "É a instituição com mais cursos da área que mais combina com o seu teste vocacional ("
                        + maior + ")."
        );
        btnRecomAcao.setText("Ver cursos");
        btnRecomAcao.setVisibility(View.VISIBLE);
        btnRecomAcao.setOnClickListener(v -> aoVerCursos(destaque));
    }

    // Mesma conta do teste: 6 áreas, 3 perguntas cada
    private String areaDoTeste(int[] r) {

        String[] nomes = {
                "UI/UX",
                "Programação",
                "Ciência de Dados",
                "Segurança",
                "Sistemas Embarcados",
                "Banco de Dados"
        };

        int melhor = 0;
        float melhorMedia = -1;

        for (int a = 0; a < 6; a++) {

            float soma = 0;
            int qtd = 0;

            for (int i = a * 3; i < a * 3 + 3; i++) {
                if (r[i] > 0) {
                    soma += r[i];
                    qtd++;
                }
            }

            float media = qtd > 0 ? soma / qtd : 0;

            if (media > melhorMedia) {
                melhorMedia = media;
                melhor = a;
            }
        }

        return nomes[melhor];
    }

    private int contarArea(Faculdade f, String area) {

        if (f.getAreas() == null) return 0;

        String alvo = normalizar(area);
        int total = 0;

        for (Map.Entry<String, Integer> e : f.getAreas().entrySet()) {

            String nome = normalizar(e.getKey());

            if (nome.isEmpty()) continue;

            if (nome.contains(alvo) || alvo.contains(nome)) {
                total += e.getValue() == null ? 0 : e.getValue();
            }
        }

        return total;
    }

    // =====================================================
    // AÇÕES DO CARTÃO
    // =====================================================

    private SharedPreferences prefs() {
        return getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    private String chave(Faculdade f) {
        return f.getId() > 0 ? String.valueOf(f.getId()) : f.getNome();
    }

    @Override
    public boolean ehFavorito(Faculdade f) {
        return prefs()
                .getStringSet(CHAVE_FAVORITAS, new HashSet<>())
                .contains(chave(f));
    }

    @Override
    public void aoFavoritar(Faculdade f) {

        Set<String> atual = new HashSet<>(
                prefs().getStringSet(CHAVE_FAVORITAS, new HashSet<>())
        );

        String k = chave(f);

        if (!atual.remove(k)) {
            atual.add(k);
        }

        prefs().edit().putStringSet(CHAVE_FAVORITAS, atual).apply();

        aplicarFiltros();
    }

    @Override
    public void aoVerCursos(Faculdade f) {

        Intent intent = new Intent(this, CursoActivity.class);
        intent.putExtra("instituicao", f.getChave());
        startActivity(intent);
    }

    @Override
    public void aoAbrirSite(Faculdade f) {

        String site = f.getSite().trim();

        if (site.isEmpty()) {
            Toast.makeText(this, "Site não cadastrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!site.startsWith("http://") && !site.startsWith("https://")) {
            site = "https://" + site;
        }

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(site)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Não foi possível abrir o site.", Toast.LENGTH_SHORT).show();
        }
    }

    // =====================================================
    // AUXILIAR
    // =====================================================

    private String normalizar(String texto) {
        if (texto == null) return "";
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim();
    }
}