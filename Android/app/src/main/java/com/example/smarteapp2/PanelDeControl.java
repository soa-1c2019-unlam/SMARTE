package com.example.smarteapp2;


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
        inicializarFirebase();

        Button btnUpdate = findViewById(R.id.updateBtn);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePanel();
            }
        });
    }


    private void updatePanel() {
        databaseReference.child("temperatura").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView txtViewTemperatura = findViewById(R.id.temperaturaTxtView);
                Long temp = (Long)dataSnapshot.getValue();

                if (temp < 20)
                    txtViewTemperatura.setTextColor(Color.BLUE);
                else if (temp >= 20 && temp < 60)
                    txtViewTemperatura.setTextColor(Color.parseColor("#E5A62A"));
                else if (temp >=60 && temp <=85 )
                    txtViewTemperatura.setTextColor(Color.GREEN);
                else
                    txtViewTemperatura.setTextColor(Color.RED);

                txtViewTemperatura.setText(temp.toString());
        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("matePuesto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView txtViewUltrasonido = findViewById(R.id.ultrasonidoTxtView);
                Long matePuesto = (Long)dataSnapshot.getValue();

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

        databaseReference.child("ledCalentandoPWM").addValueEventListener(new ValueEventListener() {
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
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDataBase.getReference();
    }

}
