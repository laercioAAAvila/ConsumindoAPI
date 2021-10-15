package com.example.consumindoapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

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
        private String cep, logradouro, bairro, localidade, uf;
        private ArrayList<String> endereco = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
            String returnConexao = ConexaoAPI.getData(strings[0]);
            return returnConexao;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                enderecoResult(s);


            }catch (Exception e){
                e.printStackTrace();
            }finally {
                textViewCep.setText(endereco.get(0));
                textViewStreet.setText(endereco.get(1));
                textViewDistrict.setText(endereco.get(2));
                textViewCity.setText(endereco.get(3));
                textViewState.setText(endereco.get(4));
            }
        }

        private ArrayList<String> enderecoResult (String s) {
            String reformula = s.toString();
            reformula = reformula.replace("{","");
            reformula = reformula.replace("}","");
            reformula = reformula.replace("-","");
            reformula = reformula.replace("\"","");
            reformula = reformula.replace(" ", "");
            reformula = reformula.replace(":,", ": S/informação,");
            reformula = reformula.replace(":", ": ");
            //======================================================
            String vetorString[];

            vetorString = reformula.split(",");

            this.cep = vetorString[0];
            this.logradouro = vetorString[1];
            this.bairro = vetorString[3];
            this.localidade = vetorString[4];
            this.uf = vetorString[5];

            this.cep = cep.replace("cep: ", "");
            this.logradouro = logradouro.replace("logradouro: ", "");
            this.logradouro = logradouro.replace(" ", "");
            this.bairro = bairro.replace("bairro: ", "");
            this.bairro = bairro.replace(" ", "");
            this.localidade = localidade.replace("localidade: ", "");
            this.uf = uf.replace("uf: ", "");

            this.endereco.add(cep);
            this.endereco.add(logradouro);
            this.endereco.add(bairro);
            this.endereco.add(localidade);
            this.endereco.add(uf);
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