package com.example.unchazapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.unchazapp.R;
import com.example.unchazapp.model.Negocio;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    Context context;

    public CustomAdapter(Context context, List<Negocio> lst) {
        this.context = context;
        this.lst = lst;
    }

    List<Negocio> lst;


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

        imageViewContacto.setImageResource(c.getImagen());
        textViewNombre.setText(c.getNombreNegocio());
        textViewDescripcion.setText(c.getDescripcionNegocio());
        return view;
    }
}