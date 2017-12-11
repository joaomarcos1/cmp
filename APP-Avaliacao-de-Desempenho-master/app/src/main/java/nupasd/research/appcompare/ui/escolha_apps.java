package nupasd.research.appcompare.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nupasd.research.appcompare.*;
/*
public class escolha_apps extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_apps);
    }
}*/


public class escolha_apps extends Activity{

    int MENU1 = 0, MENU2 = 1, MENU3 = 2;

    public static final CharSequence[] TEMPOS = { "Testes","1 MINUTO", "2 MINUTOS", "3 MINUTOS", "5 MINUTOS", "8 MINUTOS", "15 MINUTOS"};

    public static final CharSequence[] INTERVALOS = {"1", "2", "3", "5", "7", "8", "10", "15", "30", "40", "50", "60", "80", "90", "100"};


    int tempo_valores[] = {10000, 60000, 120000, 180000, 300000, 480000, 900000};
    int obsevacoes_valores[] = {1, 2, 3, 5, 7, 8, 10, 15, 30, 40, 50, 60, 80, 90, 100};

    int tempo_escolhido;
    int observacao_escolhida;

    private ListView lista_aplicativos;
    private Button voltar;
    private Button confirmar_apps;
    private CheckBox salvarDados;
    EditText pesquisa_APPS;

    int contador_apps = 0;
    ArrayAdapter<String> adapter;
    ArrayList<String> opcoes3 = new ArrayList<String>();
    final int[] cont = {0};


    ArrayList<String> aux = new ArrayList<String>();
    ArrayList<String> repeticoes = new ArrayList<String>();
    int recebeRepeticoes = 0;


    int counterRepetidos = 0;


    String recebeRepetido;

    String itemBuscado;

    /*int count = 0;
    String NOME = null;
    ApplicationInfo APP;
    CharSequence NOME1;
    String RECEBE;*/


    //boolean condicaoSalvar = false;


    final ArrayList<String> opcoes = new ArrayList<String>();
    final ArrayList<String> salvaPacotes = new ArrayList<String>();


    //variável para verifiar se os aplicativos de Sistema devem ser exibidos ou não
    boolean mostrar_APPS_Sistema = false;
    CheckBox checkBoxCkicked;



    Object obj1;
    String receberVerificacaoItemSelecionado;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_apps);
        //addPreferencesFromResource(R.xml.);

        confirmar_apps = (Button) findViewById(R.id.btn_confirmar);
        voltar = (Button) findViewById(R.id.btn_voltar_logs_apps);




        final PackageManager packageManager = getPackageManager();
        //LinearLayout linear = (LinearLayout) findViewById(android.R.layout.linear);
        final List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);


            for (ApplicationInfo ap : list) {
                String nome = ap.packageName;
                try {
                    ApplicationInfo app = getPackageManager().getApplicationInfo(nome, 0);
                    CharSequence nome1 = getPackageManager().getApplicationLabel(app);
                    if ((ap.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        if (!nome1.toString().contains("com.")) {
                            opcoes.add((String) nome1);
                            salvaPacotes.add(nome);
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }


        pesquisa_APPS = (EditText) findViewById(R.id.edt_busca_APPS);


        //ApplicationInfo app = getPackageManager().getApplicationInfo(itens.get(0), 0);
        //CharSequence nome1 = getPackageManager().getApplicationLabel(app);


        //alterar para multipla escolha


        String opcoes2[] = new  String[opcoes.size()];

        for (int i = 0; i < opcoes.size(); i++){
            opcoes2[i] = opcoes.get(i);
        }

        Arrays.sort(opcoes2);

        for (int i = 0; i < opcoes2.length; i++){
            opcoes3.add(opcoes2[i]);
        }


        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, opcoes3);

        lista_aplicativos = (ListView) findViewById(R.id.listView_Aplicativos_Instalados);

        lista_aplicativos.setAdapter(adapter);

        final ArrayList<String> itens = new ArrayList<String>();

        final ArrayList<String> pacotes = new ArrayList<String>();

        lista_aplicativos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //if (view.isPressed()) {
                //parent.removeView(view);


                        if (lista_aplicativos.isItemChecked(position)) {
                            //Toast.makeText(escolha_apps.this, "O item esta marcado", Toast.LENGTH_SHORT).show();


                            contador_apps++;
                            cont[0]++;


                            Object obj = parent.getItemAtPosition(position);
                            String recebeNome = (String) obj;

                            //itens.add (opcoes3.get(position));
                            itens.add(recebeNome);

                            int i = 0;
                            for (ApplicationInfo ap : list) {

                                String nome = ap.packageName;
                                try {
                                    ApplicationInfo app = getPackageManager().getApplicationInfo(nome, 0);
                                    CharSequence nome1 = getPackageManager().getApplicationLabel(app);
                                    String aux = (String) nome1;

                                    //if (opcoes3.get(position).equalsIgnoreCase(aux)){
                                    if (recebeNome.equalsIgnoreCase(aux)) {
                                        pacotes.add(nome);
                                        //Toast.makeText(escolha_apps.this, "ENCONTRADO! PACOTE: "+pacotes.get(position), Toast.LENGTH_SHORT).show();
                                    }


                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }
                                i++;

                            }

                            //pacotes.add(salvaPacotes.get(position));


                            if (cont[0] > 2) {
                                AlertDialog alert;
                                AlertDialog.Builder builder = new AlertDialog.Builder(escolha_apps.this);
                                builder.setTitle("Error");
                                builder.setMessage("Please, choose on maximum 02 (two) applications.");
                                builder.setPositiveButton("Ok", null);
                                alert = builder.create();
                                alert.show();
                                lista_aplicativos.setItemChecked(position, false);
                                cont[0]--;
                                contador_apps--;
                                itens.remove(itens.size()-1);
                                pacotes.remove(pacotes.size()-1);
                            }else{//Caso a quantidade de elementos selecionados seja 02
                                Toast.makeText(escolha_apps.this, "You chose: " + recebeNome, Toast.LENGTH_SHORT).show();
                            }




                        }else{
                            cont[0]--;
                            contador_apps--;
                            itens.remove(itens.size()-1);
                            pacotes.remove(pacotes.size()-1);
                        }


                    }


        });





        pesquisa_APPS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Toast.makeText(escolha_apps.this, "Sua pesquisa: "+charSequence+" / i: "+i+" / i1: "+i1+" / i2: "+i2, Toast.LENGTH_SHORT).show();
                //pesquisa_Aplicacao((String) charSequence, itens, pacotes);
                //adapter.clear();
                //lista_aplicativos.setAdapter(null);
                //lista_aplicativos.getAdapter().notifyAll();
                /*for (int a = 0; a < opcoes3.size(); a++){
                    if (pesquisa_APPS.getText().toString().equalsIgnoreCase(opcoes3.get(a))){
                        ArrayList<String> aux = new ArrayList<String>();
                        aux.add(itens.get(a));

                        ArrayAdapter <String> adapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_multiple_choice, aux);

                        lista_aplicativos.setAdapter(adapter1);
                        lista_aplicativos.notify();
                        lista_aplicativos.notifyAll();
                    }
                }*/





                //Toast.makeText(escolha_apps.this, "Pesquisa: "+ charSequence, Toast.LENGTH_SHORT).show();
                //adapter.clear();
                //lista_aplicativos.setAdapter(null);
               // aux.clear();







            }


            @Override
            public void afterTextChanged(Editable editable) {

                itemBuscado = pesquisa_APPS.getText().toString();
                lista_aplicativos.setAdapter(adapter);
                aux.clear();
                repeticoes.clear();

                if (pesquisa_APPS.getText().toString().equalsIgnoreCase("")) {
                    lista_aplicativos.setAdapter(adapter);
                    aux.clear();
                    repeticoes.clear();


                    //Correção de selecão de item após pesquisa
                    for (int i = 0; i < itens.size(); i++){

                        for (int a = 0; a < opcoes3.size(); a++){

                            obj1 = lista_aplicativos.getItemAtPosition(a);
                            receberVerificacaoItemSelecionado = (String) obj1;

                            if (itens.get(i) == receberVerificacaoItemSelecionado){
                                lista_aplicativos.setItemChecked(a, true);
                            }


                        }


                    }



                } else {
                    for (int a = 0; a < opcoes3.size(); a++) {

                        //if (opcoes3.get(a).toLowerCase().contains(pesquisa_APPS.getText().toString().toLowerCase())) {
                        if (opcoes3.get(a).toLowerCase().contains(itemBuscado.toLowerCase())){
                            //ListView list = new ListView(getApplicationContext());


                            //for (int z = 0; z < opcoes3.size(); z++) {
                            /*for (int j = 0; j < aux.size(); j++) {
                                if (opcoes3.get(a).toLowerCase().equalsIgnoreCase(aux.get(j).toLowerCase())) {
                                    counterRepetidos++;
                                }
                            }
                            //}

                            if (counterRepetidos == 0) {*/
                            aux.add(opcoes3.get(a));
                            //aux.clear();
                            //}

                            //aux.add(opcoes3.get(a));

                            //ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, aux);



                            //MÉTODO PARA BUSCA DE APLICATIVOS



                            //Toast.makeText(escolha_apps.this, "Entrou na condição de alteração do MENU", Toast.LENGTH_SHORT).show();


                            /*
                            for(ApplicationInfo ap:list) {

                                NOME = ap.packageName;
                                try {
                                    APP = getPackageManager().getApplicationInfo(NOME, 0);
                                    NOME1 = getPackageManager().getApplicationLabel(APP);
                                    RECEBE = (String) NOME1;

                                    if (pesquisa_APPS.getText().toString().toLowerCase().equalsIgnoreCase(RECEBE.toString().toLowerCase())) {
                                        itens.add(NOME1.toString());
                                        pacotes.add(NOME);
                                        //Toast.makeText(escolha_apps.this, "ENCONTRADO! PACOTE: "+pacotes.get(position), Toast.LENGTH_SHORT).show();
                                    }


                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }
                                count++;
                            }*/


                        }

                    }
                    busca_apps();
                }


            }
        });

        /*
        pesquisa_APPS.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == EditorInfo.IME_ACTION_SEARCH) ||( i == EditorInfo.IME_ACTION_DONE) || ( i == KeyEvent.ACTION_DOWN)){

                    String APP_Pesquisado = pesquisa_APPS.getText().toString();
                    if (APP_Pesquisado != null || !APP_Pesquisado.equalsIgnoreCase("")){
                        Toast.makeText(escolha_apps.this, "Fazendo Pesquisa por APP!", Toast.LENGTH_SHORT).show();
                        pesquisa_Aplicacao(APP_Pesquisado, itens, pacotes);
                    }else{
                        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, opcoes);
                        lista_aplicativos.setAdapter(adapter);
                    }
                    return true;
                }else{
                    return false;
                }
            }
        });*/

        confirmar_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cont[0] == 0){
                    AlertDialog alert;
                    AlertDialog.Builder builder = new AlertDialog.Builder(escolha_apps.this);
                    builder.setTitle("Warning");
                    builder.setMessage("Please, choose at least 01 (one) application.");
                    builder.setPositiveButton("Ok", null);
                    alert = builder.create();
                    alert.show();
                }else{
                    String aplicativo = null;
                    Intent it = new Intent(escolha_apps.this, definicao_parametros_avaliacao.class);
                    //Toast.makeText(escolha_apps.this, "Pressionou o botão!", Toast.LENGTH_LONG).show();

                    it.putExtra("QUANTIDADE_APPS", contador_apps);
                    it.putExtra("VALOR", itens);
                    it.putExtra("VALOR2", pacotes);
                    //it.putExtra("OBSERVACAO", observacao_escolhida);
                    //it.putExtra("TEMPO", tempo_escolhido);
                    //it.putExtra("CONDICAO_SALVAR", condicaoSalvar);
                    finish();
                    startActivity(it);
                }
            }

        });

            /*
        lista_aplicativos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String aplicativo = null;
                Intent it = new Intent(escolha_apps.this, PowerTop.class);

                it.putExtra("VALOR", opcoes.get(position));
                startActivity(it);

            }
        });*/


        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                Intent it = new Intent(view.getContext(), UMLogger.class);
                startActivity(it);

            }
        });


    }


    public void busca_apps(){
        for (int t = 0; t < aux.size(); t++){
            recebeRepetido = aux.get(t);
            recebeRepeticoes = 0;
            for (int p = 0; p < aux.size(); p++){
                if (aux.get(p).equals(recebeRepetido)){
                    if (recebeRepeticoes == 0) {
                        recebeRepeticoes++;
                    }else{
                        aux.remove(p);
                    }
                }

                                    /*if (aux.get(t).equals(aux.get(p))){
                                        aux.remove(aux.get(p));

                                    }*/
            }
        }


        lista_aplicativos.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, aux));


    }

    public void pesquisa_Aplicacao(CharSequence nomeAPP, ArrayList<String> itens, ArrayList<String> pacotes){
        Toast.makeText(escolha_apps.this, "Entro no método de pesquisa!", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < itens.size(); i++){
            if (nomeAPP == itens.get(i)){
                lista_aplicativos.setAdapter(null);
                adapter.clear();
                lista_aplicativos.getAdapter().notifyAll();


                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, Integer.parseInt((String) nomeAPP));

                lista_aplicativos.setAdapter(adapter);

                lista_aplicativos.notifyAll();

            }
        }
    }


    public void atualiza_Lista_APPS_System (){
        final PackageManager packageManager = getPackageManager();
        //LinearLayout linear = (LinearLayout) findViewById(android.R.layout.linear);
        final List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);


        //limpando os arraylists anteriores para inserir a nova lista de aplicações
        opcoes.clear();
        salvaPacotes.clear();


        for(ApplicationInfo ap:list){
            String nome = ap.packageName;
            try {
                ApplicationInfo app = getPackageManager().getApplicationInfo(nome, 0);
                CharSequence nome1 = getPackageManager().getApplicationLabel(app);
                //if ((ap.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    if (!nome1.toString().contains("com.")) {
                        opcoes.add((String) nome1);
                        salvaPacotes.add(nome);
                    }
                //}
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }




        String opcoes2[] = new  String[opcoes.size()];

        for (int i = 0; i < opcoes.size(); i++){
            opcoes2[i] = opcoes.get(i);
        }

        Arrays.sort(opcoes2);

        for (int i = 0; i < opcoes2.length; i++){
            opcoes3.add(opcoes2[i]);
        }


        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, opcoes3);

        lista_aplicativos = (ListView) findViewById(R.id.listView_Aplicativos_Instalados);

        lista_aplicativos.setAdapter(adapter);



    }

/*
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU1, 0, "Definir Execução");
        //menu.add(0, MENU2, 0, "Preferências Adicionais");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 0:
                //Toast.makeText(escolha_apps.this, "Selecionado Opção 1!", Toast.LENGTH_LONG).show();
                showDialog(0);
                return true;
            case 1:
                //Toast.makeText(escolha_apps.this, "Selecionado Opção 2", Toast.LENGTH_LONG).show();
                //showDialog(2);
                return true;

            case 2:
                Toast.makeText(escolha_apps.this, "Selecionado Opção 3", Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
            /*case 0:
                builder.setTitle("Tempo Total de Execução para Cada APP - Testes nova alteração");
                //builder.setItems(TEMPOS,
                builder.setItems(TEMPOS, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        tempo_escolhido = tempo_valores[item];
                        Toast.makeText(escolha_apps.this, "Tempo escolhido: "+tempo_escolhido, Toast.LENGTH_SHORT).show();
                        showDialog(01);

                    }
                });
                return builder.create();

            case 0:
                builder.setTitle("Quantidade de Observações");
                builder.setItems(INTERVALOS, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //prefs.edit().putInt("topWindowType", item).commit();
                        tempo_escolhido = obsevacoes_valores[item];
                        tempo_escolhido *= 1000;
                        tempo_escolhido += 400;
                        Toast.makeText(escolha_apps.this, "Tempo escolhido: "+tempo_escolhido, Toast.LENGTH_SHORT).show();
                        //observacao_escolhida = obsevacoes_valores[item];
                        observacao_escolhida = obsevacoes_valores[item];
                        Toast.makeText(escolha_apps.this, "Intervalo Escolhido: "+observacao_escolhida, Toast.LENGTH_SHORT).show();
                    }
                });
                return builder.create();
            case 2:
                //Intent it = new Intent (escolha_apps.this, preferencias_execucao_teste.class);
                //startActivity(it);
        }
        return null;
    }*/

    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(0, 0, 0, "System Applications");
        menu.add(0, 1, 0, "User Applications");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case 0:
                opcoes.clear();
                salvaPacotes.clear();
                opcoes3.clear();
                atualiza_Lista_APPS_System();
                return true;

            case 1:
            /*
                AlertDialog alert;
                AlertDialog.Builder builder = new AlertDialog.Builder(escolha_apps.this);
                builder.setTitle("NOTICE");
                builder.setMessage("Available in future versions.");
                builder.setPositiveButton("Ok", null);
                alert = builder.create();
                alert.show();
                return true;
                */





                    opcoes.clear();
                    salvaPacotes.clear();
                    opcoes3.clear();

                    final PackageManager packageManager = getPackageManager();
                    //LinearLayout linear = (LinearLayout) findViewById(android.R.layout.linear);
                    final List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);


                        for (ApplicationInfo ap : list) {
                            String nome = ap.packageName;
                            try {
                                ApplicationInfo app = getPackageManager().getApplicationInfo(nome, 0);
                                CharSequence nome1 = getPackageManager().getApplicationLabel(app);
                                if ((ap.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                                    if (!nome1.toString().contains("com.")) {
                                        opcoes.add((String) nome1);
                                        salvaPacotes.add(nome);
                                    }
                                }
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                        }





                    //ApplicationInfo app = getPackageManager().getApplicationInfo(itens.get(0), 0);
                    //CharSequence nome1 = getPackageManager().getApplicationLabel(app);


                    //alterar para multipla escolha


                    String opcoes2[] = new  String[opcoes.size()];

                    for (int i = 0; i < opcoes.size(); i++){
                        opcoes2[i] = opcoes.get(i);
                    }

                    Arrays.sort(opcoes2);

                    for (int i = 0; i < opcoes2.length; i++){
                        opcoes3.add(opcoes2[i]);
                    }


                    adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, opcoes3);

                    lista_aplicativos = (ListView) findViewById(R.id.listView_Aplicativos_Instalados);

                    lista_aplicativos.setAdapter(adapter);




                    mostrar_APPS_Sistema = false;
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
*/
