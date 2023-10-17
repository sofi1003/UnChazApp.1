package com.example.unchazapp;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class recomendar extends AppCompatActivity {

    private TextView usuarioRecomendacion;
    private String jsonRequest = "{ \"cliente_id\": \"-NfRe56lHIg2jw5Loopj\" }"; // Reemplaza 1 con el ID del cliente
    private static final String TAG = "ApiService";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendar);
        usuarioRecomendacion = findViewById(R.id.negocios_recomendados);
    }

    public String getJsonRequest() {
        return jsonRequest;
    }

    public void setJsonRequest(String jsonRequest) {
        this.jsonRequest = "{ \"cliente_id\": \""+jsonRequest + "\" }\"";
    }

    public void escribir(View view) {
        System.out.println("escribhir");
        new ObtenerRecomendacionesTask().execute();
        System.out.println("-------------------");
        System.out.println(jsonRequest);
    }

    private class ObtenerRecomendacionesTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            System.out.println("doInBackground");
            try {
                URL url = new URL("http://192.168.0.7:5000/recomendar"); // Reemplaza con la URL de tu API

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                System.out.println(jsonRequest);

                // Envia el JSON de solicitud al servidor
                try (OutputStream os = connection.getOutputStream()) {
                    System.out.println("entraaaaaaaaaaaaaaaaaaaa");
                    byte[] input = jsonRequest.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                System.out.println("---------------------------------------marca");
                // Obtiene la respuesta del servidor
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
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
            super.onPostExecute(jsonResponse);
            if (jsonResponse != null) {
                // Actualiza la interfaz de usuario con la respuesta obtenida.
                usuarioRecomendacion.setText(jsonResponse);
                System.out.println("-----------------------------------jsonresponde" + jsonResponse);
            } else {
                // Maneja el caso en el que no se pudo obtener la respuesta.
                usuarioRecomendacion.setText("Error al obtener recomendaciones.");
            }
        }
    }
}
