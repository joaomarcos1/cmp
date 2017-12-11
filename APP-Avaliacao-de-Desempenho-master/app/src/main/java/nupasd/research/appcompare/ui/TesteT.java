package nupasd.research.appcompare.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.TTest;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import nupasd.research.appcompare.R;


public class TesteT extends Activity {


    private EditText edt_APP01, edt_APP02;
    private Button voltar;
    private Button graficos_resultados;
    private TextView txt_ConsumoTotal_APP01;
    private TextView txt_ConsumoTotal_APP02;

    ArrayList<Double> somaAPP01 = new ArrayList<Double>();
    ArrayList<Double> somaAPP02 = new ArrayList<Double>();


    //Variáveis - Estatística

    float mediaaA = 0, mediaaB = 0, somaA = 0, somaB = 0;
    int countA = 0, countB = 0;

    ArrayList<Float>  mediaA = new ArrayList<Float>();
    ArrayList<Float>  mediaB = new ArrayList<Float>();
    ArrayList<Float> valoresA = new ArrayList<Float>();
    ArrayList<Float> valoresB = new ArrayList<Float>();
    ArrayList<Float> varianciaA = new ArrayList<Float>();
    ArrayList<Float> varianciaB = new ArrayList<Float>();
    ArrayList<Float> varianciaAux = new ArrayList<Float>();
    double variancA = 0;
    double variancB = 0;
    double desvioPadrao01 = 0;
    double desvioPadrao02 = 0;
    double mediaAplicativo01 = 0;
    double mediaAplicativo02 = 0;




    private TextView mediaAPP01;
    private TextView varianciaAPP01;
    private TextView desvioPadraoAPP01;

    private TextView mediaAPP02;
    private TextView varianciaAPP02;
    private TextView desvioPadraoAPP02;

    private TextView resultado_TesteT;
    ArrayList<String> nomes_APPS = new ArrayList<String>();
    int Nivel_Confianca = 0;

    private TextView nomeAPP01;
    private TextView nomeAPP02;
    private TextView Titulo_TesteT_Nivel_de_Confianca;
    private TextView confiabilidade_estatistica;


    boolean condicao_Salvar;

    boolean testeT;

    double consumoTotal01 = 0, consumoTotal02 = 0;
    ArrayList<Double> recebeConsumosTotais = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_t);

        //edt_APP01 = (EditText) findViewById(R.id.edt_Valores_APP_01);

        //edt_APP02 = (EditText) findViewById(R.id.edt_Valores_APP_02);

        voltar = (Button) findViewById(R.id.btn_Voltar_Tela_TesteT);

        mediaAPP01 = (TextView) findViewById(R.id.txt_Media_APP01);
        mediaAPP02 = (TextView) findViewById(R.id.txt_Media_APP02);

        varianciaAPP01 = (TextView) findViewById(R.id.txt_Variancia_APP01);
        varianciaAPP02 = (TextView) findViewById(R.id.txt_Variancia_APP02);

        desvioPadraoAPP01 = (TextView) findViewById(R.id.txt_DesvioPadrao_APP01);
        desvioPadraoAPP02 = (TextView) findViewById(R.id.txt_DesvioPadrao_APP02);

        graficos_resultados = (Button) findViewById(R.id.btn_graficos_resultados);

        resultado_TesteT = (TextView) findViewById(R.id.txt_resultadoTesteT);

        nomeAPP01 = (TextView) findViewById(R.id.txt_Nome_APP_01);

        nomeAPP02 = (TextView) findViewById(R.id.txt_Nome_APP_02);

        txt_ConsumoTotal_APP01 = (TextView) findViewById(R.id.txt_Consumo_Total_Aplicativo_01_TESTE02);
        txt_ConsumoTotal_APP02 = (TextView) findViewById(R.id.txt_Consumo_Total_Aplicativo_02_TESTE02);

        Titulo_TesteT_Nivel_de_Confianca = (TextView) findViewById(R.id.txt_TesteT_Titulo_Nivel_Confianca);
        confiabilidade_estatistica = (TextView) findViewById(R.id.txt_Resultado_Confiabilidade);

        Bundle bnd = getIntent().getExtras();

        if (bnd.containsKey("app01")){
            //somaAPP01 = bnd.getL("app01");
            somaAPP01 = (ArrayList<Double>)getIntent().getExtras().getSerializable("app01");
        }

        if (bnd.containsKey("app02")){
            somaAPP02 = (ArrayList<Double>)getIntent().getExtras().getSerializable("app02");
        }

        if (bnd.containsKey("SALVA_LOG")){
            condicao_Salvar = bnd.getBoolean("SALVA_LOG");
        }

        //---------------------------------------------------------------------------------------
        //Declaração de Array Auxiliares para subtração de diferenças

        double diferenca_APP01[] = new double[somaAPP01.size()];
        double diferenca_APP02[] = new double[somaAPP02.size()];

        double diferenca = 0;

        for (int i = 1; i < somaAPP01.size(); i++){
            diferenca_APP01[i] = (somaAPP01.get(i) - somaAPP01.get(i - 1));
        }

        for (int i = 1; i < somaAPP02.size(); i++){
            diferenca_APP02[i] = (somaAPP02.get(i) - somaAPP02.get(i - 1));
        }

        somaAPP01.clear();
        somaAPP02.clear();

        for (int i = 1; i < diferenca_APP01.length; i++){
            somaAPP01.add(diferenca_APP01[i]);
            somaAPP02.add(diferenca_APP02[i]);
        }


        //---------------------------------------------------------------------------------------

        if (bnd.containsKey("NOMES_APPS")){
            nomes_APPS = bnd.getStringArrayList("NOMES_APPS");
        }

        if (bnd.containsKey("NIVEL_CONFIANCA")){
            Nivel_Confianca = bnd.getInt("NIVEL_CONFIANCA");
        }

        if (condicao_Salvar == true) {
            for (int i = 0; i < somaAPP01.size(); i++){
                FileWriter arq = null;

                try {
                    arq = new FileWriter("/storage/emulated/0/log_" + nomes_APPS.get(0) + ".txt", true);
                    PrintWriter gravarArq = new PrintWriter(arq, true);
                    gravarArq.printf(Double.toString(somaAPP01.get(i)) + "J\n");

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(TesteT.this, "Error saving LOG DATA - Application: "+nomes_APPS.get(0), Toast.LENGTH_SHORT).show();
                }

            }


            for (int i = 0; i < somaAPP02.size(); i++){
                FileWriter arq = null;

                try {
                    arq = new FileWriter("/storage/emulated/0/log_" + nomes_APPS.get(1) + ".txt", true);
                    PrintWriter gravarArq = new PrintWriter(arq, true);
                    gravarArq.printf(Double.toString(somaAPP02.get(i)) + "J\n");

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(TesteT.this, "Error saving LOG DATA - Application: "+nomes_APPS.get(1), Toast.LENGTH_SHORT).show();
                }

            }


        }







        nomeAPP01.setText(nomes_APPS.get(0));

        nomeAPP02.setText(nomes_APPS.get(1));

        StringBuilder stb1 = new StringBuilder();
        StringBuilder stb2 = new StringBuilder();


        double SomasAplicativo01 = 0;
        for (int i = 0; i < somaAPP01.size(); i++){
            stb1.append(Double.toString(somaAPP01.get(i))+"  ");
            SomasAplicativo01 += somaAPP01.get(i);
        }


        double SomasAplicativo02 = 0;
        for (int i = 0; i < somaAPP02.size(); i++){
            stb2.append(Double.toString(somaAPP02.get(i))+"  ");
            SomasAplicativo02 += somaAPP02.get(i);
        }


        try {
            variancA = getVariancia(somaAPP01, somaAPP01.size(), SomasAplicativo01);
            variancB = getVariancia(somaAPP02, somaAPP02.size(), SomasAplicativo02);
            desvioPadrao01 = getDesvioPadrao(somaAPP01, somaAPP01.size(), SomasAplicativo01);
            desvioPadrao02 = getDesvioPadrao(somaAPP02, somaAPP02.size(), SomasAplicativo02);
            mediaAplicativo01 = getMediaAritmetica(somaAPP01, somaAPP01.size());
            mediaAplicativo02 = getMediaAritmetica(somaAPP02, somaAPP02.size());


        } catch (IOException e) {
            e.printStackTrace();
        }



        //edt_APP01.setText(stb1.toString());

        //edt_APP02.setText(stb2.toString());


        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity(1);

                AlertDialog alert;
                AlertDialog.Builder builder = new AlertDialog.Builder(TesteT.this);
                builder.setTitle("Warning");
                builder.setMessage("Really want to leave?");
                builder.setNegativeButton("NO", null);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        Intent it = new Intent (TesteT.this, UMLogger.class);
                        startActivity(it);
                    }
                });

                alert = builder.create();
                alert.show();

            }
        });


        graficos_resultados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recebeConsumosTotais.add(consumoTotal01);
                recebeConsumosTotais.add(consumoTotal02);

                Intent it = new Intent (TesteT.this, graficos_resultados.class);
                it.putExtra("varianciaAPP01", variancA);
                it.putExtra("varianciaAPP02", variancB);
                it.putExtra("desvioPadraoAPP01", desvioPadrao01);
                it.putExtra("desvioPadraoAPP02", desvioPadrao02);
                it.putExtra("mediaAPP01", mediaAplicativo01);
                it.putExtra("mediaAPP02", mediaAplicativo02);
                it.putExtra("somasAPP01", somaAPP01);
                it.putExtra("somasAPP02", somaAPP02);
                it.putExtra("NOMES_APPS", nomes_APPS);
                it.putExtra("CONSUMOS_TOTAIS", recebeConsumosTotais);
                it.putExtra("TESTE_T", testeT);
                it.putExtra("NIVEL_CONFIANCA", Nivel_Confianca);
                startActivity(it);

            }
        });

        double vetorSomasAPP01[] = new double[somaAPP01.size()];
        double vetorSomasAPP02[] = new double[somaAPP02.size()];


        for (int i = 0; i < somaAPP01.size(); i++){
            vetorSomasAPP01[i] = somaAPP01.get(i);
            consumoTotal01 += somaAPP01.get(i);
        }

        for (int i = 0; i < somaAPP02.size(); i++){
            vetorSomasAPP02[i] = somaAPP02.get(i);
            consumoTotal02 += somaAPP02.get(i);
        }

        TTest testT = new TTest();
        NormalDistribution normalD = new NormalDistribution();



        //99% = 0.01
        //boolean resutadoTesteT = testT.homoscedasticTTest(vetorSomasAPP01, vetorSomasAPP02, 0.05);




        txt_ConsumoTotal_APP01.setText(Double.toString(consumoTotal01) + " J");
        txt_ConsumoTotal_APP02.setText(Double.toString(consumoTotal02) + " J");
        mediaAPP01.setText(Double.toString(mediaAplicativo01) + " J");
        mediaAPP02.setText(Double.toString(mediaAplicativo02) + " J");

        varianciaAPP01.setText(Double.toString(variancA) + " J");
        varianciaAPP02.setText(Double.toString(variancB) + " J");

        desvioPadraoAPP01.setText(Double.toString(desvioPadrao01) + " J");
        desvioPadraoAPP02.setText(Double.toString(desvioPadrao02) + " J");

        if ((somaAPP01.size() >= 2) && (somaAPP02.size() >= 2)) {
            if (Nivel_Confianca == 95) {
                testeT = testT.homoscedasticTTest(vetorSomasAPP01, vetorSomasAPP02, 0.05);
                Titulo_TesteT_Nivel_de_Confianca.setText("Test T-Student");
                confiabilidade_estatistica.setText("Statistical Reliability: 95 %");
                resultado_TesteT.setText("Result of Hypothesis 0: "+testeT);
            } else {
                testeT = testT.homoscedasticTTest(vetorSomasAPP01, vetorSomasAPP02, 0.01);
                Titulo_TesteT_Nivel_de_Confianca.setText("Test T-Student");
                confiabilidade_estatistica.setText("Statistical Reliability: 99 %");
                resultado_TesteT.setText("Result of Hypothesis 0: "+testeT);
            }

            //resultado_TesteT.setText(Boolean.toString(testeT));
        }else{
            AlertDialog alert;
            AlertDialog.Builder builder = new AlertDialog.Builder(TesteT.this);
            builder.setTitle("Error");
            builder.setMessage("Is not possible calculate Test T-Student. Minimun quantity was not alcanced.");
            builder.setPositiveButton("Ok", null);
            alert = builder.create();
            alert.show();
            Titulo_TesteT_Nivel_de_Confianca.setText("");
            confiabilidade_estatistica.setText("");
            resultado_TesteT.setText("");
        }










    /*
        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dados);
        graph.setTitle("Médias Obtidas");
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.addSeries(series);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(10);

// draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
//series.setValuesOnTopSize(50);*/



    }



    public double getSomaDosElementosAoQuadrado(ArrayList<Double> valoresl, int cont) {

        double total = 0;

        for (int counter = 0; counter < cont; counter++)

            total += Math.pow(valoresl.get(counter), 2);

        return total;

    }



    public double getMediaAritmetica(ArrayList<Double> valoresl, int cont) {

        double total = 0;

        for (int counter = 0; counter < cont; counter++)

            total += valoresl.get(counter);

        return total / cont;

    }

    // Desvio Padrão Amostral

    public double getDesvioPadrao(ArrayList<Double> valoresl, int cont, double soma) throws
            IOException {

        return Math.sqrt(getVariancia(valoresl, cont, soma));


    }
    //Variancia

    public double getVariancia(ArrayList<Double> valoresl, int cont, double soma) throws IOException {


        double p1 = 1 / (double) (cont - 1);

        double p2 = getSomaDosElementosAoQuadrado(valoresl, cont) - (Math.pow(soma, 2) / (double) cont);

        return (p1 * p2);

    }
}

