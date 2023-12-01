package com.example.unchazapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.unchazapp.acces.GenericDAO;
import com.example.unchazapp.acces.KeyCallback;
import com.example.unchazapp.model.Negocio;
import com.example.unchazapp.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class PerfilUsuario extends AppCompatActivity {
    private String keyUsuario;
    private TextView saludo;
    private TextView nombre;

    private TextView correo;

    private LinearLayout irA;
    private LinearLayout store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        saludo = findViewById(R.id.Hola);
        nombre = findViewById(R.id.Nombre);
        correo = findViewById(R.id.Correo);
        irA = findViewById(R.id.store);
        store = findViewById(R.id.irA);


        SharedPreferences sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
        keyUsuario = sharedPreferences.getString("keyUsuario", "ValorPredeterminado");
        loadNegociosDataPerfil();

    }

    private void loadNegociosDataPerfil() {
        GenericDAO<Usuario> usuarioDao = new GenericDAO<>();

        usuarioDao.recoverByKey(keyUsuario, "usuarios", new KeyCallback() {
            @Override
            public void onKeyFound(Object key) {
                System.out.println("Todo está muy bien, mi reina");
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

                Usuario usuario = objectMapper.convertValue(key, Usuario.class);

                nombre.setText("Nombre de usuario: " + usuario.getUserName());
                correo.setText("Correo asociado: " + usuario.getEmail());
                saludo.setText("HOLA" + usuario.getUserName());

                GenericDAO<Negocio> negocioDao = new GenericDAO<>();

                negocioDao.recoverByAttribute("keyUsuario", keyUsuario, "negocios", new KeyCallback() {
                    @Override
                    public void onKeyFound(Object key) {
                        System.out.println("FUCKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK" + key);
                        irA.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        store.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Manejar el error si es necesario
            }
        });
    }









    public void irARegistrarNegocio(View view){
        Intent i = new Intent(this, NegocioRegistro.class);
        startActivity(i);
    }

    public void irAManejarNegocio(View view){
        Intent i = new Intent(this, ManejarNegocio.class);
        startActivity(i);
    }
}