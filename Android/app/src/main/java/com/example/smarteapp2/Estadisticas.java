package com.example.smarteapp2;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
    TextView reporteAgua;
    TextView reporteAzucar;
    TextView porcentajeAgua;
    TextView porcentajeAzucar;

    ProgressBar progressAgua;
    ProgressBar progressAzucar;

    MatesPorDia ultimoMate;

    updateStats estadisticas;

    String fechaDeHoy;

    int cantidadTotalDeMates = 1;
    long cantidadTotalDeAzucar = 1;
    MatesPorDia maximoMatePorDia;
    long cantidadDeDias;
    float promedioDeMatesPorDia;
    float promedioDeAzucarPorMate;

    int matesDelDia = 0;

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

        porcentajeAgua = findViewById(R.id.textViewPorcentajeAgua);
        porcentajeAzucar = findViewById(R.id.textViewPorcentajeAzucar);

        reporteAgua = findViewById(R.id.textViewReporteAgua);
        reporteAzucar = findViewById(R.id.textViewReporteAzucar);
        reporteAzucar.setTextColor(Color.BLUE);

        progressAgua = findViewById(R.id.progressBarAgua);
        progressAzucar = findViewById(R.id.progressBarAzucar);


        inicializarFirebase();

        fechaDeHoy = TimePickerFragment.obtenerFechaDeHoy();

        estadisticas = new updateStats();
        estadisticas.execute();

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDataBase.getReference();
    }

    /**
     * Lee en firebase las variables para calcular las cantidades totales de mates y azucar
     * y genera las estadisticas.
     */
    private class updateStats extends AsyncTask<Void, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {

            //region Escucho la coleccion MatesPorDia de firebase
            databaseReference.child("MatesPorDia").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    matesPorDiaList.clear();
                    for (DataSnapshot objSnapShot : dataSnapshot.getChildren()){
                        MatesPorDia p = new MatesPorDia(objSnapShot.getValue(MatesPorDia.class));
                        matesPorDiaList.add(p);
                    }

                    cantidadDeDias = matesPorDiaList.size();

                    if (cantidadDeDias > 0){
                        //region Obtener datos del ultimo dia que se tomo mate
                        String fecha = matesPorDiaList.get(matesPorDiaList.size()-1).getFecha();
                        int mates = matesPorDiaList.get(matesPorDiaList.size()-1).getMates();
                        String id = matesPorDiaList.get(matesPorDiaList.size()-1).getId();
                        int azucar = matesPorDiaList.get(matesPorDiaList.size()-1).getAzucar();
                        ultimoMate = new MatesPorDia(id, fecha, mates, azucar);
                        //endregion

                        //region Seteo de progresos diarios
                        if (Integer.parseInt(ultimoMate.getFecha()) == Integer.parseInt(fechaDeHoy)){
                            int porcentajeDeAguaDiario = (ultimoMate.getMates() * 100) / 50;
                            progressAgua.setProgress(porcentajeDeAguaDiario);

                            porcentajeAgua.setText(porcentajeDeAguaDiario + "%");

                            if (porcentajeDeAguaDiario == 100){
                                reporteAzucar.setText("• Llegaste al objetivo de agua diaria, felicitaciones!");
                                reporteAzucar.setTextColor(Color.BLUE);
                                porcentajeAgua.setTextColor(Color.BLUE);
                            }

                            int porcentajeDeAzucarDiario = (ultimoMate.getAzucar() * 100) / 10;
                            if (porcentajeDeAzucarDiario > 60 && porcentajeDeAzucarDiario < 100 ){
                                porcentajeAzucar.setTextColor(Color.RED);
                                reporteAzucar.setText("• Cuidado! máximo consumo de azucar cerca");
                                reporteAzucar.setTextColor(Color.RED);
                            }

                            if (porcentajeDeAzucarDiario >= 100){
                                porcentajeDeAzucarDiario = 100;
                                porcentajeAzucar.setTextColor(Color.RED);
                                reporteAzucar.setText("• Diabetes Alert! aflojale al azucar");
                                reporteAzucar.setTextColor(Color.RED);
                                progressAzucar.getProgressDrawable().setColorFilter(
                                        Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                            }
                            porcentajeAzucar.setText(porcentajeDeAzucarDiario + "%");

                            progressAzucar.setProgress(porcentajeDeAzucarDiario);
                        }
                        else{
                            progressAgua.setProgress(0);
                            porcentajeAzucar.setText(0);
                            porcentajeAgua.setText(0);
                            progressAzucar.setProgress(0);
                        }
                        //endregion

                        //region Calcular y setear estadisticas
                        cantidadTotalDeMates = calcularCantidadDeMates();
                        matesTomados.setText(String.valueOf(cantidadTotalDeMates));

                        promedioDeMatesPorDia = cantidadTotalDeMates / cantidadDeDias;
                        promedioPorDia.setText(String.valueOf(promedioDeMatesPorDia));

                        cantidadTotalDeAzucar = calcularCantidadDeAzucar();
                        azucarUsada.setText(String.valueOf(cantidadTotalDeAzucar));
                        promedioDeAzucarPorMate = cantidadTotalDeAzucar / cantidadTotalDeMates;
                        azucarPorMate.setText(String.valueOf(promedioDeAzucarPorMate));
                        //endregion

                        //region Obtener dia que mas mates se tomo
                        Collections.sort(matesPorDiaList, new ComparadorDeFechas());
                        int cantidadDeMatesMax = matesPorDiaList.get(0).getMates();
                        String fechaMax = matesPorDiaList.get(0).getFecha();
                        String idMax = matesPorDiaList.get(0).getId();
                        int cantidadAzucar = matesPorDiaList.get(0).getAzucar();

                        maximoMatePorDia = new MatesPorDia(idMax , fechaMax, cantidadDeMatesMax, cantidadAzucar);

                        maximaCantidad.setText(maximoMatePorDia.toString());
                        //endregion
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //endregion

            //region Escucho la variable bomba de firebase
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
            //endregion

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
