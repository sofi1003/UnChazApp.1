package com.example.unchazapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.unchazapp.acces.GenericDAO;
import com.example.unchazapp.acces.KeyCallback;
import com.example.unchazapp.model.Usuario;

public class Registro extends AppCompatActivity {

    private Usuario us = null;
    private TextView usuarioInfo;
    private EditText userNameCampo;
    private EditText documentoCampo;
    private EditText passwordCampo;
    private EditText emailCampo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        usuarioInfo = findViewById(R.id.infoRegistro);
        userNameCampo = findViewById(R.id.username_EditText);
        documentoCampo = findViewById(R.id.documento_EditText);
        passwordCampo = findViewById(R.id.correoI_EditText);
        emailCampo = findViewById(R.id.correoI_EditText);
    }
    public void insertar(View view){
        GenericDAO<Usuario> usuarioDao = new GenericDAO<>();
        Usuario usuario = new Usuario();
        usuario.setDocumento(documentoCampo.getText().toString());
        usuario.setEmail(emailCampo.getText().toString());
        usuario.setUserName(userNameCampo.getText().toString());
        usuario.setPassword(passwordCampo.getText().toString());

        usuarioDao.recoverByAttribute("documento", documentoCampo.getText().toString(), "usuarios", new KeyCallback() {
            @Override
            public void onKeyFound(Object key) {
                usuarioInfo.setText("El documento ya está en uso");}
            @Override
            public void onError(String errorMessage) {
                usuarioDao.recoverByAttribute("email", emailCampo.getText().toString(), "usuarios", new KeyCallback() {
                    @Override
                    public void onKeyFound(Object key) {
                        usuarioInfo.setText("El correo ya está en uso");
                    }
                    @Override
                    public void onError(String errorMessage) {
                        usuarioInfo.setText("Se ha registrado correctamente");
                        usuarioDao.save(usuario, "usuarios");
                        Intent i = new Intent(getApplicationContext(), ContinuacionRegistro.class);
                        startActivity(i);



                    }
                });

            }
        });
    }
}