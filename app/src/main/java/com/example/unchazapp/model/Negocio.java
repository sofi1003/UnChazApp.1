package com.example.unchazapp.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Negocio implements Serializable {

    private String nombreNegocio;
    private Categoria categoriaNegocio;
    private TipoDeNegocio tipoDeNegocio;
    private String descripcionNegocio;
    private String keyUsuario;
    private Catalogo catalogo;
    private Double latitud;
    private Double longitud;

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    private String imagen;

    public Negocio(){

    }

    public Negocio(String nombreNegocio, String descripcionNegocio, String imagen) {
        this.nombreNegocio = nombreNegocio;
        this.descripcionNegocio = descripcionNegocio;
        this.imagen = imagen;
    }

    public Negocio(String nombreNegocio, Categoria categoriaNegocio, TipoDeNegocio tipoDeNegocio, String descripcionNegocio, String keyUsuario, Catalogo catalogo, Double latitud, Double longitud, String imagen) {
        this.nombreNegocio = nombreNegocio;
        this.categoriaNegocio = categoriaNegocio;
        this.tipoDeNegocio = tipoDeNegocio;
        this.descripcionNegocio = descripcionNegocio;
        this.keyUsuario = keyUsuario;
        this.catalogo = catalogo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagen = imagen;
    }

    public Negocio(String nombreNegocio, String descripcionNegocio, String imagen, Categoria categoriaNegocio) {
        this.nombreNegocio = nombreNegocio;
        this.descripcionNegocio = descripcionNegocio;
        this.imagen = imagen;
        this.categoriaNegocio = categoriaNegocio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
    }

    public Categoria getCategoriaNegocio() {
        return categoriaNegocio;
    }

    public void setCategoriaNegocio(Categoria categoriaNegocio) {
        this.categoriaNegocio = categoriaNegocio;
    }

    public TipoDeNegocio getTipoDeNegocio() {
        return tipoDeNegocio;
    }

    public void setTipoDeNegocio(TipoDeNegocio tipoDeNegocio) {
        this.tipoDeNegocio = tipoDeNegocio;
    }

    public String getDescripcionNegocio() {
        return descripcionNegocio;
    }

    public void setDescripcionNegocio(String descripcionNegocio) {
        this.descripcionNegocio = descripcionNegocio;
    }

    public String getKeyUsuario() {
        return keyUsuario;
    }

    public void setKeyUsuario(String keyUsuario) {
        this.keyUsuario = keyUsuario;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public static List<Negocio> toListNegocio(List<Map<String, Object>> lista){
        List<Negocio> listaNegocios = new ArrayList<>();
        for (Map<String, Object> mapa : lista) {
            ObjectMapper objectMapper = new ObjectMapper();
            Negocio negocio = objectMapper.convertValue(mapa, Negocio.class);
            listaNegocios.add(negocio);
        }
        return listaNegocios;
    }

    @Override
    public String toString() {
        return "Negocio{" +
                "nombreNegocio='" + nombreNegocio + '\'' +
                ", categoriaNegocio=" + categoriaNegocio +
                ", tipoDeNegocio=" + tipoDeNegocio +
                ", descripcionNegocio='" + descripcionNegocio + '\'' +
                ", keyUsuario='" + keyUsuario + '\'' +
                ", catalogo=" + catalogo +
                '}';
    }
}
