package com.example.smarteapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region Inicio Service de monitoreo de bomba
        Intent msgIntent = new Intent(MainActivity.this, BombaService.class);
        startService(msgIntent);
        //endregion

        //region Si click en boton iniciar
        Button btnIniciar = findViewById(R.id.iniciarButton);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), IniciarCebador.class);
                startActivityForResult(intent, 0);
            }
        });
        //endregion

        //region Si click en boton perfiles
        Button btnPerfiles = findViewById(R.id.configurarButton);
        btnPerfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigurarPerfiles.class);
                startActivityForResult(intent, 0);
            }
        });
        //endregion

        //region Si click en boton panel de control
        Button btnPanelControl = findViewById(R.id.panelControlButton);
        btnPanelControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PanelDeControl.class);
                startActivityForResult(intent, 0);
            }
        });
        //endregion

        //region Si click en boton estadisticas
        Button estadisticasButton = findViewById(R.id.statsButton);
        estadisticasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Estadisticas.class);
                startActivityForResult(intent, 0);
            }
        });
        //endregion
    }

    protected void onDestroy()
    {
        super.onDestroy();
    }

}
