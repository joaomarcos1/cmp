package nupasd.research.appcompare.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import nupasd.research.appcompare.R;

public class advanced_Parameters extends Activity {


    private Button confirmar;
    private Button voltar;
    private EditText numeroOBservacoes;
    private EditText tempoCadaObservacao;
    private EditText tempoAntesdoTeste;
    private RadioGroup elementosAVerificar;
    private RadioGroup nivelConfianca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced__parameters);

        numeroOBservacoes = (EditText) findViewById(R.id.edt_ADVANCED_Number_Of_Observations);

        tempoCadaObservacao = (EditText) findViewById(R.id.edt_ADVANCED_Time_Cada_Observacao);

        tempoAntesdoTeste = (EditText) findViewById(R.id.edt_ADVANCED_Tempo_Antes_do_Teste);

        elementosAVerificar = (RadioGroup) findViewById(R.id.radioGroup_Elements_To_Check);

        nivelConfianca = (RadioGroup) findViewById(R.id.radioGroup_ADVANCED_TRUST_LEVEL);

        confirmar = (Button) findViewById(R.id.btn_CONFIRMAR_Tela_Parametros_Avancados);

        numeroOBservacoes.setEnabled(false);
        tempoCadaObservacao.setEnabled(false);
        tempoAntesdoTeste.setEnabled(false);
        elementosAVerificar.setEnabled(false);
        nivelConfianca.setEnabled(false);
        confirmar.setEnabled(false);


        voltar = (Button) findViewById(R.id.btn_VOLTAR_Tela_Parametros_Avancados);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(advanced_Parameters.this);
        builder.setTitle("NOTICE");
        builder.setMessage("Avaliable in future versions!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        alert = builder.create();
        alert.show();



    }
}
