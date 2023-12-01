package com.example.unchazapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unchazapp.R;
import com.example.unchazapp.model.Negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NegocioAdapter extends RecyclerView.Adapter<NegocioAdapter.viewHolderNegocios> {

    List<Negocio> negocioList;
    List<Negocio> negocioListOriginal;

    public NegocioAdapter(List<Negocio> negocioList) {
        this.negocioList = negocioList;
        negocioListOriginal = new ArrayList<>(negocioList);
    }

    @NonNull
    @Override
    public viewHolderNegocios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_contacto, parent, false);
        return new viewHolderNegocios(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderNegocios holder, int position) {
        Negocio negocio = negocioList.get(position);
        // Aquí deberías realizar la vinculación de datos en tu ViewHolder
        // Puedes obtener el negocio en la posición actual mediante negocioList.get(position)
    }

    @Override
    public int getItemCount() {
        return negocioList.size();
    }

    public class viewHolderNegocios extends RecyclerView.ViewHolder {

        public viewHolderNegocios(@NonNull View itemView) {
            super(itemView);
            // Aquí puedes inicializar elementos de la vista y configurar clics si es necesario
        }
    }

    public void filtrado(String txtBuscar) {
        int longitud = txtBuscar.length();
        if (longitud == 0) {
            negocioList.clear();
            negocioList.addAll(negocioListOriginal);
        } else {
            List<Negocio> colleccion = negocioListOriginal.stream()
                    .filter(i -> i.getNombreNegocio().toLowerCase().contains(txtBuscar.toLowerCase()))
                    .collect(Collectors.toList());
            negocioList.clear();
            negocioList.addAll(colleccion);
        }
        notifyDataSetChanged();
    }
}





