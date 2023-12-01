package com.example.unchazapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unchazapp.acces.GenericDAO;
import com.example.unchazapp.acces.KeyCallback;
import com.example.unchazapp.model.Catalogo;
import com.example.unchazapp.model.Categoria;
import com.example.unchazapp.model.Negocio;
import com.example.unchazapp.model.Producto;
import com.example.unchazapp.model.TipoDeNegocio;
import com.example.unchazapp.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Usuario us = null;
    private TextView usuarioInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setTheme(R.style.Theme_UnChazApp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuarioInfo = findViewById(R.id.idUsuario);
        FirebaseApp.initializeApp(this);
    }

    public void irAIngresar(View view){
        Intent i = new Intent(this, Ingresar.class);
        startActivity(i);
    }
    public void irAMapa(View view){
        Intent i = new Intent(this, MapaGeneral.class);
        startActivity(i);
    }

    public void irATodosLosNegocios(View view){
        Intent i = new Intent(this, NegociosCategorias.class);
        startActivity(i);
    }

    public void irAIRecomendar(View view){
        Intent i = new Intent(this, recomendar.class);
        startActivity(i);
    }
    public void irARegistrarNegocio(View view){
        Intent i = new Intent(this, NegocioRegistro.class);
        startActivity(i);
    }

    public void irAActualizar(View view){
        Intent i = new Intent(this, Actualizar.class);
        startActivity(i);
    }

    public void irARegistro(View view){
        Intent i = new Intent(this, Registro.class);
        startActivity(i);
    }
    public void irAEliminar(View view){
        Intent i = new Intent(this, Eliminar.class);
        startActivity(i);
    }

    public void irABuscar(View view){
        Intent i = new Intent(this, BuscarUsuario.class);
        startActivity(i);
    }

    public void irAPRUEBA(View view){
        Intent i = new Intent(this, Prueba.class);
        startActivity(i);
    }


    public void eliminar(View view){
        String documentoAEliminar = "13";
        GenericDAO<Usuario>  usuarioDao =  new GenericDAO<>();
                usuarioDao.key("documento", documentoAEliminar, "usuarios", new KeyCallback() {
                    @Override
                    public void onKeyFound(Object key) {
                        System.out.println("Clave encontrada: " + key);
                        /*if (usuarioDao.delete((String)key, "usuarios")){
                            usuarioInfo.setText("Usuario eliminado satisfactoriamente");
                        }else{
                            usuarioInfo.setText("Usuario No pudo ser eliminado");
                        }*/
                    }
                    @Override
                    public void onError(String errorMessage) {
                        System.out.println(errorMessage);
                    }
                });
    }

    public void listar(View view){
        String documentoABuscar = "14";
        GenericDAO<Usuario> usuarioDao = new GenericDAO<>();
        usuarioDao.recoverAll( "usuarios", new KeyCallback() {
            @Override
            public void onKeyFound(Object key) {
                System.out.println(key);
                List<Usuario> listaUsuarios = Usuario.toListUsuario((List<Map<String, Object>>) key);
                for (Usuario us: listaUsuarios) {
                    System.out.println(us.toString());
                }
                System.out.println("HOLAAAAAAAAAAAAA");
                usuarioInfo.setText(listaUsuarios.toString());
            }
            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
            }
        });
    }

    public void buscarByAttribute(View view){
        String documentoABuscar = "nombrre1";
        GenericDAO<Usuario> usuarioDao = new GenericDAO<>();
        usuarioDao.recoverByAttribute("userName", documentoABuscar, "usuarios", new KeyCallback() {
            @Override
            public void onKeyFound(Object key) {
                System.out.println(key);
                List<Usuario> listaUsuarios = Usuario.toListUsuario((List<Map<String, Object>>) key);
                System.out.println("El primer elemenro de la lista es: " + listaUsuarios.get(0));
                for (Usuario us: listaUsuarios) {
                    usuarioInfo.setText(us.getUserName());
                    System.out.println(us.toString());
                }
                //usuarioInfo.setText(key.toString());
            }
            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
            }
        });
    }

    public void buscarLaLLave(View view){
        String documentoABuscar = "12";
        GenericDAO<Usuario> usuarioDao = new GenericDAO<>();
        usuarioDao.key("documento", documentoABuscar, "usuarios", new KeyCallback() {
            @Override
            public void onKeyFound(Object key) {
                System.out.println("Clave encontrada: " + key);
                usuarioInfo.setText("Key:: " + key);
                System.out.println("KEYYYYYYYYYYYYYYYYY" + key);
            }
            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
            }
        });
    }
        public void actualizar(View view){
            String documentoAActualizar = "141";
            GenericDAO<Usuario> usuarioDao = new GenericDAO<>();
            usuarioDao.recoverByAttribute("documento", documentoAActualizar, "usuarios", new KeyCallback() {
                @Override
                public void onKeyFound(Object key) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<Usuario> listaUsuarios = Usuario.toListUsuario((List<Map<String, Object>>) key);
                    Usuario usuario = listaUsuarios.get(0);
                    usuario.setPassword("NUevo passssword");
                    usuario.setUserName("NUevo username");
                    Map<String, Object> updatedValues = objectMapper.convertValue(usuario, Map.class);
                    usuarioDao.updateByAttribute("documento", usuario.getDocumento(),"usuarios" , updatedValues, new KeyCallback() {
                        @Override
                        public void onKeyFound(Object key) {
                            usuarioInfo.setText("Uusario actualizado correctamente:: " + key );
                            System.out.println("-----------------------" + key);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            usuarioInfo.setText("Uusario actualizado correctamente:: " + errorMessage);
                        }
                    });
                    }
                @Override
                public void onError(String errorMessage) {
                    usuarioInfo.setText("Uusario actualizado correctamente:: qqq" + errorMessage);
                }
            });
        }

    public void crearNegocio(View view){
        GenericDAO<Negocio> negocioDao = new GenericDAO<>();

        Negocio negocio = new Negocio();
        negocio.setNombreNegocio("papitas pepito perez");
        negocio.setDescripcionNegocio("ricas");
        negocio.setCategoriaNegocio(Categoria.ACCESORIOS);
        negocio.setTipoDeNegocio(TipoDeNegocio.ESTATICO);
        negocio.setKeyUsuario("-Nf8P2FD-Fu-bLfAG30k");

        Catalogo catalogo =  new Catalogo();
        Producto producto = new Producto();
        catalogo.setNombre("catalogo de papita");

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
        usuarioInfo.setText("El usuario se almaceno good");
    }



}