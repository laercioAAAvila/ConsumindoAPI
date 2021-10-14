package com.example.consumindoapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button buttonFind;
    private TextView textViewResult;
    private EditText editTextCep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonFind = (Button) findViewById(R.id.button);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
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
            textViewResult.setText(s);
        }
    }
}