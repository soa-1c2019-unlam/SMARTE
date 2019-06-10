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

    long cantidadAzucarTotal = 0;

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
        long cantidadTotalDeMates = 1;
        long cantidadTotalDeAzucar = 1;
        List<MatesPorDia> matesPorDiaList = new ArrayList<MatesPorDia>();
        MatesPorDia maximoMatePorDia;
        long cantidadDeDias;
        float promedioDeMatesPorDia;
        float promedioDeAzucarPorMate;

        @Override
        protected Boolean doInBackground(Void... voids) {

            databaseReference.child("cantidadDeMates").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cantidadTotalDeMates = (long)dataSnapshot.getValue();
                    matesTomados.setText(String.valueOf(cantidadTotalDeMates));

                    cantidadDeDias = matesPorDiaList.size();

                    if(cantidadDeDias != 0){
                        promedioDeMatesPorDia = cantidadTotalDeMates / cantidadDeDias;
                        promedioPorDia.setText(String.valueOf(promedioDeMatesPorDia));

                        promedioDeAzucarPorMate = cantidadTotalDeAzucar / cantidadTotalDeMates;
                        azucarPorMate.setText(String.valueOf(promedioDeAzucarPorMate));

                        Collections.sort(matesPorDiaList, new ComparadorDeFechas());
                        int cantidadDeMatesMax = matesPorDiaList.get(0).getMates();
                        String fechaMax = matesPorDiaList.get(0).getFecha();
                        String id = matesPorDiaList.get(0).getId();

                        maximoMatePorDia = new MatesPorDia(id , fechaMax, cantidadDeMatesMax);

                        maximaCantidad.setText(maximoMatePorDia.toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            databaseReference.child("azucarUsada").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cantidadTotalDeAzucar = (long)dataSnapshot.getValue();
                    azucarUsada.setText(String.valueOf(cantidadTotalDeAzucar));

                    promedioDeMatesPorDia = cantidadTotalDeMates / cantidadDeDias;
                    promedioPorDia.setText(String.valueOf(promedioDeMatesPorDia));

                    promedioDeAzucarPorMate = cantidadTotalDeAzucar / cantidadTotalDeMates;
                    azucarPorMate.setText(String.valueOf(promedioDeAzucarPorMate));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

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

                            ultimoMate = new MatesPorDia(id, fecha, mates);
                        }

                        if(cantidadDeDias != 0){

                            promedioDeMatesPorDia = cantidadTotalDeMates / cantidadDeDias;
                            promedioPorDia.setText(String.valueOf(promedioDeMatesPorDia));

                            promedioDeAzucarPorMate = cantidadTotalDeAzucar / cantidadTotalDeMates;
                            azucarPorMate.setText(String.valueOf(promedioDeAzucarPorMate));

                            Collections.sort(matesPorDiaList, new ComparadorDeFechas());
                            int cantidadDeMatesMax = matesPorDiaList.get(0).getMates();
                            String fechaMax = matesPorDiaList.get(0).getFecha();
                            String id = matesPorDiaList.get(0).getId();

                            maximoMatePorDia = new MatesPorDia(id , fechaMax, cantidadDeMatesMax);

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
                            MatesPorDia mate = new MatesPorDia(ultimoMate.getId(), ultimoMate.getFecha(), ultimoMate.getMates()+1);
                            databaseReference.child("MatesPorDia").child(mate.getId()).setValue(mate);
                        }
                        else{
                            MatesPorDia mate = new MatesPorDia(fechaDeHoy, fechaDeHoy, 1);
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

}
