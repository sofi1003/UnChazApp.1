package com.example.unchazapp.model;

public class Calificacion {

    private int calificacion;
    private String keyNegocio;
    private String keyUser;
    private Categoria categoria;


    public Calificacion() {
    }

    public Calificacion(int calificacion, String keyNegocio, String keyUser, Categoria categoria) {
        this.calificacion = calificacion;
        this.keyNegocio = keyNegocio;
        this.keyUser = keyUser;
        this.categoria = categoria;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getKeyNegocio() {
        return keyNegocio;
    }

    public void setKeyNegocio(String keyNegocio) {
        this.keyNegocio = keyNegocio;
    }

    public String getKeyUser() {
        return keyUser;
    }

    public void setKeyUser(String keyUser) {
        this.keyUser = keyUser;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Calificacion{" +
                "calificacion=" + calificacion +
                ", keyNegocio='" + keyNegocio + '\'' +
                ", keyUser='" + keyUser + '\'' +
                ", categoria=" + categoria +
                '}';
    }
}
