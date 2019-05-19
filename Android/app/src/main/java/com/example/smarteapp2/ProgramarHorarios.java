package com.example.smarteapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProgramarHorarios extends AppCompatActivity {


    Switch switchActivado ;
    EditText textoHorario ;
    Spinner spinnerPerfiles ;
    Button guardarButton;

    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;

    private List<Perfil> perfilList = new ArrayList<Perfil>();
    ArrayAdapter<Perfil> arrayAdapterPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programar_horarios);

        switchActivado = findViewById(R.id.activarSwitch);
        textoHorario = findViewById(R.id.editTextHorario);
        spinnerPerfiles = findViewById(R.id.spinner);
        guardarButton = findViewById(R.id.guardarButton);

        spinnerPerfiles.setEnabled(false);

        inicializarFirebase();

        ListarDatos();

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

        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("programaActivado").setValue(1);
                databaseReference.child("horarioAutomatico").setValue(textoHorario);
            }
        });
    }

    private void ListarDatos() {
        databaseReference.child("Perfiles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                perfilList.clear();
                for (DataSnapshot objSnapShot : dataSnapshot.getChildren()){
                    Perfil p = objSnapShot.getValue(Perfil.class);
                    if (p == null){
                        Toast.makeText(ProgramarHorarios.this,"Atención: No hay perfiles cargados!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(guardarButton.getContext(), IniciarCebador.class);
                        startActivityForResult(intent, 0);
                    }
                    else{
                        perfilList.add(p);
                        arrayAdapterPerfil = new ArrayAdapter(ProgramarHorarios.this, android.R.layout.simple_spinner_dropdown_item,perfilList);
                        spinnerPerfiles.setAdapter(arrayAdapterPerfil);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDataBase.getReference();
    }
}
