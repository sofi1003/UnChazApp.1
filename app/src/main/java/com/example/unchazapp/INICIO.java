package com.example.unchazapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.unchazapp.acces.GenericDAO;
import com.example.unchazapp.acces.KeyCallback;
import com.example.unchazapp.model.Negocio;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class INICIO extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;

    ListView ListViewRecomendaciones;
    AdaptadorRecomendaciones adaptadorRecomendaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ListViewRecomendaciones = findViewById(R.id.ListViewRecomendaciones);
        ListViewRecomendaciones.setOnItemClickListener((adapterView, view, position, id) -> {
            // Obtener la entidad seleccionada
            Entidad entidadSeleccionada = (Entidad) adapterView.getItemAtPosition(position);


            Intent intent = new Intent(getApplicationContext(), PerfilNegocio.class);
            Bundle bundle = new Bundle();
            bundle.putString("nombreNegocio", entidadSeleccionada.getTitulo());
            intent.putExtras(bundle);
            startActivity(intent);
        });

        try {
            mostrarRecomendaciones();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        if (savedInstanceState == null) {
            // Verificar si ya estamos en la actividad de inicio
            if (!getClass().equals(INICIO.class)) {
                // Si no estamos en la actividad de inicio, iniciarla
                Intent iHome = new Intent(this, INICIO.class);
                startActivity(iHome);
            }
            navigationView.setCheckedItem(R.id.nav_home);
        }




        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.homeBM) {
                replaceFragment(INICIO.class);
            } else if (item.getItemId() == R.id.buscarBM) {
                replaceFragment(NegociosCategorias.class);
            } else if (item.getItemId() == R.id.mapaBM) {
                replaceFragment(MapaGeneral.class);
            } else if (item.getItemId() == R.id.perfilBM) {
                replaceFragment(PerfilUsuario.class);
            }

            return true;
        });




    }
    private  void replaceFragment(Class<?> cls) {
        Intent i = new Intent(this, cls);
        startActivity(i);
    }







    public void mostrarRecomendaciones() throws JSONException {
        ArrayList<Entidad> listRecomendaciones = new ArrayList<>();
        System.out.println("mostrarRecomendaciones");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            System.out.println();
            // Convertir la cadena JSON en un objeto JSON
            JSONObject objetoJSON = new JSONObject(bundle.getString("recomendaciones"));

            // Acceder a la lista de recomendaciones
            JSONArray recomendaciones = objetoJSON.getJSONArray("recomendaciones");
            GenericDAO<Negocio> negovioDAO = new GenericDAO<Negocio>();
            // Iterar a través de la lista de recomendaciones

            for (int i = 0; i < recomendaciones.length(); i++) {
                String recomendacion = recomendaciones.getString(i);
                System.out.println("---------" + i + recomendacion);

                negovioDAO.recoverByKey(recomendacion, "negocios", new KeyCallback() {
                    @Override
                    public void onKeyFound(Object key) {
                        System.out.println("hasta aqui bien");
                        System.out.println(key);
                        ObjectMapper objectMapper = new ObjectMapper();

                        Negocio negocio = objectMapper.convertValue(key, Negocio.class);
                        System.out.println("AQUI SE TOTEA");
                        System.out.println(negocio);
                        Entidad entidad = new Entidad(negocio.getImagen(), negocio.getNombreNegocio(), negocio.getDescripcionNegocio(), negocio.getCategoriaNegocio().toString());
                        listRecomendaciones.add(entidad);
                        System.out.println("mierde puteeee" + entidad.getCategoria() + entidad.getDescripcion() + entidad.getTitulo());

                        if (listRecomendaciones.size() == recomendaciones.length()) {
                            // Todas las recomendaciones han sido procesadas, configurar el adaptador y ListView
                            adaptadorRecomendaciones = new AdaptadorRecomendaciones(listRecomendaciones, INICIO.this);
                            ListViewRecomendaciones.setAdapter(adaptadorRecomendaciones);
                    }}

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
            }




        }

    }

    public void mostrarRecomendaciones1(View view) throws JSONException {
        System.out.println("mostrarRecomendaciones");
        GenericDAO<Negocio> negovioDAO = new GenericDAO<Negocio>();
        negovioDAO.recoverByKey("-Ngo-zkyHpn_yKIfBhHh", "negocios", new KeyCallback() {
            @Override
            public void onKeyFound(Object key) {
                System.out.println(key);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

                Negocio negocio = objectMapper.convertValue(key, Negocio.class);

                System.out.println(negocio);

            }

            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
            }
        });
    }

    public void irATodosLosNegocios(View view){
        Intent i = new Intent(this, NegociosCategorias.class);
        startActivity(i);
    }
    public void irAMapaGeneral(View view){
        Intent i = new Intent(this, MapaGeneral.class);
        startActivity(i);
    }
    public void irAPerfil(View view){
        Intent i = new Intent(this, PerfilUsuario.class);
        startActivity(i);
    }
    public void irABuscar(View view){
        Intent i = new Intent(this, NegociosCategorias.class);
        startActivity(i);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (item.getItemId() == R.id.nav_home) {
            replaceFragment(INICIO.class);
        } else if (item.getItemId() == R.id.nav_switch) {
            replaceFragment(NegociosCategorias.class);
        } else if (item.getItemId() == R.id.nav_account) {
            replaceFragment(MapaGeneral.class);
        } else if (item.getItemId() == R.id.nav_negocio) {
            replaceFragment(PerfilUsuario.class);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }
}