package com.example.unchazapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ContinuacionRegistro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuacion_registro);
    }

    public void irARegistrarNegocio(View view){
        Intent i = new Intent(this, NegocioRegistro.class);
        startActivity(i);
    }
    public void irAInicio(View view){
        Intent i = new Intent(this, INICIO.class);
        startActivity(i);
    }

}