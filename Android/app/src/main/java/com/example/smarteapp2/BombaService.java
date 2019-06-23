package com.example.smarteapp2;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Service que se inicializa con la app
 * se fija las variables bomba y bombaAplauso en Firebase
 * Si alguna sufre se pone en 1 ya sea por aplauso, sensores o botones de la app
 * acumula en el contador de mates tomados
 */

public class BombaService extends Service {

    private Thread hiloBackground = null;
    
    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;

    List<MatesPorDia> matesPorDiaList = new ArrayList<MatesPorDia>();
    MatesPorDia ultimoMate = new MatesPorDia();
    String fechaDeHoy;

    @Override
    public void onCreate() {
        //region inicializar firebase
        InicializarFirebase firebase = new InicializarFirebase();
        firebase.inicializar(getApplicationContext());
        firebaseDataBase = firebase.getFirebaseDataBase();
        databaseReference = firebase.getDatabaseReference();
        //endregion
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if(hiloBackground == null || !hiloBackground.isAlive()){
            hiloBackground = new Thread(new Runnable() {
                public void run() {

                    databaseReference.child("MatesPorDia").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot objSnapShot : dataSnapshot.getChildren()){
                                MatesPorDia p = new MatesPorDia(objSnapShot.getValue(MatesPorDia.class));
                                matesPorDiaList.add(p);
                            }

                            if (matesPorDiaList.size() > 0){
                                ultimoMate = MatesPorDia.obtenerUltimoMateTomado(matesPorDiaList);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    databaseReference.child("bomba").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if ((Long)dataSnapshot.getValue() == 1) {
                                ultimoMate = MatesPorDia.obtenerUltimoMateTomado(matesPorDiaList);
                                actualizarCantidadDeMates();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    databaseReference.child("bombaAplauso").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if ((Long)dataSnapshot.getValue() == 1){
                                ultimoMate = MatesPorDia.obtenerUltimoMateTomado(matesPorDiaList);
                                actualizarCantidadDeMates();
                            }

                            databaseReference.child("bombaAplauso").setValue(0);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            });

            hiloBackground.start();
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * Metodo que actualiza la cantidad de mates tomadas en el dia.
     * Si ya se tomó algun mate en el dia acumula.
     * Si no se tomó ningun mate en el dia crea un nuevo registro con la fecha actual
     */
    private void actualizarCantidadDeMates(){

        fechaDeHoy = TimePickerFragment.obtenerFechaDeHoy();

        if(ultimoMate != null && Integer.parseInt(ultimoMate.getFecha()) == Integer.parseInt(fechaDeHoy)){
            MatesPorDia mate = new MatesPorDia(ultimoMate.getId(),
                    ultimoMate.getFecha(),
                    ultimoMate.getMates()+1,
                    ultimoMate.getAzucar());
            databaseReference.child("MatesPorDia").child(mate.getId()).setValue(mate);
        }
        else{
            MatesPorDia mate = new MatesPorDia(fechaDeHoy, fechaDeHoy, 1, 0);
            databaseReference.child("MatesPorDia").child(mate.getId()).setValue(mate);
        }
    }
}
