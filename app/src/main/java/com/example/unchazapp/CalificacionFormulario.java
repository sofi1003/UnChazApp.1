package com.example.unchazapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unchazapp.acces.GenericDAO;
import com.example.unchazapp.acces.KeyCallback;
import com.example.unchazapp.model.Calificacion;
import com.example.unchazapp.model.Categoria;
import com.example.unchazapp.model.Negocio;

import java.util.List;
import java.util.Map;

public class CalificacionFormulario extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView nombreNegocio;
    private TextView descripcionNegocio;
    private int calificacion;
    private String keyNegocio;

    private Categoria categoriaNegocio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificacion);
        ratingBar = findViewById(R.id.ratingBarNegocio);
        nombreNegocio = findViewById(R.id.nombre_negocio_calificacion);
        descripcionNegocio = findViewById(R.id.descripcion_negocio_calificacion);
        loadNegociosDataCalificacion();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                calificacion = (int)rating;
                Toast.makeText(getApplicationContext(), "Calificación: " + calificacion, Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void loadNegociosDataCalificacion() {
        GenericDAO<Negocio> negocioDao = new GenericDAO<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            keyNegocio = bundle.getString("keyNegocio"); // Reemplaza "tuClave" por la clave real que utilizaste
            if (keyNegocio != null) {

                System.out.println("Cadena recibida: " + keyNegocio);
                negocioDao.recoverByKey(keyNegocio, "negocios" ,new KeyCallback() {
                    @Override
                    public void onKeyFound(Object key) {
                        System.out.println("todo esta muy bien mi reina");
                        List<Negocio> listaNegocios = Negocio.toListNegocio((List<Map<String, Object>>) key);
                        Negocio negocio = null;
                        for (Negocio neg: listaNegocios) {
                            negocio = neg;
                            nombreNegocio.setText(neg.getNombreNegocio());
                            descripcionNegocio.setText(neg.getDescripcionNegocio());
                            CalificacionFormulario.this.categoriaNegocio = neg.getCategoriaNegocio();

                        }

                    }

                    @Override
                    public void onError(String errorMessage) {

                        System.out.println("error mamor");
                        System.out.println(errorMessage);

                    }
                } );
            } else {
                System.out.println("La cadena no se encontró en el Bundle");
            }
        } else {
            System.out.println("El bundle es nulo");
        }

    }
    private void enviarCalificacion(View view){
        GenericDAO<Calificacion> calificacionDao = new GenericDAO<>();
        Calificacion calificacion = new Calificacion();
        calificacion.setKeyNegocio(this.keyNegocio);
        calificacion.setCalificacion(this.calificacion);
        calificacion.setCategoria(this.categoriaNegocio);




    }
}