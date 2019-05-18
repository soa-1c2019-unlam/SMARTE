package com.example.smarteapp2;

public class Perfil {
    String id;
    String yerba;
    String azucar;
    String nombre;

    public Perfil(){

    }

    public Perfil (String id, String nombre, String yerba, String azucar){
        this.id = id;
        this.nombre = nombre;
        this.yerba = yerba;
        this.azucar = azucar;
    }

    public String getId(){ return this.id; }

    public String getYerba(){
        return this.yerba;
    }

    public String getAzucar(){
        return this.azucar;
    }

    public String getNombre(){
        return this.nombre;
    }

    public void setId(String id) {this.id = id;}

    public void setYerba(String yerba){
        this.yerba = yerba;
    }

    public void setAzucar(String azucar){
        this.azucar = azucar;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    @Override
    public String toString(){
        return nombre+" | "+ azucar + " de azúcar";
    }
}
