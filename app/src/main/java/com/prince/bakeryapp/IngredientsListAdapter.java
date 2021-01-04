package com.prince.bakeryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.MyOwnViewHandler> {
    Context context;
    List<Double> quantity = new ArrayList<>();
    List<String> measure = new ArrayList<>();
    List<String> ingredients = new ArrayList<>();
    @NonNull
    @Override
    public MyOwnViewHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.ingredients_plate,parent,false);


        return new MyOwnViewHandler(view);
    }







    @Override
    public void onBindViewHolder(@NonNull MyOwnViewHandler holder, int position) {
        holder.ingredientName.setText("-> "+ingredients.get(position));
        holder.ingredientQuantity.setText(quantity.get(position).toString());
        holder.ingredientMeasure.setText(" "+measure.get(position));
    }

    @Override
    public int getItemCount() {
        return quantity.size();
    }

    public class MyOwnViewHandler extends RecyclerView.ViewHolder {
        public final TextView ingredientName;
        public final TextView ingredientQuantity;
        public final TextView ingredientMeasure;

        public MyOwnViewHandler(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
            ingredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
            ingredientMeasure = itemView.findViewById(R.id.ingredient_measure);

        }


    }
    IngredientsListAdapter(Context context,Ingredients ingredients ){
        this.context=context;
        this.quantity = ingredients.quantity;
        this.ingredients = ingredients.ingredient;
        this.measure = ingredients.measure;

        notifyDataSetChanged();
    }
}

