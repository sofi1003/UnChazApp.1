package com.example.unchazapp.services.python;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ObtenerRecomendacionesTask extends AsyncTask<Void, Void, String> {

    private TextView usuarioRecomendacion;
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
            URL url = new URL("http://192.168.0.7:5000/recomendar"); // Reemplaza con la URL de tu API

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            System.out.println(jsonRequest);

            // Envia el JSON de solicitud al servidor
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonRequest.getBytes("utf-8");
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
        } else {
            // Maneja el caso en el que no se pudo obtener la respuesta.
            respuesta = "Error al obtener recomendaciones.";
            //usuarioRecomendacion.setText("Error al obtener recomendaciones.");
        }
    }

}
