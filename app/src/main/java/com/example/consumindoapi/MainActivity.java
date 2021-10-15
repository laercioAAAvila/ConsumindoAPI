package com.example.consumindoapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button buttonFind;
    private TextView textViewCep, textViewDistrict , textViewStreet, textViewCity, textViewState;
    private EditText editTextCep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonFind = (Button) findViewById(R.id.button);
        textViewCep = (TextView) findViewById(R.id.textViewCEP);
        textViewDistrict = (TextView) findViewById(R.id.textViewDistrict);
        textViewStreet = (TextView) findViewById(R.id.textViewStreet);
        textViewCity = (TextView) findViewById(R.id.textViewCity);
        textViewState = (TextView) findViewById(R.id.textViewState);
        editTextCep = (EditText) findViewById(R.id.inputCEP);

        buttonFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getCep;
                getCep = editTextCep.getText().toString();

                Tarefa tarefa = new Tarefa();
                tarefa.execute("https://viacep.com.br/ws/"+getCep+"/json/");
            }
        });
    }

    private class Tarefa extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String returnConexao = ConexaoAPI.getData(strings[0]);
            return returnConexao;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                endereco(s);
            }catch (Exception e){
                e.printStackTrace();
            }
            textViewCep.setText(s);
        }

        private String[] endereco (String s) {
            String reformula = s.toString();
            reformula = reformula.replace("{","");
            reformula = reformula.replace("}","");
            reformula = reformula.replace("-","");
            reformula = reformula.replace("\"","");
            reformula = reformula.replace(" ", "");
            reformula = reformula.replace(":,", ": Sem informação,");
            reformula = reformula.replace(":", ": ");
            //======================================================
            String vetorString[], cep, logradouro, bairro, localidade, uf, endereco[] = new String[5];

            vetorString = reformula.split(",");

            cep = vetorString[0];
            logradouro = vetorString[1];
            bairro = vetorString[3];
            localidade = vetorString[4];
            uf = vetorString[5];

            endereco[0]= cep.replace("cep: ", "");
            endereco[1]= logradouro.replace("logradouro: ", "");
            endereco[2]= bairro.replace("bairro: ", "");
            endereco[3]= localidade.replace("localidade: ", "");
            endereco[4]= uf.replace("uf: ", "");

            /*
            endereco[0]= cep;
            endereco[1]= logradouro;
            endereco[2]= bairro;
            endereco[3]= localidade;
            endereco[4]= uf;*/

            //=======================================================
            /*
            for (int i = 0; i < endereco.length; i++) {
                System.out.println(endereco[i]);
            }
            */
            //=======================================================
            return endereco;
        }
    }
}