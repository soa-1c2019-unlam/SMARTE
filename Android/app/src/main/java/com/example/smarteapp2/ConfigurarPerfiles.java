package com.example.smarteapp2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.UUID;

public class ConfigurarPerfiles extends AppCompatActivity {

    private List<Perfil> perfilList = new ArrayList<Perfil>();
    ArrayAdapter<Perfil> arrayAdapterPerfil;

    ListView listaPerfiles;
    EditText txtNombre;
    EditText txtYerba;
    EditText txtAzucar;

    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;

    Perfil perfilSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_perfiles);


        listaPerfiles = findViewById(R.id.listaDePerfiles);
        txtAzucar = findViewById(R.id.azucarEditText);
        txtYerba = findViewById(R.id.yerbaEditText);
        txtNombre = findViewById(R.id.nombreEditText);

        inicializarFirebase();

        ListarDatos();

        listaPerfiles.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                perfilSeleccionado = (Perfil) parent.getItemAtPosition(position);
                txtNombre.setText(perfilSeleccionado.getNombre());
                txtYerba.setText(perfilSeleccionado.getYerba());
                txtAzucar.setText(perfilSeleccionado.getAzucar());
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
                    perfilList.add(p);

                    arrayAdapterPerfil = new ArrayAdapter<Perfil>(ConfigurarPerfiles.this, android.R.layout.simple_list_item_1, perfilList);
                    listaPerfiles.setAdapter(arrayAdapterPerfil);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        String nombre = txtNombre.getText().toString();
        String yerba = txtYerba.getText().toString();
        String azucar = txtAzucar.getText().toString();


        switch (item.getItemId()){
            case R.id.icon_add: {
                if (nombre.equals("")){
                    mensajeRequerido(txtNombre);
                }
                else if (yerba.equals("")){
                    mensajeRequerido(txtYerba);
                }
                else if (azucar.equals("")){
                    mensajeRequerido(txtAzucar);
                }
                else{
                    Perfil perfil = new Perfil();
                    perfil.setId(UUID.randomUUID().toString());
                    perfil.setNombre(nombre);
                    perfil.setYerba(yerba);
                    perfil.setAzucar(azucar);

                    databaseReference.child("Perfiles").child(perfil.getId()).setValue(perfil);

                    Toast.makeText(this,"Perfil agregado", Toast.LENGTH_LONG).show();
                    LimpiarTextBox();
                }

                break;
            }
            case R.id.icon_edit: {
                Toast.makeText(this,"Editar perfil", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.icon_save: {
                Perfil p = new Perfil();
                p.setId(perfilSeleccionado.getId());
                p.setNombre(nombre);
                p.setYerba(yerba);
                p.setAzucar(azucar);

                databaseReference.child("Perfiles").child(p.getId()).setValue(p);

                Toast.makeText(this,"Guardado", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.icon_delete: {
                Toast.makeText(this,"Perfil eliminado", Toast.LENGTH_LONG).show();
                break;
            }
        }

        return true;
    }

    private void mensajeRequerido (EditText nombre){
            nombre.setError("Requerido.");
    }

    private void LimpiarTextBox (){
        txtNombre.setText("");
        txtAzucar.setText("");
        txtYerba.setText("");
    }
}
