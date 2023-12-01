package com.example.unchazapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unchazapp.acces.GenericDAO;
import com.example.unchazapp.model.Catalogo;
import com.example.unchazapp.model.Categoria;
import com.example.unchazapp.model.Negocio;
import com.example.unchazapp.model.Producto;
import com.example.unchazapp.model.TipoDeNegocio;
import com.example.unchazapp.model.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class NegocioRegistro extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private Usuario us = null;
    private GoogleMap mMap;
    private EditText nombre;
    private EditText descripcion;
    private Spinner spinnerC;
    private Spinner spinnerT;
    private EditText nombreCatalogo;

    private Double latitud;
    private Double longitud;
    private Marker negocioMarker;

    private FrameLayout mapContainer;
    private TextView textoUbi;

    //imagen
    private static final int SELECCIONAR_IMAGEN = 1;

    private ImageView imageView;
    private Uri imagenUri;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    String urlImagen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocio_registro);
        imageView = findViewById(R.id.imageViewNegocio);
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

        nombre = findViewById(R.id.nombreN_editText);
        mapContainer = findViewById(R.id.mapContainer);
        textoUbi = findViewById(R.id.textoUbi);
        descripcion = findViewById(R.id.descripcionN_editText);
        nombreCatalogo = findViewById(R.id.nombreC_editText);


        spinnerC = findViewById(R.id.spinner_1);
        String[] categorias = { "ALIMENTACION", "ACCESORIOS", "MANUALIDADES", "MAQUILLAJE", "PAPELERIA", "ROPA","OBJETOS DE ENTRETENIMIENTO" };
        ArrayAdapter<String> adapterC = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias);
        spinnerC.setAdapter(adapterC);

        spinnerT = findViewById(R.id.spinner_2);
        String[] tipo = { "ESTATICO", "MOVIL", };
        ArrayAdapter <String> adapterT = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipo);
        spinnerT.setAdapter(adapterT);

        spinnerT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String seleccionadoT = spinnerT.getSelectedItem().toString();
                if (seleccionadoT.equals("ESTATICO"))
                    mostrarMapa();
                else if (seleccionadoT.equals("MOVIL"))
                    ocultarMapa();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
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
                        Toast.makeText(NegocioRegistro.this, "Imagen subida con éxito", Toast.LENGTH_SHORT).show();

                        // Obtener la URL de la imagen
                        obtenerUrlImagen(referenciaImagen);
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(NegocioRegistro.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Selecciona una imagen primero", Toast.LENGTH_SHORT).show();
        }

    }


    private void obtenerUrlImagen(StorageReference referenciaImagen) {
        referenciaImagen.getDownloadUrl().addOnSuccessListener(uri -> {
            // Aquí puedes usar la URL de la imagen como desees
            urlImagen = uri.toString();
            Toast.makeText(NegocioRegistro.this, "URL de la imagen: " + urlImagen, Toast.LENGTH_SHORT).show();
        });

    }

    public void crearNegocio(View view){
        int count = 0;
        String seleccionadoC = spinnerC.getSelectedItem().toString();
        String seleccionadoT = spinnerT.getSelectedItem().toString();
        GenericDAO<Negocio> negocioDao = new GenericDAO<>();

        Negocio negocio = new Negocio();
        negocio.setNombreNegocio(nombre.getText().toString());
        negocio.setDescripcionNegocio(descripcion.getText().toString());
        if (seleccionadoC.equals("ALIMENTACION"))
            negocio.setCategoriaNegocio(Categoria.ALIMENTACION);
        else if (seleccionadoC.equals("ACCESORIOS"))
            negocio.setCategoriaNegocio(Categoria.ACCESORIOS);
        else if(seleccionadoC.equals("MANUALIDADES"))
            negocio.setCategoriaNegocio(Categoria.MANUALIDADES);
        else if(seleccionadoC.equals("MAQUILLAJE"))
            negocio.setCategoriaNegocio(Categoria.MAQUILLAJE);
        else if(seleccionadoC.equals("PAPELERIA"))
            negocio.setCategoriaNegocio(Categoria.PAPELERIA);
        else if(seleccionadoC.equals("ROPA"))
            negocio.setCategoriaNegocio(Categoria.ROPA);
        else if(seleccionadoC.equals("OBJETOS DE ENTRETENIMIENTO"))
            negocio.setCategoriaNegocio(Categoria.OBJETOS_ENTRETENIMIENTO);

        if (seleccionadoT.equals("ESTATICO")){
            negocio.setTipoDeNegocio(TipoDeNegocio.ESTATICO);
            if (latitud ==null && longitud==null){
                mostrarDialogoBasico();
                count+=1;

            }
            else{
                negocio.setLatitud(latitud);
                negocio.setLongitud(longitud);
            }


        }
        else if (seleccionadoT.equals("MOVIL")){
            negocio.setTipoDeNegocio(TipoDeNegocio.MOVIL);
            negocio.setLatitud(1.0);
            negocio.setLongitud(1.0);
        }

        SharedPreferences preferences = getSharedPreferences("keyDelRegistrado", MODE_PRIVATE);
        String keyUsuarioRegistrado = preferences.getString("key", "valorPredeterminado");

        SharedPreferences sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
        String keyUsuario = sharedPreferences.getString("keyUsuario", "ValorPredeterminado");
        if (preferences != null){
            negocio.setKeyUsuario(keyUsuarioRegistrado);
        }else if(sharedPreferences!= null){
            negocio.setKeyUsuario(keyUsuario);
        }


        Catalogo catalogo =  new Catalogo();
        Producto producto = new Producto();
        catalogo.setNombre(nombreCatalogo.getText().toString());

        List<Producto> productos = new ArrayList<>();

        Producto producto1 = new Producto();
        producto1.setNombreProducto("P1");
        producto1.setPrecioProducto(1000D);
        producto1.setDescripcionProducto("descrip P1");
        productos.add(producto1);

        Producto producto2 = new Producto();
        producto2.setNombreProducto("P2");
        producto2.setPrecioProducto(2000D);
        producto2.setDescripcionProducto("descrip P2");
        productos.add(producto2);

        catalogo.setProductos(productos);


        negocio.setCatalogo(catalogo);
        if (urlImagen.length()>10){
            negocio.setImagen(urlImagen);
        }else{
            negocio.setImagen("sinImagen");
        }


        System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUUUUUU " + urlImagen);

        if (count!=0){
            Toast.makeText(getApplicationContext(), "No se pudo guardar los datos", Toast.LENGTH_SHORT);
        }
        else{
            if (negocio.getImagen().length() != 0){
                negocioDao.save(negocio, "negocios");
                irAInicio();
            }
            else{
                Toast.makeText(getApplicationContext(), "No se pudo guardar los datos", Toast.LENGTH_SHORT);

            }



        }
        System.out.println("keyDEL REGISTARDO " + negocio.getKeyUsuario() );

    }
    private void irAInicio(){
        Intent i = new Intent(getApplicationContext(), INICIO.class);
        startActivity(i);
    }

    private void mostrarMapa(){
        textoUbi.setVisibility(View.VISIBLE);
        mapContainer.setVisibility(View.VISIBLE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLngBounds zonaPermitida = new LatLngBounds(
                new LatLng(4.632037316971541, -74.08961060988099), // Esquina inferior izquierda
                new LatLng(4.643198230686855, -74.07868614743685)  // Esquina superior derecha
        );

        mMap.setLatLngBoundsForCameraTarget(zonaPermitida);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(zonaPermitida, 0));
        mMap.setMinZoomPreference(15);
        this.mMap.setOnMapClickListener(this);

    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if (negocioMarker!=null){
            negocioMarker.remove();
        }
        String titulo= "";
        if (nombre.length()==0)
            titulo = "Tu negocio";
        else{
            titulo = nombre.getText().toString();
        }

        latitud = latLng.latitude;
        longitud = latLng.longitude;
        LatLng nacho = new LatLng(latitud, longitud);
        negocioMarker = mMap.addMarker(new MarkerOptions()
                .position(nacho)
                .title(titulo));
    }
    private void ocultarMapa(){
        mapContainer.setVisibility(View.GONE);
    }

    private void mostrarDialogoBasico(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NegocioRegistro.this);
        builder.setTitle("Error");
        builder.setMessage("Debes escoger una ubicación para poder registrar tu negocio")
                .setPositiveButton("Vale", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setCancelable(false)
                .show();
    }

}



