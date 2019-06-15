package com.example.smarteapp2;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minutos = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener)getActivity(), hora, minutos, android.text.format.DateFormat.is24HourFormat(getActivity())) ;
    }

    /**
     * Metodo que devuelve la fecha actual en string
     * formato del return: ddmmaaaa
     * @return
     */
    public static String obtenerFechaDeHoy(){
        String fechaDeHoy;

        LocalDateTime dateHoy = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));

        fechaDeHoy = dateHoy.toString();
        fechaDeHoy = fechaDeHoy.split("-")[0]+
                fechaDeHoy.split("-")[1]+
                fechaDeHoy.split("-")[2].split("T")[0];

        return fechaDeHoy;
    }
}
