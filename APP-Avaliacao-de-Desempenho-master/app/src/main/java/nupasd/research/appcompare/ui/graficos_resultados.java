package nupasd.research.appcompare.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nupasd.*;
import nupasd.research.appcompare.R;

/*
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPojint;(*/

public class graficos_resultados extends Activity {

    private Button voltar;
    double varianciaAPP01, varianciaAPP02, desvioPadraoAPP01, desvioPadraoAPP02, mediaAPP01, mediaAPP02;
    ArrayList<Double> somasAPP01 = new ArrayList<Double>();
    ArrayList<Double> somasAPP02 = new ArrayList<Double>();

    CharSequence opcoesInformacoes[] = {"Consumo Energético", "Desvios Padrão", "Variancia", "Média", "Moda", "Mediana"};

    private Button SalvarRelatorio;
    private Button SalvarGraficos;

    CandleStickChart candleStickChart;
    BarChart barChart;
    BarChart barChar2;

    String nomeImagem01, nomeImagem02, nomeImagem03, nomePDF;

    ArrayList<String> nomesAPPS = new ArrayList<String>();



    //DATABASE


    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<Message> messagesList = new ArrayList<Message>();
    private ListView main_listview;
    private MainAdapter mainAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String test_string;
    private String username;
    private graficos_resultados mContext;
    private TextView textView_is_typing;

    String app1, app2;
    private Button button_send;

    ArrayList<Double> ConsumosTotais = new ArrayList<Double>();
    boolean TesteT;

    int NivelConfianca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos_resultados);


        /*mContext = graficos_resultados.this;


        mFirebaseAnalytics.setUserProperty("user_type", "author");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //button_send = (Button) findViewById(R.id.button_send);
        //editText_message = (EditText) findViewById(R.id.editText_message);
        //textView_is_typing = (TextView) findViewById(R.id.textView_is_typing);
        //main_listview = (ListView) findViewById(R.id.main_listview);
        username = getSharedPreferences("PREFS", 0).getString("username", "Anonymous");
        //textView_is_typing.setVisibility(View.INVISIBLE);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        mainAdapter = new MainAdapter(mContext, messagesList);
        //main_listview.setAdapter(mainAdapter);

        test_string = null;

       /* button_send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                databaseReference.child("room-typing").child("irc").child(username).setValue(false);


                process_message();
                //editText_message.setText("Mensagem Fixa");

                Toast.makeText(graficos_resultados.this, "Dados salvos na Nuvem!", Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference.child("users").child(MyUtils.generateUniqueUserId(mContext)).addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                username = dataSnapshot.getValue(String.class);
                if (username == null) {
                    username = "Anonymous";
                }
            }

            @Override public void onCancelled(DatabaseError databaseError) {
            }
        });

        //------------------------------------------------------------
        databaseReference.child("db_messages").limitToLast(20).addChildEventListener(new ChildEventListener() {
            @Override public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                messagesList.add(message);
                mainAdapter.notifyDataSetChanged();
                Log.d("message", message.toString());
            }

            @Override public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Log.d("onChildChanged", dataSnapshot.toString());
            }

            @Override public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Log.d("onChildRemoved", dataSnapshot.toString());
            }

            @Override public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Log.d("onChildMoved", dataSnapshot.toString());
            }

            @Override public void onCancelled(DatabaseError databaseError) {
                Log.d("onCancelled", databaseError.toString());
            }
        });
*/
//--------------------------------------------------------------------------------

        voltar = (Button) findViewById(R.id.btn_voltar_tela_graficos);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        SalvarGraficos = (Button) findViewById(R.id.btn_Salvar_Graficos_TesteT);

        SalvarGraficos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomeImagem01 = "grap1"+ System.currentTimeMillis();
                nomeImagem02 = "grap2"+ System.currentTimeMillis();
                nomeImagem03 = "grap3" + System.currentTimeMillis();
                if (barChart.saveToGallery(nomeImagem01, 150) && candleStickChart.saveToGallery(nomeImagem02, 150) && barChar2.saveToGallery(nomeImagem03, 150)) {

                    Toast.makeText(graficos_resultados.this, "Images saved in /storage/emulated/0/DCIM/ as " + nomeImagem01 + ".jpg, "+nomeImagem02+".jpg, "+nomeImagem03+".jpg", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();
            }
        });

        SalvarRelatorio = (Button) findViewById(R.id.btn_Salvar_Relatorios__TesteT);


        SalvarRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(graficos_resultados.this, "Making Complete Report!", Toast.LENGTH_SHORT).show();


                nomeImagem01 = "grap1" + System.currentTimeMillis();
                nomeImagem02 = "grap2" + System.currentTimeMillis();
                nomeImagem03 = "grap3" + System.currentTimeMillis();
                if (barChart.saveToGallery(nomeImagem01, 150) && candleStickChart.saveToGallery(nomeImagem02, 150) && barChar2.saveToGallery(nomeImagem03, 150)) {

                    //Toast.makeText(getApplicationContext(), "Saving Graphs",Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Error Saving Graphs!", Toast.LENGTH_SHORT).show();


                Document document = new Document();


                try {

                    //Toast.makeText(getApplicationContext(), "Pdf OK1!",
                    //        Toast.LENGTH_SHORT).show();

                    nomePDF = Long.toString(System.currentTimeMillis());
                    PdfWriter.getInstance(document,
                            new FileOutputStream("/storage/emulated/0/" + nomePDF + ".pdf"));
                    document.open();
                    //Toast.makeText(getApplicationContext(), "Pdf OK 1.1!",Toast.LENGTH_SHORT).show();
                    Paragraph paragraph = new Paragraph();
                    Paragraph paragraph2 = new Paragraph();
                    Paragraph paragraph3 = new Paragraph();
                    paragraph.setAlignment(Element.TEXT_NODE);
                    paragraph.add(new Phrase("Report "+ nomesAPPS.get(0)+ " and "+nomesAPPS.get(1) + "\n\n"));
                    document.add(paragraph);


                    document.add(new Chunk("This Report presents data of evaluation of Applications "+nomesAPPS.get(0)+" and "+nomesAPPS.get(1)+", " +
                            "presenting some statistical informations about this.\n\n"));

                    //Toast.makeText(getApplicationContext(), "Pdf OK2!",
                    //       Toast.LENGTH_SHORT).show();

                    Image image2 = Image.getInstance("/storage/emulated/0/DCIM/" + nomeImagem02 + ".jpg");

                    image2.setIndentationLeft(65);

                    image2.scaleAbsolute(400f, 200f);

                    document.add(image2);

                    ///storage/emulated/0/
                    //Toast.makeText(getApplicationContext(), "Pdf OK3!",
                    //       Toast.LENGTH_SHORT).show();


                    /*String imageUrl = "http://jenkov.com/images/" +
                            "20081123-20081123-3E1W7902-small-portrait.jpg";
                    Toast.makeText(getApplicationContext(), "Pdf OK3!",
                            Toast.LENGTH_SHORT).show();

                    com.itextpdf.text.Image image22 = com.itextpdf.text.Image.getInstance(new URL(imageUrl));
                    document.add(image22);*/


                    //document.add(new Chunk("Este Relatório apresenta os dados da avaliação do Aplicativo " +
                    //      "e apresenta algumas informações estatíticas sobre o mesmo."));


                    //pega hora

                    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

                    Date data = new Date();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(data);
                    Date data_atual = cal.getTime();


                    String media1 = Double.toString(mediaAPP01);
                    //String consumoTotal01 = Double.toString())
                    String consumo01 = Double.toString(ConsumosTotais.get(0));
                    String variancia1 = Double.toString(varianciaAPP01);
                    String desvioP1 = Double.toString(desvioPadraoAPP01);


                    paragraph2.add(new Phrase("\n\n\nInformations " + nomesAPPS.get(0)+" \n\n"));
                    document.add(paragraph2);
                    Image image1 = Image.getInstance("/storage/emulated/0/DCIM/" + nomeImagem01 + ".jpg");
                    image1.setIndentationLeft(65);
                    image1.scaleAbsolute(400f, 200f);
                    document.add(image1);

                    document.add(new Chunk("\n\n"));

                    PdfPTable table1 = new PdfPTable(2); // 2 columns.

                    PdfPCell cell0 = new PdfPCell(new Paragraph("Energy Consumption"));
                    PdfPCell cell00 = new PdfPCell(new Paragraph(consumo01 + " J"));
                    PdfPCell cell1 = new PdfPCell(new Paragraph("Average"));
                    PdfPCell cell2 = new PdfPCell(new Paragraph(media1 + " J"));
                    PdfPCell cell3 = new PdfPCell(new Paragraph("Variation"));
                    PdfPCell cell4 = new PdfPCell(new Paragraph(variancia1 + " J"));
                    PdfPCell cell5 = new PdfPCell(new Paragraph("Standart Deviation"));
                    PdfPCell cell6 = new PdfPCell(new Paragraph(desvioP1 + " J"));

                    table1.addCell(cell0);
                    table1.addCell(cell00);
                    table1.addCell(cell1);
                    table1.addCell(cell2);
                    table1.addCell(cell3);
                    table1.addCell(cell4);
                    table1.addCell(cell5);
                    table1.addCell(cell6);

                    document.add(table1);


                    document.add(new Chunk("\n\n\n\n"));


                    String media2 = Double.toString(mediaAPP02);
                    ;
                    String variancia2 = Double.toString(varianciaAPP02);
                    String desvioP2 = Double.toString(desvioPadraoAPP02);
                    String consumo02 = Double.toString(ConsumosTotais.get(1));

                    paragraph3.add(new Phrase("\n\nInformations " + nomesAPPS.get(1)+ "\n\n"));
                    document.add(paragraph3);

                    Image image3 = Image.getInstance("/storage/emulated/0/DCIM/" + nomeImagem03 + ".jpg");
                    image3.setIndentationLeft(65);
                    image3.scaleAbsolute(400f, 200f);
                    document.add(image3);


                    document.add(new Chunk("\n\n"));



                    PdfPTable table2 = new PdfPTable(2); // 2 columns.

                    PdfPCell cell20 = new PdfPCell(new Paragraph("Energy Consumption"));
                    PdfPCell cell21 = new PdfPCell(new Paragraph(consumo02 + " J"));
                    PdfPCell cell7 = new PdfPCell(new Paragraph("Average"));
                    PdfPCell cell8 = new PdfPCell(new Paragraph(media2 + " J"));
                    PdfPCell cell9 = new PdfPCell(new Paragraph("Variation"));
                    PdfPCell cell10 = new PdfPCell(new Paragraph(variancia2 + " J"));
                    PdfPCell cell11 = new PdfPCell(new Paragraph("Standart Deviation"));
                    PdfPCell cell12 = new PdfPCell(new Paragraph(desvioP2 + " J"));

                    table2.addCell(cell20);
                    table2.addCell(cell21);
                    table2.addCell(cell7);
                    table2.addCell(cell8);
                    table2.addCell(cell9);
                    table2.addCell(cell10);
                    table2.addCell(cell11);
                    table2.addCell(cell12);

                    document.add(table2);


                    document.add(new Chunk("\n\n\nComparative\n\n"));
                    document.add(new Chunk("TTest's Trust Level: "+ NivelConfianca+"%\n"));
                    document.add(new Chunk("Result of TTest: "+TesteT+"\n"));
                    if (TesteT == true){
                        document.add(new Chunk("Hypothesis 0 CONFIRMED, the applications evaluated are equivalent."));
                    }else{
                        document.add(new Chunk("Hypothesis 0 NOT CONFIRMED, the applications evaluated are not equivalent."));
                    }




                    document.close();
                    //Toast.makeText(graficos_resultados.this, "Report generated Sucessfully!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(graficos_resultados.this, "Report saved in /storage/emulated/0/ as " + nomePDF + ".pdf", Toast.LENGTH_SHORT).show();

                    AlertDialog alert;
                    AlertDialog.Builder builder = new AlertDialog.Builder(graficos_resultados.this);
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
                    Toast.makeText(graficos_resultados.this, "Error generating Report: "+ e, Toast.LENGTH_SHORT).show();
                }
            }
        });



        Bundle bnd = getIntent().getExtras();

        if (bnd.containsKey("NIVEL_CONFIANCA")){
            NivelConfianca = bnd.getInt("NIVEL_CONFIANCA");
        }

        if (bnd.containsKey("TESTE_T")){
            TesteT = bnd.getBoolean("TESTE_T");
        }

        if (bnd.containsKey("CONSUMOS_TOTAIS")){
            ConsumosTotais = (ArrayList<Double>) getIntent().getExtras().getSerializable("CONSUMOS_TOTAIS");
        }

        if(bnd.containsKey("NOMES_APPS")){
            nomesAPPS = bnd.getStringArrayList("NOMES_APPS");
        }

        if (bnd.containsKey("varianciaAPP01")) {
            varianciaAPP01 = bnd.getDouble("varianciaAPP01");
        }

        if (bnd.containsKey("varianciaAPP02")) {
            varianciaAPP02 = bnd.getDouble("varianciaAPP02");
        }

        if (bnd.containsKey("desvioPadraoAPP01")) {
            desvioPadraoAPP01 = bnd.getDouble("desvioPadraoAPP01");
        }

        if (bnd.containsKey("desvioPadraoAPP02")) {
            desvioPadraoAPP02 = bnd.getDouble("desvioPadraoAPP02");
        }

        if (bnd.containsKey("mediaAPP01")) {
            mediaAPP01 = bnd.getDouble("mediaAPP01");
        }

        if (bnd.containsKey("mediaAPP02")) {
            mediaAPP02 = bnd.getDouble("mediaAPP02");
        }

        if (bnd.containsKey("somasAPP01")) {
            somasAPP01 = (ArrayList<Double>) getIntent().getExtras().getSerializable("somasAPP01");
        }

        if (bnd.containsKey("somasAPP02")) {
            somasAPP02 = (ArrayList<Double>) getIntent().getExtras().getSerializable("somasAPP02");
        }


        candleStickChart = (CandleStickChart) findViewById(R.id.chart);


        ArrayList<CandleEntry> entries = new ArrayList<CandleEntry>();
        entries.add(new CandleEntry(0, (float) varianciaAPP01, (float) (varianciaAPP01 - (0.90 * varianciaAPP01)), (float) (varianciaAPP01 - (varianciaAPP01 * 0.70)), (float) (varianciaAPP01 - (varianciaAPP01 * 0.30))));
        entries.add(new CandleEntry(1, (float) varianciaAPP02, (float) (varianciaAPP02 - (0.90 * varianciaAPP02)), (float) (varianciaAPP02 - (varianciaAPP02 * 0.70)), (float) (varianciaAPP02 - (varianciaAPP02 * 0.30))));


        CandleDataSet dataset = new CandleDataSet(entries, nomesAPPS.get(0)+" and "+nomesAPPS.get(1)+ ", respectively");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add(nomesAPPS.get(0));
        labels.add(nomesAPPS.get(1));

        CandleData data = new CandleData(labels, dataset);
        candleStickChart.setData(data);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        candleStickChart.setDescription("Variances");
        candleStickChart.animateY(2000);
        candleStickChart.invalidate();



        //Gráfico de Barras

        //1º Passo
        barChart = (BarChart) findViewById(R.id.chart2);
        ArrayList<BarEntry> entrada = new ArrayList<BarEntry>();
        for (int i = 0; i < somasAPP01.size(); i++){
            entrada.add(new BarEntry(somasAPP01.get(i).floatValue(), i));
        }


        //2º Passo
        BarDataSet dataSet = new BarDataSet(entrada, nomesAPPS.get(0));

        ArrayList<String> legends1 = new ArrayList<String>();
        for (int i = 0; i < somasAPP01.size(); i++){
            legends1.add("obs"+ (i+1));
        }

        BarData dados = new BarData(legends1, dataSet);

        //3º Passo

        barChart.setData(dados);
        barChart.setDescription("Energy Consumption (In Joules) "+ nomesAPPS.get(0));
        barChart.animateY(2000);
        barChart.invalidate();


        barChar2 = (BarChart) findViewById(R.id.chart3);

        ArrayList<BarEntry> entrada2 = new ArrayList<BarEntry>();
        for (int i = 0; i < somasAPP02.size(); i++){
            entrada2.add(new BarEntry(somasAPP02.get(i).floatValue(), i));
        }

        ArrayList<String> legends2 = new ArrayList<String>();
        for (int i = 0; i < somasAPP02.size(); i++){
            legends2.add("obs"+ (i+1));
        }


        BarDataSet dataSet2 = new BarDataSet(entrada2, nomesAPPS.get(1));

        BarData dados2 = new BarData(legends2, dataSet2);

        barChar2.setData(dados2);
        barChar2.setDescription("Energy Consumption (In Joules) "+nomesAPPS.get(1));
        barChar2.animateY(2000);
        barChar2.invalidate();




    }


    private void process_message() {

        String message2 = "";
        String message3 = "";
        //String ap1 = app1;
        //String ap2 = app1;

        for(int i = 0; i < somasAPP01.size(); i++){
            message2 = message2 + somasAPP01.get(i) + "&";
        }
        for(int i = 0; i < somasAPP02.size(); i++){
            message3 = message3 + somasAPP02.get(i) + "&";
        }

        //sends the db to the server.

        String username = "Anonimo";
        String key = databaseReference.child("db_messages").push().getKey();
        Message post = new Message(MyUtils.generateUniqueUserId(mContext), username, message2, message3, app1, app2, System.currentTimeMillis() / 1000L);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("/db_messages/" + key, postValues);
        databaseReference.updateChildren(childUpdates);
    }








/*

    public void enviarDados(View view){


        new Thread(){
            public void run(){
                EditText nEt = (EditText) findViewById(R.id.nome);
                EditText sEt = (EditText) findViewById(R.id.sobrenome);
                EditText eEt = (EditText) findViewById(R.id.email);

                postHttp(nEt.getText().toString(), sEt.getText().toString(), eEt.getText().toString());
            }
        }.start();

    }

    public void postHttp(String nome, String sobrenome, String email){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://www.villopim.com.br/android/teste/server.php");

        try{
            ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
            valores.add(new BasicNameValuePair("nome", nome));
            valores.add(new BasicNameValuePair("sobrenome", sobrenome));
            valores.add(new BasicNameValuePair("email", email));

            httpPost.setEntity(new UrlEncodedFormEntity(valores));
            final HttpResponse resposta = httpClient.execute(httpPost);

            runOnUiThread(new Runnable(){
                public void run(){
                    try {
                        Toast.makeText(getBaseContext(), EntityUtils.toString(resposta.getEntity()), Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        catch(ClientProtocolException e){}
        catch(IOException e){}
    }






    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 01, 0, "Escolher Gráficos");
        menu.add(0, 02, 0, "Salvar Material de Análise");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                //Toast.makeText(escolha_apps.this, "Selecionado Opção 1!", Toast.LENGTH_LONG).show();
                showDialog(0);
                return true;
            case 1:
                //Toast.makeText(escolha_apps.this, "Selecionado Opção 2", Toast.LENGTH_LONG).show();
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
                builder.setTitle("Escolha as Informações:");
                //builder.setItems(TEMPOS,
                builder.setItems(opcoesInformacoes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //tempo_escolhido = tempo_valores[item];
                        //.makeText(escolha_apps.this, "Tempo escolhido: "+tempo_escolhido, Toast.LENGTH_SHORT).show();
                        showDialog(01);

                    }
                });
                return builder.create();


        }
        return null;
    }*/


}

