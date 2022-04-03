package com.example.facebookclone.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebookclone.databinding.RecyclerRowBinding;
import com.example.facebookclone.model.Gönderi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GönderiAdapter extends RecyclerView.Adapter<GönderiAdapter.GönderiHolder> {

    private ArrayList<Gönderi> gönderiArrayList;

    public GönderiAdapter(ArrayList<Gönderi> gönderiArrayList) {
        this.gönderiArrayList = gönderiArrayList;
    }

    @NonNull
    @Override
    public GönderiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding =RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GönderiHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GönderiHolder holder, int position) {
        holder.recyclerRowBinding.reycylerViewEmailTextView.setText(gönderiArrayList.get(position).email);
        holder.recyclerRowBinding.recyclerViewYorumTextView.setText(gönderiArrayList.get(position).yorum);
        Picasso.get().load(gönderiArrayList.get(position).dowloandUrl).into(holder.recyclerRowBinding.recyclerVieWImageView);
    }

    @Override
    public int getItemCount() {
        return gönderiArrayList.size();
    }

    class GönderiHolder extends RecyclerView.ViewHolder{

        RecyclerRowBinding recyclerRowBinding;

        public GönderiHolder(RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());

            this.recyclerRowBinding = recyclerRowBinding;
        }
    }
}
