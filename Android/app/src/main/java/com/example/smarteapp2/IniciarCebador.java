package com.example.smarteapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class IniciarCebador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_cebador);

        //region Apretar boton Custom
        Button btnCustom = findViewById(R.id.customButton);
        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MateAlGustoActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        //endregion

        //region Apretar boton iniciar perfiles
        Button btnIniciarPerfiles = findViewById(R.id.opcionPerfilesButton);
        btnIniciarPerfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), IniciarPerfiles.class);
                    startActivityForResult(intent, 0);
            }
        });
        //endregion
    }
}
