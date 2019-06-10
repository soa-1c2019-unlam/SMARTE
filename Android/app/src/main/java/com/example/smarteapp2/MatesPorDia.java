package com.example.smarteapp2;

import java.util.Comparator;
import java.util.Date;

public class MatesPorDia {
    String id;
    String fecha;
    int mates;

    public MatesPorDia(){

    }

    public MatesPorDia(MatesPorDia matesPorDia){
        this.id = matesPorDia.getId();
        this.fecha = matesPorDia.getFecha();
        this.mates = matesPorDia.getMates();

    }

    public MatesPorDia (String id, String fecha, int mates){
        this.id = id;
        this.fecha = fecha;
        this.mates = mates;
    }

    public String getId(){ return id;}

    public int getMates(){
        return mates;
    }

    public String getFecha(){
        return fecha;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setMate(int mates){
        this.mates = mates;
    }

    public void setFecha(String fecha){
        this.fecha = fecha;
    }

    @Override
    public String toString(){

        String anio = fecha.substring(0,4);
        String mes = fecha.substring(4,6);
        String dia = fecha.substring(6,8);

        if (mates == 1)
            return mates + " mate el día " + dia + "/" + mes + "/" + anio;
        return mates + " mates el día " + dia + "/" + mes + "/" + anio;
    }
}


