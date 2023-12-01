package com.example.unchazapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unchazapp.model.Negocio;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<Negocio> lst;
    List<Negocio> negocioListOriginal;

    public CustomAdapter(Context context, List<Negocio> lst) {
        this.context = context;
        this.lst = lst;
        negocioListOriginal = new ArrayList<>(lst);
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageViewContacto;
        TextView textViewNombre;
        TextView textViewDescripcion;

        Negocio c = lst.get(i);
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.listview_contacto, null);
        imageViewContacto = view.findViewById(R.id.imageViewContacto);
        textViewNombre = view.findViewById(R.id.textViewNombre);
        textViewDescripcion = view.findViewById(R.id.textViewDescripcion);
        if (c.getImagen().length()>10){
            Picasso.get().load(c.getImagen()).into(imageViewContacto);
        }else{
            imageViewContacto.setImageResource(R.drawable.imagensita);
        }


        textViewNombre.setText(c.getNombreNegocio());
        textViewDescripcion.setText(c.getDescripcionNegocio());
        return view;
    }

    public void filtrado(String txtBuscar) {
        int longitud = txtBuscar.length();
        if (longitud == 0) {
            lst.clear();
            lst.addAll(negocioListOriginal);
        } else {
            List<Negocio> colleccion = negocioListOriginal.stream()
                    .filter(i -> i.getNombreNegocio().toLowerCase().contains(txtBuscar.toLowerCase()))
                    .collect(Collectors.toList());
            lst.clear();
            lst.addAll(colleccion);
        }
        notifyDataSetChanged();
    }
}
