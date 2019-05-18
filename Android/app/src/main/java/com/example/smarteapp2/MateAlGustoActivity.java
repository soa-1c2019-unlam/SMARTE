package com.example.smarteapp2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MateAlGustoActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;

    Button botonAzucar, botonYerba, botonAgua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mate_al_gusto);

        inicializarFirebase();

        botonAgua = findViewById(R.id.colocarAguaButton);
        botonAzucar = findViewById(R.id.azucarButton);
        botonYerba = findViewById(R.id.yerbaButton);

        botonAgua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    databaseReference.child("bomba").setValue(1);
                }
            });

        botonYerba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("servoYerba").setValue(1);
            }
        });

        botonAzucar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("servoAzucar").setValue(1);
            }
        });


    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDataBase.getReference();
    }
}
