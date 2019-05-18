package com.example.smarteapp2;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IniciarServo extends AppCompatActivity {

    Button servoUnoButton;
    TextView txtEstadoBomba;

    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;
    DatabaseReference sirviendoAguaRef;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_servo);

        servoUnoButton = findViewById(R.id.servo1Button);
        txtEstadoBomba = findViewById(R.id.textViewBomba);

        inicializarFirebase();

        sirviendoAguaRef = sirviendoAguaRef.child("bomba");

        servoUnoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("servoAzucar").setValue(i++);
            }
        });

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                int bomba = dataSnapshot.getValue(Integer.class);
                if(bomba == 1){
                    txtEstadoBomba.setText("Bomba prendida");
                    txtEstadoBomba.setTextColor(Color.BLUE);
                }
                else if (bomba == 0){
                    txtEstadoBomba.setText("Bomba apagada");
                    txtEstadoBomba.setTextColor(Color.RED);
                }
                // ...
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        sirviendoAguaRef.addValueEventListener(postListener);
    }


    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDataBase.getReference();
        sirviendoAguaRef = databaseReference;
    }
}
