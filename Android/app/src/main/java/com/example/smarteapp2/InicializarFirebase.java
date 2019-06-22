package com.example.smarteapp2;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InicializarFirebase  {

    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;

    public void inicializar(Context context) {
        FirebaseApp.initializeApp(context);
        firebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDataBase.getReference();
    }


    public FirebaseDatabase getFirebaseDataBase()
    {
        return firebaseDataBase;
    }

    public DatabaseReference getDatabaseReference()
    {
        return databaseReference;
    }
}
