package com.example.smarteapp2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

public class ProgramarHorarios extends AppCompatActivity {


    Switch switchActivado ;
    EditText textoHorario ;
    Spinner spinnerPerfiles ;
    Button guardarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programar_horarios);

        switchActivado = findViewById(R.id.activarSwitch);
        textoHorario = findViewById(R.id.editText);
        spinnerPerfiles = findViewById(R.id.spinner);
        guardarButton = findViewById(R.id.guardarButton);

        spinnerPerfiles.setEnabled(false);

        switchActivado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textoHorario.setEnabled(true);
                    spinnerPerfiles.setEnabled(true);
                    guardarButton.setEnabled(true);
                } else {
                    textoHorario.setEnabled(false);
                    spinnerPerfiles.setEnabled(false);
                    guardarButton.setEnabled(false);
                }
            }
        });
    }
}
