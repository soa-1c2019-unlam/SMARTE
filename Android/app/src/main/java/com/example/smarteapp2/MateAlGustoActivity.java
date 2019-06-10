package com.example.smarteapp2;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MateAlGustoActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;

    Button botonAzucar, botonYerba, botonAgua;
    List<MatesPorDia> matesPorDiaList = new ArrayList<MatesPorDia>();
    MatesPorDia ultimoMate;
    boolean matePuesto;

    String fechaDeHoy;

    fechasEnBase fechasMate;

    long cantidadAzucarTotal = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mate_al_gusto);

        inicializarFirebase();

        fechasMate = new fechasEnBase();
        fechasMate.execute();

        LocalDateTime dateHoy = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));

        fechaDeHoy = dateHoy.toString();
        fechaDeHoy = fechaDeHoy.split("-")[0]+fechaDeHoy.split("-")[1]+fechaDeHoy.split("-")[2].split("T")[0];

        listarDatos();

        botonAgua = findViewById(R.id.colocarAguaButton);
        botonAzucar = findViewById(R.id.azucarButton);
        botonYerba = findViewById(R.id.yerbaButton);

        botonAgua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("funcionaConMicrofono").setValue(0);
                if (matePuesto){
                    databaseReference.child("bomba").setValue(1);
                    if(ultimoMate != null && ultimoMate.getFecha() == fechaDeHoy){
                        MatesPorDia mate = new MatesPorDia(ultimoMate.getId(), ultimoMate.getFecha(), ultimoMate.getMates()+1);
                        databaseReference.child("MatesPorDia").child(mate.getId()).setValue(mate);
                    }
                    else{
                        MatesPorDia mate = new MatesPorDia(fechaDeHoy, fechaDeHoy, 1);
                        databaseReference.child("MatesPorDia").child(mate.getId()).setValue(mate);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"No hay mate puesto", Toast.LENGTH_LONG).show();
                }
            }
        });

        botonYerba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("funcionaConMicrofono").setValue(0);
                if(matePuesto){
                    databaseReference.child("servoYerba").setValue(1);
                }
                else{
                    Toast.makeText(getApplicationContext(),"No hay mate puesto", Toast.LENGTH_LONG).show();
                }

            }
        });

        botonAzucar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("funcionaConMicrofono").setValue(0);
                if (matePuesto){
                    databaseReference.child("servoAzucar").setValue(1);
                    databaseReference.child("cantAzucar").setValue(1);
                    databaseReference.child("azucarUsada").setValue(cantidadAzucarTotal++);
                }
                else{
                    Toast.makeText(getApplicationContext(),"No hay mate puesto", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDataBase.getReference();
    }

    private void listarDatos(){
        databaseReference.child("MatesPorDia").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapShot : dataSnapshot.getChildren()){
                    MatesPorDia p = new MatesPorDia(objSnapShot.getValue(MatesPorDia.class));
                    matesPorDiaList.add(p);

                    if (matesPorDiaList.size() > 0){
                        String fecha = matesPorDiaList.get(matesPorDiaList.size()-1).getFecha();
                        int mates = matesPorDiaList.get(matesPorDiaList.size()-1).getMates();
                        String id = matesPorDiaList.get(matesPorDiaList.size()-1).getId();

                        ultimoMate = new MatesPorDia(id, fecha, mates);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("azucarUsada").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cantidadAzucarTotal = (long)dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private class fechasEnBase extends AsyncTask<Void, Integer, Boolean> {

        List<MatesPorDia> matesPorDiaList = new ArrayList<MatesPorDia>();

        @Override
        protected Boolean doInBackground(Void... voids) {

            databaseReference.child("matePuesto").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Long valorMate = (Long)dataSnapshot.getValue();

                    if (valorMate == 1) {
                        matePuesto = true;
                    }
                    else{
                        matePuesto = false;
                    }
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

                        if (matesPorDiaList.size() > 0){
                            String fecha = matesPorDiaList.get(matesPorDiaList.size()-1).getFecha();
                            int mates = matesPorDiaList.get(matesPorDiaList.size()-1).getMates();
                            String id = matesPorDiaList.get(matesPorDiaList.size()-1).getId();

                            ultimoMate = new MatesPorDia(id, fecha, mates);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            databaseReference.child("azucarUsada").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cantidadAzucarTotal = (long)dataSnapshot.getValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            return true;
        }
    }
}
