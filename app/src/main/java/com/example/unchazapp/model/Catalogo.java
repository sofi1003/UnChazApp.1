package com.example.unchazapp.model;

import java.io.Serializable;
import java.util.List;

public class Catalogo implements Serializable {

    private String nombre;

    private List<Producto> productos;

    public Catalogo(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "Catalogo{" +
                "nombre='" + nombre + '\'' +
                ", productos=" + productos +
                '}';
    }
}
