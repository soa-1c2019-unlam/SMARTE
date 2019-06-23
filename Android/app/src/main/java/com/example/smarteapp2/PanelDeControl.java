package com.example.smarteapp2;


import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PanelDeControl extends AppCompatActivity {

    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_de_control);

        //region inicializar firebase
        InicializarFirebase firebase = new InicializarFirebase();
        firebase.inicializar(getApplicationContext());
        firebaseDataBase = firebase.getFirebaseDataBase();
        databaseReference = firebase.getDatabaseReference();
        //endregion

        updatePanel();

        Button btnUpdate = findViewById(R.id.updateBtn);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePanel();
            }
        });
    }

    
    private void updatePanel() {

        databaseReference.child("matePuesto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long matePuesto = (Long)dataSnapshot.getValue();
                TextView txtViewUltrasonido = findViewById(R.id.ultrasonidoTxtView);
                if (matePuesto != 1) {
                    txtViewUltrasonido.setText("Mate no Encontrado");
                    txtViewUltrasonido.setTextColor(Color.RED);
                } else{
                    txtViewUltrasonido.setTextColor(Color.GREEN);
                    txtViewUltrasonido.setText("Mate Encontrado");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        databaseReference.child("termometro").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView txtViewCalentador = findViewById(R.id.calentadorTxtView);
                Long calentandoAgua = (Long)dataSnapshot.getValue();

                if (calentandoAgua == 1){
                    txtViewCalentador.setText("Calentador Prendido");
                    txtViewCalentador.setTextColor(Color.GREEN);
                } else {
                    txtViewCalentador.setText("Calentador Apagado");
                    txtViewCalentador.setTextColor(Color.RED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        databaseReference.child("servoAzucar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long servoAzucarActivado = (Long)dataSnapshot.getValue();
                TextView txtViewServoAzucar = findViewById(R.id.servoAzucarTxtView);
                if (servoAzucarActivado != 1) {
                    txtViewServoAzucar.setText("No solicitado");
                    txtViewServoAzucar.setTextColor(Color.RED);
                } else{
                    txtViewServoAzucar.setTextColor(Color.GREEN);
                    txtViewServoAzucar.setText("Azucar Solicitada");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        databaseReference.child("servoYerba").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long servoYerba = (Long)dataSnapshot.getValue();
                TextView txtViewServoYerba = findViewById(R.id.servoYerbaTxtView);
                if (servoYerba != 1) {
                    txtViewServoYerba.setText("No solicitado");
                    txtViewServoYerba.setTextColor(Color.RED);
                } else{
                    txtViewServoYerba.setTextColor(Color.GREEN);
                    txtViewServoYerba.setText("Yerba Solicitada");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        TextView textViewService = findViewById(R.id.ultimoMateTxtViewServiceBomba);
        if (isMyServiceRunning(BombaService.class)){
            textViewService.setTextColor(Color.GREEN);
            textViewService.setText("Service corriendo");
        }
        else{
            textViewService.setTextColor(Color.RED);
            textViewService.setText("Service detenido");
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
