package com.example.b3tempoduffard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.b3tempoduffard.databinding.TempoDateItemV2Binding;

import java.util.List;

public class TempoDateAdapterV2 extends RecyclerView.Adapter<TempoDateAdapterV2.TempoDateV2ViewHolder>  {

    private List<TempoDate> tempoDates;
    private Context context ;


    @NonNull
    @Override
    public TempoDateV2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tempo_date_item_v2, parent,false);
        TempoDateItemV2Binding binding = TempoDateItemV2Binding.bind(v);


        return new TempoDateV2ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TempoDateV2ViewHolder holder, int position) {
        holder.binding.textView2.setText("great");
    }


    public TempoDateAdapterV2(List<TempoDate> tempoDates, Context context) {
        this.tempoDates = tempoDates;
        this.context = context ;
    }

    @Override
    public int getItemCount() {
        return tempoDates.size();
    }

    public class TempoDateV2ViewHolder extends RecyclerView.ViewHolder {
        TempoDateItemV2Binding binding ;

        public TempoDateV2ViewHolder(@NonNull TempoDateItemV2Binding binding) {
            super(binding.getRoot());
            this.binding = binding ;
        }
    }
}
