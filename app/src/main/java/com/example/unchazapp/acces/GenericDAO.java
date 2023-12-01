package com.example.unchazapp.acces;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.unchazapp.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenericDAO<T> {

    private String key = null;
    private T result = null;
    private List<T> list;

    public GenericDAO() {

    }

    public Boolean save(T object, String pathChild) {
        try {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("/");
        databaseRef.child(pathChild).push().setValue(object);
        } catch (DatabaseException e) {
            return false;
        }
        return true;
    }
    public void delete(String key, String pathChild, KeyCallback callback) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("/").child(pathChild+ "/"+key);
        System.out.println(pathChild + "/" + key);
        try {
            databaseRef.removeValue();
            callback.onKeyFound(true);
        } catch (DatabaseException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null,
                    ex);
            callback.onKeyFound(false);
        }
    }

    public void recoverByAttribute(String atributo, String valorAtributo, String pathChild, KeyCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/").child(pathChild);
        Query query = ref.orderByChild(atributo).equalTo(valorAtributo);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean keyFound = false;
                if (dataSnapshot.exists()) {
                    List<Map<String, Object>> lista = new ArrayList<>();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> object= objectMapper.convertValue(userSnapshot.getValue(), Map.class);
                        keyFound = true;
                        lista.add(object);
                    }
                    callback.onKeyFound(lista);
                }
                if (!keyFound) {
                    String errorMessage = "No se encontraron usuarios con ese atributo.";
                    System.out.println(errorMessage);
                    callback.onError(errorMessage);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                String errorMessage = "La lectura falló: " + databaseError.getCode();
                System.out.println(errorMessage);
                callback.onError(errorMessage);
            }
        });
    }

    public void recoverAll(String pathChild, KeyCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/").child(pathChild);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean keyFound = false;
                if (dataSnapshot.exists()) {
                    List<Map<String, Object>> lista = new ArrayList<>();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> object= objectMapper.convertValue(userSnapshot.getValue(), Map.class);
                        keyFound = true;
                        lista.add(object);
                    }
                    callback.onKeyFound(lista);
                }
                if (!keyFound) {
                    String errorMessage = "No se encontraron usuarios con ese atributo.";
                    System.out.println(errorMessage);
                    callback.onError(errorMessage);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                String errorMessage = "La lectura falló: " + databaseError.getCode();
                System.out.println(errorMessage);
                callback.onError(errorMessage);
            }
        });
    }

    public void key(String atributo, String valorAtributo, String pathChild, KeyCallback callback) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/").child(pathChild);
            Query query = ref.orderByChild(atributo).equalTo(valorAtributo);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("onDataChange");
                    boolean keyFound = false;
                    if (dataSnapshot.exists()) {
                        System.out.println("Entro por el exist...");
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            System.out.println("/...");
                            key = userSnapshot.getKey();
                            keyFound = true; // Indica que se encontró una clave
                            callback.onKeyFound(key);
                            break; // Sal del ciclo después de encontrar la clave
                        }
                    }else{
                        String errorMessage = "No se encontraron usuarios con ese atributo.onDataChange";
                        System.out.println(errorMessage);
                        callback.onError(errorMessage);
                    }
                    /*if (!keyFound) {

                    }*/
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    String errorMessage = "La lectura falló: " + databaseError.getCode();
                    System.out.println(errorMessage);
                    callback.onError(errorMessage);
                }
            });
    }

    public void updateByAttribute(String atributo, String valorAtributo, String pathChild, Map entidad, KeyCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/").child(pathChild);
        Query query = ref.orderByChild(atributo).equalTo(valorAtributo);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean keyFound = false;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        DatabaseReference userRef = userSnapshot.getRef();
                        userRef.updateChildren(entidad)
                                .addOnSuccessListener(new OnSuccessListener<Void>(){
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        callback.onKeyFound(true);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        callback.onKeyFound(false);
                                    }
                                });
                    }
                }
                if (!keyFound) {
                    String errorMessage = "No se encontraron usuarios con ese atributo.";
                    System.out.println(errorMessage);
                    callback.onError(errorMessage);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                String errorMessage = "La lectura falló: " + databaseError.getCode();
                System.out.println(errorMessage);
                callback.onError(errorMessage);
            }
        });
    }

    public void recoverByKey(String key, String pathChild, KeyCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(pathChild + "/" + key);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Convierte directamente el valor del dataSnapshot al tipo deseado
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> object = objectMapper.convertValue(dataSnapshot.getValue(), Map.class);
                    System.out.println("Valor encontrado");
                    callback.onKeyFound(object);
                } else {
                    String errorMessage = "No se encontró ningún valor con la clave especificada.";
                    System.out.println(errorMessage);
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String errorMessage = "La lectura falló: " + databaseError.getCode();
                System.out.println(errorMessage);
                callback.onError(errorMessage);
            }
        });
    }













}
