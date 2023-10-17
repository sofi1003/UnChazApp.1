package com.example.unchazapp.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unchazapp.model.Negocio;

import java.util.List;

public class NegocioAdapter extends RecyclerView.Adapter<NegocioAdapter.viewHolderNegocios> {

    List<Negocio>  negocioList;

    public NegocioAdapter(List<Negocio> negocioList) {
        this.negocioList = negocioList;
    }

    @NonNull
    @Override
    public viewHolderNegocios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderNegocios holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewHolderNegocios extends RecyclerView.ViewHolder {

        public viewHolderNegocios(@NonNull View itemView) {
            super(itemView);
        }
    }

}




