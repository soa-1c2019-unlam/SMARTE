package com.example.smarteapp2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button botonPrenderLed, botonApagarLed;
    TextView txtEstadoLed, txtTemperatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnIniciar = findViewById(R.id.iniciarButton);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), IniciarCebador.class);
                startActivityForResult(intent, 0);
            }
        });

        Button btnPerfiles = findViewById(R.id.configurarButton);
        btnPerfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigurarPerfiles.class);
                startActivityForResult(intent, 0);
            }
        });

        Button btnHorarios = findViewById(R.id.programarButton);
        btnHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProgramarHorarios.class);
                startActivityForResult(intent, 0);
            }
        });


        botonPrenderLed = findViewById(R.id.prenderLedButton);
        botonApagarLed = findViewById(R.id.apagarLedButton);

        txtEstadoLed = findViewById(R.id.textLedState);
        txtTemperatura = findViewById(R.id.textTemp);

        botonPrenderLed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Solicitar("/4/on");
                }
        });

        botonApagarLed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Solicitar("/4/off");
            }
        });
    }

    private void Solicitar(String comando){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String url = "http://192.168.1.200";

            new SolicitarDatos().execute(url + comando);
        }
        else {
            Toast.makeText(MainActivity.this, "Ninguna conexión detectada.", Toast.LENGTH_LONG).show();
        }
    }

    private class SolicitarDatos extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... url) {
            return Conexion.getDatos(url[0]);
        }

        @Override
        protected void onPostExecute(String resultado){
            if(resultado != null){
                if(resultado.contains("GPIO 4 - State on")){
                    txtEstadoLed.setTextColor(Color.GREEN);
                    txtEstadoLed.setText("Led prendido");
                    txtTemperatura.setText(resultado.split("temperatura es")[1].substring(10,15));
                }
                else if (resultado.contains("GPIO 4 - State off")){
                    txtEstadoLed.setText("Led apagado");
                    txtEstadoLed.setTextColor(Color.RED);
                    txtTemperatura.setText(resultado.split("temperatura es")[1].substring(10,15));
                }
            }
            else{
                Toast.makeText(MainActivity.this, "Ocurrió un error.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
