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
    }
}
