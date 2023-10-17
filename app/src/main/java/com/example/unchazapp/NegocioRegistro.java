package com.example.unchazapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.unchazapp.acces.GenericDAO;
import com.example.unchazapp.model.Catalogo;
import com.example.unchazapp.model.Categoria;
import com.example.unchazapp.model.Negocio;
import com.example.unchazapp.model.Producto;
import com.example.unchazapp.model.TipoDeNegocio;
import com.example.unchazapp.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class NegocioRegistro extends AppCompatActivity {

    private Usuario us = null;
    private TextView registroInfo;
    private EditText nombre;
    private EditText descripcion;
    private Spinner spinnerC;
    private Spinner spinnerT;
    private EditText nombreCatalogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocio_registro);
        nombre = findViewById(R.id.nombreN_editText);
        descripcion = findViewById(R.id.descripcionN_editText);
        nombreCatalogo = findViewById(R.id.nombreC_editText);
        registroInfo = findViewById(R.id.registroNegocioInfo);
        spinnerC = findViewById(R.id.spinner_1);
        String[] categorias = { "ALIMENTACION", "ACCESORIOS", "MANUALIDADES", "MAQUILLAJE", "PAPELERIA", "ROPA","OBJETOS DE ENTRETENIMIENTO" };
        ArrayAdapter<String> adapterC = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias);
        spinnerC.setAdapter(adapterC);
        spinnerT = findViewById(R.id.spinner_2);
        String[] tipo = { "ESTATICO", "MOVIL", };
        ArrayAdapter <String> adapterT = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipo);
        spinnerT.setAdapter(adapterT);
    }
    public void crearNegocio(View view){
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

        if (seleccionadoT.equals("ESTATICO"))
            negocio.setTipoDeNegocio(TipoDeNegocio.ESTATICO);
        else if (seleccionadoC.equals("MOVIL"))
            negocio.setTipoDeNegocio(TipoDeNegocio.MOVIL);

        negocio.setKeyUsuario("-Nf8P2FD-Fu-bLfAG30k");

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

        negocioDao.save(negocio, "negocios");
        registroInfo.setText("El catalogo se almaceno good");
    }
}