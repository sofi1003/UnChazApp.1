package com.example.unchazapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.unchazapp.acces.GenericDAO;
import com.example.unchazapp.acces.KeyCallback;
import com.example.unchazapp.model.Negocio;
import com.example.unchazapp.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class INICIO extends AppCompatActivity {

    private TextView usuarioInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        usuarioInfo = findViewById(R.id.recomendacionesNegocios);
    }

    public void mostrarRecomendaciones(View view) throws JSONException {
        System.out.println("mostrarRecomendaciones");
        usuarioInfo.setText("");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            System.out.println();
            // Convertir la cadena JSON en un objeto JSON
            JSONObject objetoJSON = new JSONObject(bundle.getString("recomendaciones"));

            // Acceder a la lista de recomendaciones
            JSONArray recomendaciones = objetoJSON.getJSONArray("recomendaciones");
            GenericDAO<Negocio> negovioDAO = new GenericDAO<Negocio>();
            // Iterar a través de la lista de recomendaciones

            for (int i = 0; i < recomendaciones.length(); i++) {
                String recomendacion = recomendaciones.getString(i);
                System.out.println("---------" + i + recomendacion);

                negovioDAO.recoverByKey(recomendacion, "negocios", new KeyCallback() {
                    @Override
                    public void onKeyFound(Object key) {
                        System.out.println("hasta aqui bien");
                        System.out.println(key);
                        ObjectMapper objectMapper = new ObjectMapper();

                        Negocio negocio = objectMapper.convertValue(key, Negocio.class);
                        System.out.println("AQUI SE TOTEA");
                        System.out.println(negocio);
                        usuarioInfo.setText(usuarioInfo.getText() + negocio.getNombreNegocio() + " - " + negocio.getTipoDeNegocio() + "\n");
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
            }
            //System.out.println(cad);


        }
    }

    public void mostrarRecomendaciones1(View view) throws JSONException {
        System.out.println("mostrarRecomendaciones");
        GenericDAO<Negocio> negovioDAO = new GenericDAO<Negocio>();
        negovioDAO.recoverByKey("-Ngo-zkyHpn_yKIfBhHh", "negocios", new KeyCallback() {
            @Override
            public void onKeyFound(Object key) {
                System.out.println(key);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

                Negocio negocio = objectMapper.convertValue(key, Negocio.class);

                System.out.println(negocio);

            }

            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
            }
        });
    }

    public void irATodosLosNegocios(View view){
        Intent i = new Intent(this, NegociosCategorias.class);
        startActivity(i);
    }




}