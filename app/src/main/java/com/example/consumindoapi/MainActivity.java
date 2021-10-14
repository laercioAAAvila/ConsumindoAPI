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
    private TextView textViewCep, textViewStreet, textViewCity, textViewState;
    private EditText editTextCep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonFind = (Button) findViewById(R.id.button);
        textViewCep = (TextView) findViewById(R.id.textViewCEP);
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
                String string1 = s.toString();
                string1 = string1.replace("{","");
                string1 = string1.replace("}","");
                string1 = string1.replace("-","");
                string1 = string1.replace("\"","");
                string1 = string1.replace(",","");
                String strings2[] = string1.split(":");
                endereco(strings2);
                /*for(int i = 0;i < strings2.length;i++) {
                    System.out.print(strings2[i]);
                }*/

            }catch (Exception e){
                e.printStackTrace();
            }
            textViewCep.setText(s);
        }

        private String[] endereco (String[] s) {
            String antigoEndereco[] = s;
            for (int i = 0; i < antigoEndereco.length; i++){
                if (antigoEndereco[i] == "cep"){
                    antigoEndereco[i] = "Sem Informação";
                }if (antigoEndereco[i] == "");
            }
            return antigoEndereco;
        }
    }
}