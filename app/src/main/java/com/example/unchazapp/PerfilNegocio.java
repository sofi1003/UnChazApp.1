package com.example.unchazapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unchazapp.acces.GenericDAO;
import com.example.unchazapp.acces.KeyCallback;
import com.example.unchazapp.model.Negocio;

import java.util.List;
import java.util.Map;

public class PerfilNegocio extends AppCompatActivity {

    private TextView nombreNegocioo;
    private TextView descripcionNegocio;
    private TextView categoriaNegocio;
    private TextView tipoNegocio;
    private ImageView imagenNegocio;

    private String keyNegocio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_negocio);
        nombreNegocioo = findViewById(R.id.nombre_negocio_perfil);
        descripcionNegocio = findViewById(R.id.descripcion_negocio_perfil);
        categoriaNegocio = findViewById(R.id.categoria_negocio_perfil);
        tipoNegocio = findViewById(R.id.tipo_negocio_perfil);
        imagenNegocio =  findViewById(R.id.imageViewNegocios);
        loadNegociosDataPerfil();
    }

    private void loadNegociosDataPerfil() {
        GenericDAO<Negocio> negocioDao = new GenericDAO<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String nombreNegocio = bundle.getString("nombreNegocio"); // Reemplaza "tuClave" por la clave real que utilizaste
            if (nombreNegocio != null) {
                // Tu cadena se encuentra en "miString", puedes trabajar con ella aquí
                System.out.println("Cadena recibida: " + nombreNegocio);
                negocioDao.recoverByAttribute("nombreNegocio", nombreNegocio, "negocios" ,new KeyCallback() {
                    @Override
                    public void onKeyFound(Object key) {
                        System.out.println("todo esta muy bien mi reina");
                        List<Negocio> listaNegocios = Negocio.toListNegocio((List<Map<String, Object>>) key);
                        Negocio negocio = null;
                        for (Negocio neg: listaNegocios) {

                            negocio = neg;
                            imagenNegocio.setImageResource(R.drawable.imagensita);
                            descripcionNegocio.setText(neg.getDescripcionNegocio());
                            nombreNegocioo.setText(neg.getNombreNegocio());
                            categoriaNegocio.setText(neg.getCategoriaNegocio().toString());
                            tipoNegocio.setText(neg.getTipoDeNegocio().toString());
                        }

                    }

                    @Override
                    public void onError(String errorMessage) {
                        nombreNegocioo.setText("ERROR BEBESITA");
                        System.out.println("error mamor");

                    }
                } );
                negocioDao.key("nombreNegocio", nombreNegocio, "negocios" , new KeyCallback() {
                    @Override
                    public void onKeyFound(Object key) {
                        keyNegocio = key.toString();
                        System.out.println("------------"+ key);
                        System.out.println(keyNegocio);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
            } else {
                System.out.println("La cadena no se encontró en el Bundle");
            }
        } else {
            System.out.println("El bundle es nulo");
        }

}

    public void irACalificacion(View view){
        Intent i = new Intent(this, CalificacionFormulario.class);
        Bundle bundle = new Bundle();
        bundle.putString("keyNegocio", keyNegocio);
        i.putExtras(bundle);
        startActivity(i);
    }
}
