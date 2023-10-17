package com.example.unchazapp.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Usuario {
    private String userName;
    private String email;
    private String documento;
    private String password;

    public Usuario(String userName, String email, String documento, String password) {
        this.userName = userName;
        this.email = email;
        this.documento = documento;
        this.password = password;
    }

    public Usuario() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario toUsuario(Map<String, Object> mapa){
        ObjectMapper objectMapper = new ObjectMapper();
        Usuario usuario = objectMapper.convertValue(mapa, Usuario.class);
        return usuario;
    }

    public static List<Usuario> toListUsuario(List<Map<String, Object>> lista){
        List<Usuario> listaUsuarios = new ArrayList<>();
        for (Map<String, Object> mapa : lista) {
            ObjectMapper objectMapper = new ObjectMapper();
            Usuario usuario = objectMapper.convertValue(mapa, Usuario.class);
            listaUsuarios.add(usuario);
        }
        return listaUsuarios;
    }

    @Override
    public String toString() {
        return "Usuario{" + "userName=" + userName + ", email=" + email + ", documento=" + documento + ", password=" + password + '}';
    }


}