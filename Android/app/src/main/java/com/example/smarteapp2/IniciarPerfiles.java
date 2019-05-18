package com.example.smarteapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class IniciarPerfiles extends AppCompatActivity {

    private static final String TAG = "IniciarPerfiles";
    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;

    private List<Perfil> perfilList = new ArrayList<Perfil>();
    ArrayAdapter<Perfil> arrayAdapterPerfil;

    ListView listaPerfiles;
    Perfil perfilSeleccionado;
    Button iniciarPerfilButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_perfiles);

        listaPerfiles = findViewById(R.id.listaPerfiles);
        iniciarPerfilButton = findViewById(R.id.iniciarPerfilbutton);

        inicializarFirebase();

        ListarDatos();

        listaPerfiles.setOnItemClickListener(new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                perfilSeleccionado = (Perfil) parent.getItemAtPosition(position);
                iniciarPerfilButton.setVisibility(View.VISIBLE);
                iniciarPerfilButton.setText("Iniciar " + perfilSeleccionado.getNombre());
            }
        });

        iniciarPerfilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("servoAzucar").setValue(1);
                databaseReference.child("servoYerba").setValue(1);
                databaseReference.child("cantAzucar").setValue(Integer.parseInt(perfilSeleccionado.getAzucar()));
                Toast.makeText(IniciarPerfiles.this,"A Disfrutar del mejor mate!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
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
                        Toast.makeText(IniciarPerfiles.this,"Atención: No hay perfiles cargados!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(listaPerfiles.getContext(), IniciarCebador.class);
                        startActivityForResult(intent, 0);
                    }
                    else{
                        perfilList.add(p);
                        arrayAdapterPerfil = new CustomListAdaptar(IniciarPerfiles.this, R.layout.activity_custom_list_adaptar, perfilList);
                        listaPerfiles.setAdapter(arrayAdapterPerfil);
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