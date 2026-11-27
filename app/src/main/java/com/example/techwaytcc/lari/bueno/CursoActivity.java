package com.example.techwaytcc.lari.bueno;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techwaytcc.Navegacao;
import com.example.techwaytcc.R;
import com.example.techwaytcc.lari.bueno.api.ApiService;
import com.example.techwaytcc.lari.bueno.api.RetrofitCliente;
import com.example.techwaytcc.lari.bueno.model.Curso;
import com.example.techwaytcc.lari.bueno.model.CursosResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursoActivity extends AppCompatActivity implements CursoAdapter.Acoes {

    private static final String PREFS = "techway_prefs";
    private static final String CHAVE_FAVORITOS = "cursos_favoritos";

    private TextView txtContador, txtVazio, txtSalvos, btnLimpar;
    private TextView txtRecomLabel, txtRecomArea, txtRecomTexto;
    private MaterialButton btnRecomAcao;
    private MaterialCardView btnSalvos;
    private ImageView imgSalvos;
    private Spinner spinnerCidade, spinnerCurso;
    private ChipGroup chipGroupAreas;
    private SearchView barraPesquisa;

    private final List<Curso> todos = new ArrayList<>();
    private CursoAdapter adapter;

    // Filtros atuais
    private String busca = "";
    private String cidade = "";
    private String curso = "";
    private String area = "";
    private String instituicao = "";
    private boolean soSalvos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_curso_tecnico);
        String extra = getIntent().getStringExtra("instituicao");
        if (extra != null) {
            instituicao = extra;
        }

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
        spinnerCurso = findViewById(R.id.spinnerCurso);
        chipGroupAreas = findViewById(R.id.chipGroupAreas);
        barraPesquisa = findViewById(R.id.barra_pesquisa2);

        // Menus
        Navegacao.ligar(this, Navegacao.CURSOS, findViewById(R.id.chipTeste), Navegacao.TESTE);
        Navegacao.ligar(this, Navegacao.CURSOS, findViewById(R.id.chipResultado), Navegacao.RESULTADO);
        Navegacao.ligar(this, Navegacao.CURSOS, findViewById(R.id.chipAreas), Navegacao.AREAS);
        Navegacao.ligar(this, Navegacao.CURSOS, findViewById(R.id.chipCursos), Navegacao.CURSOS);
        Navegacao.configurarMenuInferior(this, Navegacao.CURSOS);

        // Lista
        adapter = new CursoAdapter(this);
        RecyclerView lista = findViewById(R.id.listaCursos);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adapter);

        configurarBusca();
        configurarBotoes();
        configurarRecomendacao();
        atualizarBotaoSalvos();

        carregarCursos();
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
            soSalvos = !soSalvos;
            atualizarBotaoSalvos();
            aplicarFiltros();
        });

        btnLimpar.setOnClickListener(v -> {
            instituicao = "";
            soSalvos = false;
            atualizarBotaoSalvos();
            barraPesquisa.setQuery("", false);
            spinnerCidade.setSelection(0);
            spinnerCurso.setSelection(0);

            if (chipGroupAreas.getChildCount() > 0) {
                ((Chip) chipGroupAreas.getChildAt(0)).setChecked(true);
                aplicarFiltros();aplicarFiltros();
            }
        });
    }

    private void atualizarBotaoSalvos() {

        int fundo = soSalvos ? 0xFFA285DC : 0xFF1D2740;
        int texto = soSalvos ? 0xFF2E1A6B : 0xFFE6DEE9;

        btnSalvos.setCardBackgroundColor(fundo);
        txtSalvos.setTextColor(texto);
        imgSalvos.setColorFilter(texto);
    }

    // =====================================================
    // CARREGAR OS CURSOS DO BANCO
    // =====================================================

    private void carregarCursos() {

        ApiService api = RetrofitCliente
                .getClient()
                .create(ApiService.class);

        api.listarCursos().enqueue(new Callback<CursosResponse>() {

            @Override
            public void onResponse(
                    Call<CursosResponse> call,
                    Response<CursosResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isSucesso()) {

                    todos.clear();

                    if (response.body().getCursos() != null) {
                        todos.addAll(response.body().getCursos());
                    }

                    montarFiltros();
                    aplicarFiltros();

                } else {
                    mostrarErro("Não foi possível carregar os cursos.");
                }
            }

            @Override
            public void onFailure(Call<CursosResponse> call, Throwable t) {
                mostrarErro("Sem conexão com o servidor.");
            }
        });
    }

    private void mostrarErro(String mensagem) {
        txtContador.setText("Erro ao carregar");
        txtVazio.setText(mensagem);
        txtVazio.setVisibility(View.VISIBLE);
    }

    // =====================================================
    // FILTROS: cidades, cursos e áreas vêm dos dados
    // =====================================================

    private void montarFiltros() {

        TreeSet<String> cidades = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        TreeSet<String> nomes = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        TreeSet<String> areas = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        for (Curso c : todos) {
            if (!c.getCidade().isEmpty()) cidades.add(c.getCidade());
            if (!c.getNome().isEmpty()) nomes.add(c.getNome());
            if (!c.getArea().isEmpty()) areas.add(c.getArea());
        }

        // Cidade
        List<String> listaCidades = new ArrayList<>();
        listaCidades.add("Todas as cidades");
        listaCidades.addAll(cidades);
        configurarSpinner(spinnerCidade, listaCidades, true);

        // Curso
        List<String> listaNomes = new ArrayList<>();
        listaNomes.add("Todos os cursos");
        listaNomes.addAll(nomes);
        configurarSpinner(spinnerCurso, listaNomes, false);

        // Áreas (chips)
        chipGroupAreas.removeAllViews();
        adicionarChipArea("Todas", true);
        for (String a : areas) {
            adicionarChipArea(a, false);
        }

        chipGroupAreas.setOnCheckedStateChangeListener((group, ids) -> {

            if (ids.isEmpty()) {
                area = "";
            } else {
                Chip chip = group.findViewById(ids.get(0));
                area = chip.getTag() == null ? "" : chip.getTag().toString();
            }

            aplicarFiltros();
        });
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
                    curso = escolhido;
                }

                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void adicionarChipArea(String texto, boolean marcado) {

        Chip chip = new Chip(this);
        chip.setId(View.generateViewId());
        chip.setText(texto);
        chip.setTag(marcado ? "" : texto);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(false);

        int[][] estados = {
                new int[]{android.R.attr.state_checked},
                new int[]{}
        };

        chip.setChipBackgroundColor(
                new ColorStateList(estados, new int[]{0xFFA285DC, 0xFF1D2740})
        );
        chip.setTextColor(
                new ColorStateList(estados, new int[]{0xFF2E1A6B, 0xFFE6DEE9})
        );
        chip.setChipStrokeColor(ColorStateList.valueOf(0xFF3A4259));
        chip.setChipStrokeWidth(dp(1));

        chipGroupAreas.addView(chip);
        chip.setChecked(marcado);
    }

    private void aplicarFiltros() {

        List<Curso> resultado = new ArrayList<>();
        String q = normalizar(busca);

        for (Curso c : todos) {

            if (soSalvos && !ehFavorito(c)) continue;

            if (!instituicao.isEmpty()
                    && !c.getInstituicao().equalsIgnoreCase(instituicao)) continue;

            if (!cidade.isEmpty() && !c.getCidade().equalsIgnoreCase(cidade)) continue;

            if (!curso.isEmpty() && !c.getNome().equalsIgnoreCase(curso)) continue;

            if (!area.isEmpty() && !normalizar(c.getArea()).equals(normalizar(area))) continue;

            if (!q.isEmpty()) {
                String texto = normalizar(
                        c.getNome() + " " + c.getInstituicao() + " "
                                + c.getCidade() + " " + c.getArea()
                );
                if (!texto.contains(q)) continue;
            }

            resultado.add(c);
        }

        adapter.atualizar(resultado);

        int n = resultado.size();
        String textoContador = n == 1 ? "1 curso encontrado" : n + " cursos encontrados";
        if (!instituicao.isEmpty()) {
            textoContador += " · " + instituicao;
        }
        txtContador.setText(textoContador);

        if (n == 0) {
            txtVazio.setText(
                    soSalvos
                            ? "Você ainda não salvou nenhum curso."
                            : "Nenhum curso encontrado."
            );
            txtVazio.setVisibility(View.VISIBLE);
        } else {
            txtVazio.setVisibility(View.GONE);
        }
    }

    // =====================================================
    // COMBINA COM VOCÊ (usa o resultado do teste)
    // =====================================================

    private void configurarRecomendacao() {

        int[] r = Navegacao.lerRespostas(this);

        // Ainda não fez o teste: convite
        if (r == null || r.length < 18) {

            txtRecomLabel.setText("DESCUBRA SUA ÁREA");
            txtRecomArea.setText("Faça o teste vocacional");
            txtRecomTexto.setText("Responda 18 perguntas e veja os cursos que combinam com você.");
            btnRecomAcao.setText("Fazer o teste");

            Navegacao.ligar(this, Navegacao.CURSOS, btnRecomAcao, Navegacao.TESTE);
            return;
        }

        // Mesma ordem do teste: 6 áreas, 3 perguntas cada
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

        String recomendada = nomes[melhor];

        txtRecomLabel.setText("COMBINA COM VOCÊ");
        txtRecomArea.setText(recomendada);
        txtRecomTexto.setText("Pelo seu teste vocacional, estes cursos têm mais a ver com o seu perfil.");
        btnRecomAcao.setText("Ver cursos");

        btnRecomAcao.setOnClickListener(v -> selecionarArea(recomendada));
    }

    private void selecionarArea(String recomendada) {

        String alvo = normalizar(recomendada);

        for (int i = 0; i < chipGroupAreas.getChildCount(); i++) {

            Chip chip = (Chip) chipGroupAreas.getChildAt(i);
            String nomeChip = normalizar(chip.getTag() == null ? "" : chip.getTag().toString());

            if (nomeChip.isEmpty()) continue;

            if (nomeChip.contains(alvo) || alvo.contains(nomeChip)) {
                chip.setChecked(true);
                return;
            }
        }

        Toast.makeText(
                this,
                "Ainda não há cursos cadastrados dessa área.",
                Toast.LENGTH_SHORT
        ).show();
    }

    // =====================================================
    // AÇÕES DO CARTÃO (favoritar e detalhes)
    // =====================================================

    private SharedPreferences prefs() {
        return getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    private String chave(Curso c) {
        return c.getId() > 0 ? String.valueOf(c.getId()) : c.getNome();
    }

    @Override
    public boolean ehFavorito(Curso c) {
        return prefs()
                .getStringSet(CHAVE_FAVORITOS, new HashSet<>())
                .contains(chave(c));
    }

    @Override
    public void aoFavoritar(Curso c) {

        Set<String> atual = new HashSet<>(
                prefs().getStringSet(CHAVE_FAVORITOS, new HashSet<>())
        );

        String k = chave(c);

        if (!atual.remove(k)) {
            atual.add(k);
        }

        prefs().edit().putStringSet(CHAVE_FAVORITOS, atual).apply();

        // Se estiver vendo só os salvos, o curso sai da lista
        aplicarFiltros();
    }

    @Override
    public void aoVerDetalhes(Curso c) {

        StringBuilder sb = new StringBuilder();

        linha(sb, "Escola", c.getInstituicao());
        linha(sb, "Cidade", c.getCidade());
        linha(sb, "Duração", c.getDuracao());
        linha(sb, "Modalidade", c.getModalidade());
        linha(sb, "Área", c.getArea());

        if (!c.getDescricao().isEmpty()) {
            sb.append("\n").append(c.getDescricao());
        }

        new AlertDialog.Builder(this)
                .setTitle(c.getNome())
                .setMessage(sb.toString().trim())
                .setPositiveButton("Onde estudar", (d, w) ->
                        startActivity(new Intent(this, FaculdadesActivity.class)))
                .setNegativeButton("Fechar", null)
                .show();
    }

    private void linha(StringBuilder sb, String rotulo, String valor) {
        if (!valor.isEmpty()) {
            sb.append(rotulo).append(": ").append(valor).append("\n");
        }
    }

    // =====================================================
    // AUXILIARES
    // =====================================================

    private String normalizar(String texto) {
        if (texto == null) return "";
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim();
    }

    private int dp(int valor) {
        return Math.round(valor * getResources().getDisplayMetrics().density);
    }
}
