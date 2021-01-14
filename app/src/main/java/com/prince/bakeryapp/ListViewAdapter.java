package com.prince.bakeryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

    Context context;
    RecipeList recipeList;

    public ListViewAdapter(Context context, RecipeList recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @Override
    public int getCount() {
        return recipeList.getIds().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            Context context = parent.getContext();

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.recepie_plate,parent,false);
            ((TextView)convertView.findViewById(R.id.recepie_view)).setText(recipeList.getNames().get(position));
            ((TextView)convertView.findViewById(R.id.servings_view)).setText(String.valueOf(recipeList.getServings().get(position)));

        }
        return convertView;
    }
}
