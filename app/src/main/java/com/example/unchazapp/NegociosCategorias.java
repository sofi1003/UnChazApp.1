package com.example.unchazapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.unchazapp.acces.GenericDAO;
import com.example.unchazapp.acces.KeyCallback;
import com.example.unchazapp.acces.NegocioCallback;
import com.example.unchazapp.model.Negocio;
import com.example.unchazapp.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NegociosCategorias extends AppCompatActivity {
    ListView ListViewContacto;
    List<Negocio> lst;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocios_categorias);

        ListViewContacto = findViewById(R.id.ListViewContacto);
        loadNegociosData(new NegocioCallback() {
            @Override
            public void onNegociosLoaded(List<Negocio> listaNegocios) {
                lst = listaNegocios;
                CustomAdapter adapter = new CustomAdapter(NegociosCategorias.this, lst);
                ListViewContacto.setAdapter(adapter);

            }
        });

        ListViewContacto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Negocio c = lst.get(i);
                Intent intent = new Intent(getApplicationContext(), PerfilNegocio.class);
                Bundle bundle = new Bundle();
                bundle.putString("nombreNegocio", c.getNombreNegocio());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


    }

    private void loadNegociosData(NegocioCallback callback) {
        GenericDAO<Negocio> negocioDao = new GenericDAO<>();
        negocioDao.recoverAll("negocios", new KeyCallback() {
            @Override
            public void onKeyFound(Object key) {
                System.out.println(key);
                List<Negocio> listaNegocios = Negocio.toListNegocio((List<Map<String, Object>>) key);
                List<Negocio> lst = new ArrayList<>();

                for (Negocio negoc : listaNegocios) {
                    Negocio negocio = new Negocio(negoc.getNombreNegocio(), negoc.getDescripcionNegocio(), R.drawable.imagensita);
                    System.out.println("---------------------esto debería ir primero---------------");
                    System.out.println(negocio.toString());
                    lst.add(negocio);
                }

                // Llama al callback con la lista de negocios una vez que estén disponibles.
                callback.onNegociosLoaded(lst);
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
            }
        });
    }







}