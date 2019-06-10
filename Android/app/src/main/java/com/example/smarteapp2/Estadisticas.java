package com.example.smarteapp2;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Estadisticas extends AppCompatActivity {

    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;

    TextView matesTomados;
    TextView promedioPorDia;
    TextView azucarUsada;
    TextView azucarPorMate;
    TextView maximaCantidad;

    MatesPorDia ultimoMate;

    updateStats estadisticas;

    String fechaDeHoy;

    int cantidadTotalDeMates = 1;
    long cantidadTotalDeAzucar = 1;
    MatesPorDia maximoMatePorDia;
    long cantidadDeDias;
    float promedioDeMatesPorDia;
    float promedioDeAzucarPorMate;

    List<MatesPorDia> matesPorDiaList = new ArrayList<MatesPorDia>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        matesTomados = findViewById(R.id.textViewMatesTomados);
        promedioPorDia = findViewById(R.id.textViewPromedioMatePorDia);
        azucarUsada = findViewById(R.id.textViewAzucarUsado);
        azucarPorMate = findViewById(R.id.textViewPromedioAzucarPorMate);
        maximaCantidad = findViewById(R.id.textViewMaximaCantidadEnUnDia);

        inicializarFirebase();

        estadisticas = new updateStats();
        estadisticas.execute();

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDataBase.getReference();
    }

    private class updateStats extends AsyncTask<Void, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {


            databaseReference.child("MatesPorDia").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot objSnapShot : dataSnapshot.getChildren()){
                        MatesPorDia p = new MatesPorDia(objSnapShot.getValue(MatesPorDia.class));
                        matesPorDiaList.add(p);

                        cantidadDeDias = matesPorDiaList.size();

                        if (matesPorDiaList.size() > 0){
                            String fecha = matesPorDiaList.get(matesPorDiaList.size()-1).getFecha();
                            int mates = matesPorDiaList.get(matesPorDiaList.size()-1).getMates();
                            String id = matesPorDiaList.get(matesPorDiaList.size()-1).getId();
                            int azucar = matesPorDiaList.get(matesPorDiaList.size()-1).getAzucar();

                            ultimoMate = new MatesPorDia(id, fecha, mates, azucar);
                        }

                        if(cantidadDeDias != 0){

                            cantidadTotalDeMates = calcularCantidadDeMates();
                            matesTomados.setText(String.valueOf(cantidadTotalDeMates));

                            promedioDeMatesPorDia = cantidadTotalDeMates / cantidadDeDias;
                            promedioPorDia.setText(String.valueOf(promedioDeMatesPorDia));

                            cantidadTotalDeAzucar = calcularCantidadDeAzucar();
                            azucarUsada.setText(String.valueOf(cantidadTotalDeAzucar));
                            promedioDeAzucarPorMate = cantidadTotalDeAzucar / cantidadTotalDeMates;
                            azucarPorMate.setText(String.valueOf(promedioDeAzucarPorMate));

                            Collections.sort(matesPorDiaList, new ComparadorDeFechas());
                            int cantidadDeMatesMax = matesPorDiaList.get(0).getMates();
                            String fechaMax = matesPorDiaList.get(0).getFecha();
                            String id = matesPorDiaList.get(0).getId();
                            int cantidadAzucar = matesPorDiaList.get(0).getAzucar();

                            maximoMatePorDia = new MatesPorDia(id , fechaMax, cantidadDeMatesMax, cantidadAzucar);

                            maximaCantidad.setText(maximoMatePorDia.toString());

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            databaseReference.child("bomba").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot objSnapShot : dataSnapshot.getChildren()){
                        MatesPorDia p = new MatesPorDia(objSnapShot.getValue(MatesPorDia.class));
                        if(ultimoMate != null && ultimoMate.getFecha() == fechaDeHoy){
                            MatesPorDia mate = new MatesPorDia(ultimoMate.getId(), ultimoMate.getFecha(), ultimoMate.getMates()+1, ultimoMate.getAzucar());
                            databaseReference.child("MatesPorDia").child(mate.getId()).setValue(mate);
                        }
                        else{
                            MatesPorDia mate = new MatesPorDia(fechaDeHoy, fechaDeHoy, 1, 0);
                            databaseReference.child("MatesPorDia").child(mate.getId()).setValue(mate);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return true;
        }
    }

    private int calcularCantidadDeMates(){
        int cantidad = 0;

        for (MatesPorDia mates: matesPorDiaList) {
            cantidad = cantidad + mates.getMates();
        }

        return cantidad;
    }

    private int calcularCantidadDeAzucar(){
        int cantidad = 0;

        for (MatesPorDia mates: matesPorDiaList) {
            cantidad = cantidad + mates.getAzucar();
        }

        return cantidad;
    }

}
