package nupasd.research.appcompare.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import nupasd.research.appcompare.R;

public class definicao_parametros_avaliacao extends Activity {



    private ListView listaQTObservações;
    private Button confirmarParametros;
    private CheckBox salvarDados;
    private Button Voltar;
    private EditText quantidade;
    private RadioGroup niveis_confianca;


    private EditText Quantidade_Observacoes;
    private Button confirmar_Quantidade_Observacoes;



    int Quantidade_OBservacoes_Escolhida_EditText = 0;


    ArrayList<Integer> opcoes = new ArrayList<Integer>();

    int quantidade_APPS;
    boolean condicaoSalvar = false;

    ArrayList<String> itens = new ArrayList<String>();
    ArrayList<String> pacotes = new ArrayList<String>();


    int quantidadeObservacoesSelecionada;
    int tempo_escolhido;
    int nivelConfianca;

    //Variáveis de blindagem
    int number_OSB = 0, check_TrustLevel = 0;

    private TextView ChooseTrust_Level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicao_parametros_avaliacao);



        //quantidade = (EditText) findViewById(R.id.edt_Quantidade_Observacoes);

        //String aux = quantidade.getText().toString();

        //quantidadeObservacoesSelecionada = Integer.parseInt(aux);


        ChooseTrust_Level = (TextView) findViewById(R.id.txt_CHOOSE_TRUST_LEVEL);

        Quantidade_Observacoes = (EditText) findViewById(R.id.edt_Quantidade_Observacoes_Parametros_Avaliacao);

        Quantidade_Observacoes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Quantidade_Observacoes.getText().toString().equalsIgnoreCase("")){
                    number_OSB = 0;
                }else {
                    number_OSB = 1;

                    //Toast.makeText(definicao_parametros_avaliacao.this, "Selected Quantity: "+Quantidade_Observacoes.getText().toString(), Toast.LENGTH_SHORT).show();
                    tempo_escolhido = Integer.parseInt(Quantidade_Observacoes.getText().toString());
                    quantidadeObservacoesSelecionada = tempo_escolhido;
                    tempo_escolhido *= 1000;
                    tempo_escolhido += 400;
                }
            }
        });


        /*Quantidade_Observacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Quantidade_OBservacoes_Escolhida_EditText = Integer.parseInt(Quantidade_Observacoes.getText().toString());
                Toast.makeText(definicao_parametros_avaliacao.this, "Selected Quantity: "+quantidadeObservacoesSelecionada, Toast.LENGTH_SHORT).show();
                tempo_escolhido = Quantidade_OBservacoes_Escolhida_EditText;
                tempo_escolhido *= 1000;
                tempo_escolhido += 400;
                number_OSB = 1;
            }
        });*/




        Voltar = (Button) findViewById(R.id.btn_Voltar_Definicao_Parametros);

        Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent it = new Intent(definicao_parametros_avaliacao.this, escolha_apps.class);
                startActivity(it);
            }
        });


        //listaQTObservações = (ListView) findViewById(R.id.list_Quantidade_Observacoes_Avaliacao);


        /*for (int i = 1; i <= 200; i++){
            opcoes.add(i);
        }*/


        //ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, opcoes);

        //listaQTObservações.setAdapter(adapter);


        salvarDados = (CheckBox)  findViewById(R.id.chk_SalvarDadosEmLOG_TelaSelecaoDeAPPS);
        salvarDados.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    condicaoSalvar = true;
                }else{
                    condicaoSalvar = false;
                }
            }
        });



        //Quando o usuário seleciona uma quantidade, a variável é atualizada
        /*listaQTObservações.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                quantidadeObservacoesSelecionada = opcoes.get(i);
                Toast.makeText(definicao_parametros_avaliacao.this, "Selected Quantity: "+quantidadeObservacoesSelecionada, Toast.LENGTH_SHORT).show();
                tempo_escolhido = quantidadeObservacoesSelecionada;
                tempo_escolhido *= 1000;
                tempo_escolhido += 400;
                number_OSB = 1;
            }
        });*/


        niveis_confianca = (RadioGroup) findViewById(R.id.radioGroup_Nivel_Confianca);





        niveis_confianca.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radio = (RadioButton) findViewById(i);
                //Toast.makeText(definicao_parametros_avaliacao.this, "Vc Selecionou: "+radio.getText().toString(), Toast.LENGTH_SHORT).show();
                if (radio.getText().toString().contains("95")){
                    nivelConfianca = 95;
                }else{
                    nivelConfianca = 99;
                }
                check_TrustLevel = 1;
            }
        });

        confirmarParametros = (Button) findViewById(R.id.btn_confirmarParametros);


        Bundle bnd = getIntent().getExtras();


        if(bnd.containsKey("QUANTIDADE_APPS")){
            quantidade_APPS = bnd.getInt("QUANTIDADE_APPS");
        }

        if (bnd.containsKey("VALOR")){
            itens = bnd.getStringArrayList("VALOR");
        }

        if (bnd.containsKey("VALOR2")){
            pacotes = bnd.getStringArrayList("VALOR2");
        }


        if (quantidade_APPS == 1){
            Toast.makeText(definicao_parametros_avaliacao.this, "For Evaluations of an application it is not necessary" +
                    " to choose Trust Levels.", Toast.LENGTH_SHORT).show();
            niveis_confianca.setVisibility(View.INVISIBLE);
            ChooseTrust_Level.setVisibility(View.INVISIBLE);
        }else{
            niveis_confianca.setVisibility(View.VISIBLE);
            ChooseTrust_Level.setVisibility(View.VISIBLE);
        }


        confirmarParametros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (quantidade_APPS == 1){
                   if (number_OSB == 0) {
                       AlertDialog alert;
                       AlertDialog.Builder builder = new AlertDialog.Builder(definicao_parametros_avaliacao.this);
                       builder.setTitle("Error");
                       builder.setMessage("Choose the number of observations.");
                       builder.setPositiveButton("Ok", null);
                       alert = builder.create();
                       alert.show();
                   }else{
                       Intent it = new Intent(definicao_parametros_avaliacao.this, PowerTop.class);
                       //Toast.makeText(escolha_apps.this, "Pressionou o botão!", Toast.LENGTH_LONG).show();


                       it.putExtra("QUANTIDADE_APPS", quantidade_APPS);
                       it.putExtra("VALOR", itens);
                       it.putExtra("VALOR2", pacotes);
                       it.putExtra("OBSERVACAO", quantidadeObservacoesSelecionada);
                       it.putExtra("TEMPO", tempo_escolhido);
                       it.putExtra("CONDICAO_SALVAR", condicaoSalvar);
                       finish();
                       startActivity(it);
                   }

               }else {
                   if (number_OSB == 0) {
                       AlertDialog alert;
                       AlertDialog.Builder builder = new AlertDialog.Builder(definicao_parametros_avaliacao.this);
                       builder.setTitle("Error");
                       builder.setMessage("Choose the number of observations.");
                       builder.setPositiveButton("Ok", null);
                       alert = builder.create();
                       alert.show();
                   } else if (check_TrustLevel == 0) {
                       AlertDialog alert;
                       AlertDialog.Builder builder = new AlertDialog.Builder(definicao_parametros_avaliacao.this);
                       builder.setTitle("Error");
                       builder.setMessage("Choose the Trust Level.");
                       builder.setPositiveButton("Ok", null);
                       alert = builder.create();
                       alert.show();
                   } else {
                       if (quantidade_APPS == 1) {

                           Intent it = new Intent(definicao_parametros_avaliacao.this, PowerTop.class);
                           //Toast.makeText(escolha_apps.this, "Pressionou o botão!", Toast.LENGTH_LONG).show();


                           it.putExtra("QUANTIDADE_APPS", quantidade_APPS);
                           it.putExtra("VALOR", itens);
                           it.putExtra("VALOR2", pacotes);
                           it.putExtra("OBSERVACAO", quantidadeObservacoesSelecionada);
                           it.putExtra("TEMPO", tempo_escolhido);
                           it.putExtra("CONDICAO_SALVAR", condicaoSalvar);
                           finish();
                           startActivity(it);


                       } else if ((quantidade_APPS == 2) && (quantidadeObservacoesSelecionada < 2)) {
                           AlertDialog alert;
                           AlertDialog.Builder builder = new AlertDialog.Builder(definicao_parametros_avaliacao.this);
                           builder.setTitle("Erro");
                           builder.setMessage("For evaluations with 2 (two) applications it is necessary to choose at least 2 (duas) observations.");
                           builder.setPositiveButton("Ok", null);
                           alert = builder.create();
                           alert.show();
                       } else if ((quantidade_APPS == 2) && (quantidadeObservacoesSelecionada >= 2)) {

                           Intent it = new Intent(definicao_parametros_avaliacao.this, PowerTop.class);
                           //Toast.makeText(escolha_apps.this, "Pressionou o botão!", Toast.LENGTH_LONG).show();


                           it.putExtra("QUANTIDADE_APPS", quantidade_APPS);
                           it.putExtra("VALOR", itens);
                           it.putExtra("VALOR2", pacotes);
                           it.putExtra("OBSERVACAO", quantidadeObservacoesSelecionada);
                           it.putExtra("TEMPO", tempo_escolhido);
                           it.putExtra("CONDICAO_SALVAR", condicaoSalvar);
                           it.putExtra("NIVEL_CONFIANCA", nivelConfianca);
                           finish();
                           startActivity(it);


                       }
                   }

               }
            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(0, 0, 0, "Advanced Panel");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent it = new Intent (definicao_parametros_avaliacao.this, advanced_Parameters.class);
                //INSERIR PASSAGEM DE PARÂMTEROS
                startActivity(it);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
