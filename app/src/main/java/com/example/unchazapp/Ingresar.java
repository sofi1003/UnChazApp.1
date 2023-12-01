package com.example.unchazapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.unchazapp.acces.GenericDAO;
import com.example.unchazapp.acces.KeyCallback;
import com.example.unchazapp.model.Usuario;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Ingresar extends AppCompatActivity {

    private EditText passwordCampo;
    private EditText emailCampo;
    private TextView usuarioInfo;

    private String recomendaciones;

    private String keyUsuario;

    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);
        passwordCampo = findViewById(R.id.passwordI_EditText);
        emailCampo = findViewById(R.id.correoI_EditText);
        usuarioInfo = findViewById(R.id.infoRegistro);
    }
    public void ingresar(View view){
        System.out.println("Ingresar.ingresar");
        String emailABuscar = emailCampo.getText().toString();
        GenericDAO<Usuario> usuarioDao = new GenericDAO<>();
        usuarioDao.recoverByAttribute("email", emailABuscar, "usuarios", new KeyCallback() {
            @Override
            public void onKeyFound(Object key) {
                System.out.println("Ingresar-onKeyFound");
                System.out.println(key);
                List<Usuario> listaUsuarios = Usuario.toListUsuario((List<Map<String, Object>>) key);
                Usuario usuario = null;
                for (Usuario us: listaUsuarios) {
                    System.out.println(us.getPassword());
                    usuario = us;
                    password = us.getPassword();
                }
                if (password.equals(passwordCampo.getText().toString())){

                    GenericDAO<Usuario> usuarioDao = new GenericDAO<>();
                    usuarioDao.key("email", emailABuscar,"usuarios",  new KeyCallback(){
                        @Override
                        public void onKeyFound(Object key) {
                            keyUsuario = key.toString();
                            key = key;
                            ObtenerRecomendacionesTask1 recomendar = new ObtenerRecomendacionesTask1();
                            recomendar.setJsonRequest(key.toString());
                            recomendar.execute();

                        }
                        @Override
                        public void onError(String errorMessage) {
                            errorMessage = "Error al consultar las recomendaciones.";
                        }
                    });

                }
                else{
                    usuarioInfo.setText("Contraseña incorrecta");
                }
            }

            @Override
            public void onError(String errorMessage) {
                usuarioInfo.setText("No existe cuenta asociada a ese correo electronico");
            }
        });
    }


    //aca la clase privada.

    private class ObtenerRecomendacionesTask1 extends AsyncTask<Void, Void, String> {

        private String jsonRequest; // Reemplaza 1 con el ID del cliente
        private static final String TAG = "ApiService";

        private String respuesta;

        public String getRespuesta() {
            return respuesta;
        }

        public String getJsonRequest() {
            return jsonRequest;
        }
        public void setJsonRequest(String jsonRequest) {
            this.jsonRequest = "{ \"cliente_id\": \""+jsonRequest + "\"}";
        }
        @Override
        protected String doInBackground(Void... params) {
            System.out.println("doInBackground");
            try {
                URL url = new URL("http://192.168.227.254:5000/recomendar"); // Reemplaza con la URL de tu API

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                System.out.println("json requesttttttt");
                System.out.println(jsonRequest);

                // Envia el JSON de solicitud al servidor
                try (OutputStream os = connection.getOutputStream()) {
                    System.out.println("esta entrando aca");
                    byte[] input = this.getJsonRequest().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                // Obtiene la respuesta del servidor
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                    this.respuesta = response.toString();
                    return response.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error al obtener recomendaciones: " + e.getMessage());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error en la solicitud HTTP: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            System.out.println("onPostExecute");
            super.onPostExecute(jsonResponse);
            if (jsonResponse != null) {
                // Actualiza la interfaz de usuario con la respuesta obtenida.
                //usuarioRecomendacion.setText(jsonResponse);
                respuesta = jsonResponse;
                System.out.println("-----------------------------------jsonresponde" + jsonResponse);

                recomendaciones = jsonResponse;
                Intent i = new Intent(getApplicationContext(), INICIO.class);
                Bundle bundle = new Bundle();
                bundle.putString("recomendaciones", recomendaciones);
                SharedPreferences sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("keyUsuario", keyUsuario);
                editor.apply();
                i.putExtras(bundle);
                startActivity(i);
            } else {
                // Maneja el caso en el que no se pudo obtener la respuesta.
                respuesta = "Error al obtener recomendaciones.";
                //usuarioRecomendacion.setText("Error al obtener recomendaciones.");
            }
        }
    }
}