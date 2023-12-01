package com.example.unchazapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.unchazapp.acces.GenericDAO;
import com.example.unchazapp.acces.KeyCallback;
import com.example.unchazapp.model.Usuario;
import com.google.firebase.FirebaseApp;

public class Eliminar extends AppCompatActivity {

    private Usuario us = null;

    private EditText documentoCampo;

    private TextView respuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);
        documentoCampo =  findViewById(R.id.documentoD_EditText);
        respuesta = findViewById(R.id.infoD);
        FirebaseApp.initializeApp(this);
    }

    public void eliminar(View view){
        String documentoAEliminar = documentoCampo.getText().toString();
        GenericDAO<Usuario> usuarioDao =  new GenericDAO<>();
        usuarioDao.key("documento", documentoAEliminar, "usuarios", new KeyCallback() {
            @Override
            public void onKeyFound(Object key) {
                System.out.println("public void eliminar");
                System.out.println("Clave encontrada: " + key);
                usuarioDao.delete(key.toString(), "usuarios", new KeyCallback() {
                    @Override
                    public void onKeyFound(Object key) {
                        respuesta.setText("Usuario eliminado satisfactoriamente");
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
            }
            @Override
            public void onError(String errorMessage) {
                respuesta.setText("Usuario No pudo ser eliminado " + errorMessage);
            }
        });
    }
}