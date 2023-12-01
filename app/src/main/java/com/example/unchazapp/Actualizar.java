package com.example.unchazapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.unchazapp.acces.GenericDAO;
import com.example.unchazapp.acces.KeyCallback;
import com.example.unchazapp.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;

import java.util.List;
import java.util.Map;

public class Actualizar extends AppCompatActivity {

    private Usuario us = null;
    private TextView respuesta;
    private EditText userNameCampo;
    private EditText documentoCampo;
    private EditText passwordCampo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);
        respuesta = findViewById(R.id.infoU);
        userNameCampo = findViewById(R.id.userNameU_EditText);
        documentoCampo = findViewById(R.id.documentoU_EditText);
        passwordCampo = findViewById(R.id.contraseñaU_EditText);
        FirebaseApp.initializeApp(this);
    }
    public void actualizar(View view){
        String documentoAActualizar = documentoCampo.getText().toString();
        System.out.println(documentoAActualizar);
        GenericDAO<Usuario> usuarioDao = new GenericDAO<>();
        usuarioDao.recoverByAttribute("documento", documentoAActualizar, "usuarios", new KeyCallback() {
            @Override
            public void onKeyFound(Object key) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Usuario> listaUsuarios = Usuario.toListUsuario((List<Map<String, Object>>) key);

                Usuario usuario = listaUsuarios.get(0);
                usuario.setPassword(passwordCampo.getText().toString());
                usuario.setUserName(userNameCampo.getText().toString());
                Map<String, Object> updatedValues = objectMapper.convertValue(usuario, Map.class);
                usuarioDao.updateByAttribute("documento", usuario.getDocumento(),"usuarios" , updatedValues, new KeyCallback() {
                    @Override
                    public void onKeyFound(Object key) {
                        respuesta.setText("Uusario actualizado correctamente:: " + key );
                        System.out.println(key);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        respuesta.setText("Usuario no actualizado correctamente " + errorMessage);
                    }
                });
            }
            @Override
            public void onError(String errorMessage) {
                respuesta.setText("Usuario no actualizado correctamente " + errorMessage);
            }
        });
    }

    public static class Calificacion extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_calificacion);
        }
    }
}