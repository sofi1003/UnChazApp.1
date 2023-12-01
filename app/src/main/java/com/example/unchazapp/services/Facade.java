package com.example.unchazapp.services;


import com.example.unchazapp.model.Usuario;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Facade {

    public void insertar(){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        Usuario usuario = new Usuario();
        usuario.setDocumento("12");
        usuario.setEmail("12");
        usuario.setUserName("12");
        usuario.setPassword("12");

        databaseRef.child("usuarios").push().setValue(usuario);

    }

}
