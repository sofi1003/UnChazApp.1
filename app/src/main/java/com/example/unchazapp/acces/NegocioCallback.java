package com.example.unchazapp.acces;

import com.example.unchazapp.model.Negocio;

import java.util.List;

public interface NegocioCallback {

    void onNegociosLoaded(List<Negocio> listaNegocios);
}
