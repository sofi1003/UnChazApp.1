package com.example.unchazapp;

public class Entidad {

    private String imgFoto;
    private String titulo;
    private String descripcion;
    private String Categoria;

    public Entidad(String imgFoto, String titulo, String descripcion, String categoria) {
        this.imgFoto = imgFoto;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.Categoria = categoria;
    }

    public String getImgFoto() {
        return imgFoto;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return Categoria;
    }
}
