package nupasd.research.appcompare.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Image;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import nupasd.research.appcompare.R;

public class resultados_teste_01_app extends Activity {


    private Button voltar;

    CharSequence opcoesInformacoes[] = {"Consumo Energético", "Desvios Padrão", "Variancia", "Média", "Moda", "Mediana"};

    ArrayList<Double> somaAPP01 = new ArrayList<Double>();

    private TextView mediaAPP01;
    private TextView varianciaAPP01;
    private TextView desvioPadraoAPP01;

    String nome_APP;

    double variancia01 = 0;
    double desvioPadrao01 = 0;
    double mediaAplicativo01 = 0;

    private LineChart mChart;

    private TextView txt_Nome_APP;
    private TextView txt_Consumo_Total;

    //private Button Salvar_Grafico_Imagem;
    //private Button SalvarRelatorio;

    boolean condicao_Salvar;

    private BarChart barChart;

    String nomeImagem, nomePDF;

    double consumoTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_resultados_teste_01_app);


        mediaAPP01 = (TextView) findViewById(R.id.txt_media_teste_01_APP);
        varianciaAPP01 = (TextView) findViewById(R.id.variancia_teste_01_APP);
        desvioPadraoAPP01 = (TextView) findViewById(R.id.txt_Desvio_Padrao_Teste_01_APP);
        txt_Nome_APP = (TextView) findViewById(R.id.txt_Nome_APP_TESTE_01_APP);
        txt_Consumo_Total = (TextView) findViewById(R.id.txt_Consumo_Total_APP_Teste_01_APP);

        //Salvar_Grafico_Imagem = (Button) findViewById(R.id.btn_Salvamento_Grafico);
        //SalvarRelatorio = (Button) findViewById(R.id.btn_Salvamento_Relatorio);


        /*Salvar_Grafico_Imagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomeImagem = "grap"+ System.currentTimeMillis();
                if (barChart.saveToGallery(nomeImagem, 50)) {

                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();
            }
        });

        SalvarRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/


        Bundle bnd = getIntent().getExtras();
        if (bnd.containsKey("APLICATIVO01")) {
            somaAPP01 = (ArrayList<Double>) getIntent().getExtras().getSerializable("APLICATIVO01");
        }


        double diferencaAPP[] = new double[somaAPP01.size()];

        for (int i = 1; i < somaAPP01.size(); i++){
            diferencaAPP[i] = (somaAPP01.get(i) - somaAPP01.get(i - 1));
        }

        somaAPP01.clear();

        for (int i = 1; i < diferencaAPP.length; i++){
            somaAPP01.add(diferencaAPP[i]);
        }


        if (bnd.containsKey("NOME_APP")) {
            nome_APP = bnd.getString("NOME_APP");
        }

        if (bnd.containsKey("SAlVAR_LOG")){
            condicao_Salvar = bnd.getBoolean("SAlVAR_LOG");
        }

        if (condicao_Salvar == true){
            for (int i = 0; i < somaAPP01.size(); i++){
                FileWriter arq = null;

                try {
                    arq = new FileWriter("/storage/emulated/0/log_" + nome_APP + ".txt", true);
                    PrintWriter gravarArq = new PrintWriter(arq, true);
                    gravarArq.printf(Double.toString(somaAPP01.get(i)) + "J\n");

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(resultados_teste_01_app.this, "Error saving LOG DATA - Application: "+nome_APP, Toast.LENGTH_SHORT).show();
                }

            }
        }

        txt_Nome_APP.setText(nome_APP);


        voltar = (Button) findViewById(R.id.btn_voltar_teste_01_app);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity(1);

                AlertDialog alert;
                AlertDialog.Builder builder = new AlertDialog.Builder(resultados_teste_01_app.this);
                builder.setTitle("Warning");
                builder.setMessage("Really want to leave?");
                builder.setNegativeButton("NO", null);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        Intent it = new Intent (resultados_teste_01_app.this, UMLogger.class);
                        startActivity(it);
                    }
                });

                alert = builder.create();
                alert.show();

            }
        });

        double SomasAplicativo01 = 0;
        for (int i = 0; i < somaAPP01.size(); i++) {
            SomasAplicativo01 += somaAPP01.get(i);
        }

        try {
            variancia01 = getVariancia(somaAPP01, somaAPP01.size(), SomasAplicativo01);
            desvioPadrao01 = getDesvioPadrao(somaAPP01, somaAPP01.size(), SomasAplicativo01);
            mediaAplicativo01 = getMediaAritmetica(somaAPP01, somaAPP01.size());

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*NumberFormat formatt = NumberFormat.getInstance();
        formatt.setMinimumFractionDigits(3);
        formatt.setMaximumFractionDigits(6);
        formatt.setMaximumIntegerDigits(3);
        formatt.setRoundingMode(RoundingMode.HALF_UP);*/

        consumoTotal = 0;
        for (int i = 0; i < somaAPP01.size(); i++){
            consumoTotal += somaAPP01.get(i);
        }


        float auxMedia = (float) mediaAplicativo01;

        mediaAPP01.setText(Double.toString(auxMedia) + " J");
        varianciaAPP01.setText(Double.toString(variancia01) + " J");
        desvioPadraoAPP01.setText(Double.toString(desvioPadrao01) + " J");
        txt_Consumo_Total.setText(Double.toString(consumoTotal)+ " J");

        //GRÁFICO de BARRA - MÉTODO OnCreate


        barChart = (BarChart) findViewById(R.id.chart_teste_01_app);

        ArrayList<BarEntry> entrada = new ArrayList<BarEntry>();
        //String aux = null;
        for (int i = 0; i < somaAPP01.size(); i++) {
            //aux = Double.toString(somaAPP01.get(i));
            //entrada.add(new Entry(Float.parseFloat(aux), i));
            entrada.add(new BarEntry(somaAPP01.get(i).floatValue(), i));

        }

        //2º PAsso
        BarDataSet dataSet = new BarDataSet(entrada, "Application Consumption");

        ArrayList<String> legends = new ArrayList<String>();
        for (int i = 0; i < somaAPP01.size(); i++) {
            legends.add("obs" + (i+1));
        }

        BarData dados = new BarData(legends, dataSet);

        //3º Passo


        //dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setData(dados);
        barChart.setDescription("Energy Consumption (In Joules)");
        barChart.animateY(2000);
        barChart.invalidate();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }






/*
            case R.id.button_create2:

                break;*/

    //case R.id.bSair:
    //  finish();


    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 0, 0, "Save Graph");
        menu.add(0, 01, 0, "Save Report");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:

                nomeImagem = "grap"+ System.currentTimeMillis();
                if (barChart.saveToGallery(nomeImagem, 50)) {

                    Toast.makeText(resultados_teste_01_app.this, "Image saved in /storage/emulated/0/DCIM/ as " + nomeImagem + ".jpg", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "SAVING IMAGE FAILED!", Toast.LENGTH_SHORT)
                            .show();

                return true;



            case 1:
                Toast.makeText(resultados_teste_01_app.this, "Making Complete Report!", Toast.LENGTH_SHORT).show();


                nomeImagem = "grap"+ System.currentTimeMillis();
                if (barChart.saveToGallery(nomeImagem, 50)) {

                  //  Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",       Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();



                Document document = new Document();


                try {

                    //Toast.makeText(getApplicationContext(), "Pdf OK1!", Toast.LENGTH_SHORT).show();

                    //Nome e caminho do pdf

                    nomePDF = Long.toString(System.currentTimeMillis());

                    PdfWriter.getInstance(document,
                            new FileOutputStream("/storage/emulated/0/" + nomePDF + ".pdf"));
                    document.open();

                    Paragraph paragraph = new Paragraph();
                    Paragraph paragraph2 = new Paragraph();
                    paragraph.setAlignment(Element.TEXT_NODE);
                    paragraph.add(new Phrase("\t\t\tReport " + nome_APP+"\n"));
                    document.add(paragraph);

                    document.add(new Chunk("\n\nThis Report presents data of evaluation of Application "+nome_APP+" and presents some statistical informations about this.\n\n"));

                    //Toast.makeText(getApplicationContext(), "Pdf OK2!",   Toast.LENGTH_SHORT).show();


                    Image image1 = Image.getInstance("/storage/emulated/0/DCIM/"+nomeImagem+".jpg");

                    image1.setIndentationLeft(65);

                    image1.scaleAbsolute(400f, 200f);
                    document.add(image1);
                    ///storage/emulated/0/
                    //Toast.makeText(getApplicationContext(), "Pdf OK3!", Toast.LENGTH_SHORT).show();


                    /*String imageUrl = "http://jenkov.com/images/" +
                            "20081123-20081123-3E1W7902-small-portrait.jpg";
                    Toast.makeText(getApplicationContext(), "Pdf OK3!",
                            Toast.LENGTH_SHORT).show();

                    com.itextpdf.text.Image image22 = com.itextpdf.text.Image.getInstance(new URL(imageUrl));
                    document.add(image22);*/




                    //pega hora

                    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

                    Date data = new Date();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(data);
                    Date data_atual = cal.getTime();





                    String hora_atual = dateFormat_hora.format(data_atual);
                    String ConsumoTotal = Double.toString(consumoTotal);
                    String media =  Double.toString(mediaAplicativo01);;
                    String variancia = Double.toString(variancia01);
                    String desvioP = Double.toString(desvioPadrao01);
                    paragraph2.add(new Phrase("\n"+nome_APP+" data\n\n"));
                    document.add(paragraph2);

                    PdfPTable table1 = new PdfPTable(2); // 2 columns.

                    PdfPCell cell0 = new PdfPCell(new Paragraph("Total Consumption"));
                    PdfPCell cell00 = new PdfPCell(new Paragraph(ConsumoTotal + " J"));
                    PdfPCell cell1 = new PdfPCell(new Paragraph("Average"));
                    PdfPCell cell2 = new PdfPCell(new Paragraph(media + " J"));
                    PdfPCell cell3 = new PdfPCell(new Paragraph("Variation"));
                    PdfPCell cell4 = new PdfPCell(new Paragraph(variancia + " J"));
                    PdfPCell cell5 = new PdfPCell(new Paragraph("Standart Deviation"));
                    PdfPCell cell6 = new PdfPCell(new Paragraph(desvioP + " J"));

                    table1.addCell(cell0);
                    table1.addCell(cell00);
                    table1.addCell(cell1);
                    table1.addCell(cell2);
                    table1.addCell(cell3);
                    table1.addCell(cell4);
                    table1.addCell(cell5);
                    table1.addCell(cell6);

                    document.add(table1);

                    document.close();
                    Toast.makeText(resultados_teste_01_app.this, "Report saved in /storage/emulated/0/ as " + nomePDF + ".pdf", Toast.LENGTH_SHORT).show();


                    AlertDialog alert;
                    AlertDialog.Builder builder = new AlertDialog.Builder(resultados_teste_01_app.this);
                    builder.setTitle("");
                    builder.setMessage("Do you want to view the report?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            File file = new File ("/storage/emulated/0/" + nomePDF + ".pdf");
                            Intent it = new Intent (Intent.ACTION_VIEW);
                            it.setDataAndType(Uri.fromFile(file), "application/pdf");
                            it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(it);
                        }
                    });

                    builder.setNegativeButton("NO", null);
                    alert = builder.create();
                    alert.show();





                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(resultados_teste_01_app.this, "ERROR SAVING REPORT", Toast.LENGTH_SHORT).show();
                }
                //showDialog(2);
                //Colocar aqui o método de salvaros dados
                return true;

        }
        return false;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case 0:
                builder.setTitle("Escolha a Informação para ser exibida:");
                //builder.setItems(TEMPOS,
                builder.setItems(opcoesInformacoes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(resultados_teste_01_app.this, "Opção esoclhida: " + opcoesInformacoes[item], Toast.LENGTH_SHORT).show();
                        //CRIAR CONDIÇÕES PARA QUANDO CADA QUE QUANDO CADA UMA DAS OPÇOÕES SEJA SELECIONADA O GRÁFICO SERÁ REESCRITO

                    }
                });

                return builder.create();

        }
        return null;
    }


    //SOMA DOS ELEMENTOS AO QUADRADO
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


    //  A PARTIR DE AGORA , OGRÁFICOS DE LINHAS!!!


    private void setData() {
        ArrayList<String> xVals = setXAxisValues();

        ArrayList<Entry> yVals = setYAxisValues();

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "DataSet 1 - TESTE hahaha");

        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);

    }


    private ArrayList<String> setXAxisValues() {
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("10");
        xVals.add("20");
        xVals.add("30");
        xVals.add("30.5");
        xVals.add("40");

        return xVals;
    }

    private ArrayList<Entry> setYAxisValues() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(60, 0));
        yVals.add(new Entry(48, 1));
        yVals.add(new Entry(70.5f, 2));
        yVals.add(new Entry(100, 3));
        yVals.add(new Entry(180.9f, 4));

        return yVals;
    }

/*
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "resultados_teste_01_app Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://edu.umich.PowerTutor.ui/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "resultados_teste_01_app Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://edu.umich.PowerTutor.ui/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }*/
}
