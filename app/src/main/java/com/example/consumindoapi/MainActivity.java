package com.example.consumindoapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button buttonFind;
    private TextView textViewCep, textViewDistrict , textViewStreet, textViewCity, textViewState;
    private TextView textViewCepTitle, textViewDistrictTitle , textViewStreetTitle, textViewCityTitle, textViewStateTitle, textViewError;
    private EditText editTextCep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonFind = (Button) findViewById(R.id.button);
        textViewCepTitle = (TextView) findViewById(R.id.textViewCEPTitle);
        textViewCep = (TextView) findViewById(R.id.textViewCEP);
        textViewDistrictTitle = (TextView) findViewById(R.id.textViewDistrictTitle);
        textViewDistrict = (TextView) findViewById(R.id.textViewDistrict);
        textViewStreetTitle = (TextView) findViewById(R.id.textViewStreetTitle);
        textViewStreet = (TextView) findViewById(R.id.textViewStreet);
        textViewCityTitle = (TextView) findViewById(R.id.textViewCityTitle);
        textViewCity = (TextView) findViewById(R.id.textViewCity);
        textViewStateTitle = (TextView) findViewById(R.id.textViewStateTitle);
        textViewState = (TextView) findViewById(R.id.textViewState);
        textViewError = (TextView) findViewById(R.id.textViewError);
        editTextCep = (EditText) findViewById(R.id.inputCEP);

        buttonFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                String getCep;
                getCep = editTextCep.getText().toString();

                Tarefa tarefa = new Tarefa();
                tarefa.execute("https://viacep.com.br/ws/" + getCep + "/json/");
                } catch (Exception e){
                    e.printStackTrace();
                }finally {
                    textViewError.setText("Error: Coloque o cep");
                }
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
                textViewCepTitle.setVisibility(View.VISIBLE);
                textViewCep.setText(endereco.get(0));
                textViewStreetTitle.setVisibility(View.VISIBLE);
                textViewStreet.setText(endereco.get(1));
                textViewDistrictTitle.setVisibility(View.VISIBLE);
                textViewDistrict.setText(endereco.get(2));
                textViewCityTitle.setVisibility(View.VISIBLE);
                textViewCity.setText(endereco.get(3));
                textViewStateTitle.setVisibility(View.VISIBLE);
                textViewState.setText(endereco.get(4));
            }
        }

        private ArrayList<String> enderecoResult (String s) {


            try {
                String reformula = s.toString();
                reformula = reformula.replace("{", "");
                reformula = reformula.replace("}", "");
                reformula = reformula.replace("-", "");
                reformula = reformula.replace("\"", "");
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

                return endereco;
            } catch(ArrayIndexOutOfBoundsException exception) {
                exception.printStackTrace();
            } finally {
                this.endereco.add(cep);
                this.endereco.add("Sem informações");
                this.endereco.add("Sem informações");
                this.endereco.add("Sem informações");
                this.endereco.add("Sem informações");
                return endereco;
            }
        }
    }
}