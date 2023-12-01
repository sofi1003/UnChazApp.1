package com.example.unchazapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Prueba extends AppCompatActivity {
    private static final int SELECCIONAR_IMAGEN = 1;

    private ImageView imageView;
    private Uri imagenUri;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        imageView = findViewById(R.id.imageView1);
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Subiendo imagen...");

        // Evento clic para seleccionar una imagen
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarImagen();
            }
        });
    }

    private void seleccionarImagen() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), SELECCIONAR_IMAGEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECCIONAR_IMAGEN && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagenUri = data.getData();
            imageView.setImageURI(imagenUri);
        }
    }

    public void subirImagen(View view) {
        if (imagenUri != null) {
            progressDialog.show();

            // Ruta de almacenamiento en Firebase Storage (puedes cambiar "imagenes" por la carpeta que desees)
            StorageReference referenciaImagen = storageReference.child("imagenes/" + System.currentTimeMillis()); // Usamos un nombre único para cada imagen

            referenciaImagen.putFile(imagenUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        Toast.makeText(Prueba.this, "Imagen subida con éxito", Toast.LENGTH_SHORT).show();

                        // Obtener la URL de la imagen
                        obtenerUrlImagen(referenciaImagen);
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(Prueba.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Selecciona una imagen primero", Toast.LENGTH_SHORT).show();
        }
    }


    private void obtenerUrlImagen(StorageReference referenciaImagen) {
        referenciaImagen.getDownloadUrl().addOnSuccessListener(uri -> {
            // Aquí puedes usar la URL de la imagen como desees
            String urlImagen = uri.toString();
            Toast.makeText(Prueba.this, "URL de la imagen: " + urlImagen, Toast.LENGTH_SHORT).show();
        });
    }
}
