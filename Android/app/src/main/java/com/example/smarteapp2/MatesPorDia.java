package com.example.smarteapp2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MatesPorDia {
    String id;
    String fecha;
    int azucar;
    int mates;

    public MatesPorDia(){
    }

    public MatesPorDia(MatesPorDia matesPorDia){
        this.id = matesPorDia.getId();
        this.fecha = matesPorDia.getFecha();
        this.mates = matesPorDia.getMates();
        this.azucar = matesPorDia.getAzucar();

    }

    public MatesPorDia (String id, String fecha, int mates, int azucar){
        this.id = id;
        this.fecha = fecha;
        this.mates = mates;
        this.azucar = azucar;
    }

    public String getId(){ return id;}

    public int getMates(){
        return mates;
    }

    public String getFecha(){
        return fecha;
    }

    public int getAzucar(){ return azucar; }

    public void setId(String id){
        this.id = id;
    }

    public void setMate(int mates){
        this.mates = mates;
    }

    public void setFecha(String fecha){
        this.fecha = fecha;
    }

    public void setAzucar(int azucar) { this.azucar = azucar ;}

    @Override
    public String toString(){

        String anio = fecha.substring(0,4);
        String mes = fecha.substring(4,6);
        String dia = fecha.substring(6,8);

        if (mates == 1)
            return mates + " mate el día " + dia + "/" + mes + "/" + anio;
        return mates + " mates el día " + dia + "/" + mes + "/" + anio;
    }

    /**
     * metodo que devuelve el ultimo registro de mates tomados, con sus respectivas cantidades.
     * @param matesPorDiaList lista de fechas en firebase
     * @return la ultima fecha que se tomo mate con sus respectivas cantidades.
     */
    public static MatesPorDia obtenerUltimoMateTomado(List<MatesPorDia> matesPorDiaList){
        String fecha = matesPorDiaList.get(matesPorDiaList.size()-1).getFecha();
        int mates = matesPorDiaList.get(matesPorDiaList.size()-1).getMates();
        String id = matesPorDiaList.get(matesPorDiaList.size()-1).getId();
        int azucar = matesPorDiaList.get(matesPorDiaList.size()-1).getAzucar();

        MatesPorDia ultimoMate = new MatesPorDia(id, fecha, mates, azucar);

        return ultimoMate;
    }
}


