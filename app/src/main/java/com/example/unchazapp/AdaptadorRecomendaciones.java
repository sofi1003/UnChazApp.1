package com.example.unchazapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unchazapp.Entidad;
import com.example.unchazapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorRecomendaciones extends BaseAdapter {

    private ArrayList<Entidad> listItemR;
    private Context context;

    public AdaptadorRecomendaciones(ArrayList<Entidad> listItemR, Context context) {
        this.listItemR = listItemR;
        this.context = context;
    }


    @Override
    public int getCount() {
        return listItemR.size();
    }

    @Override
    public Object getItem(int i) {
        return listItemR.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Entidad item =(Entidad) getItem(i);

        view = LayoutInflater.from(context).inflate(R.layout.listview_inicio_recomendaciones, null);
        ImageView imgFoto = view.findViewById(R.id.imageViewREC);
        TextView tituloREC = view.findViewById(R.id.textViewTituloREC);
        TextView categoriaREC = view.findViewById(R.id.textViewCategoriaREC);
        TextView descripcionREC = view.findViewById(R.id.textViewDescripcionREC);


            Picasso.get()
                    .load(item.getImgFoto())  // Reemplaza neg.getUrlDeLaImagen() con el método correcto para obtener la URL de la imagen
                    .placeholder(R.drawable.imagensita)  // Placeholder mientras se carga la imagen
                    .error(R.drawable.imagensita)  // Placeholder en caso de error al cargar la imagen
                    .into(imgFoto);
        tituloREC.setText(item.getTitulo());
        categoriaREC.setText(item.getCategoria());
        descripcionREC.setText(item.getDescripcion());

        return view;
    }
}
