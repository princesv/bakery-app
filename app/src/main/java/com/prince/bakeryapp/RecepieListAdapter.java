package com.prince.bakeryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecepieListAdapter extends RecyclerView.Adapter<RecepieListAdapter.MyOwnViewHandler> {
    Context context;
    List<Integer> ids;
    List<String> names;
    List<String> ingredients;
    List<String> steps;
    List<Integer> servings;
    final private ListItemClickListener mClickListener;
    @NonNull
    @Override
    public MyOwnViewHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recepie_plate,parent,false);


        return new MyOwnViewHandler(view);
    }


    public interface ListItemClickListener{
        void onListItemClick(int listItemIndex);

    }




    @Override
    public void onBindViewHolder(@NonNull MyOwnViewHandler holder, int position) {
        holder.recepieName.setText(names.get(position));
        holder.servingsCount.setText(servings.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return ids.size();
    }

    public class MyOwnViewHandler extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView recepieName;
        public final TextView servingsCount;

        public MyOwnViewHandler(@NonNull View itemView) {
            super(itemView);
            recepieName = itemView.findViewById(R.id.recepie_view);
            servingsCount = itemView.findViewById(R.id.servings_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedItemIndex = getAdapterPosition();
            mClickListener.onListItemClick(clickedItemIndex);

        }
    }
    RecepieListAdapter(Context context,RecipeList parameters,ListItemClickListener listener ){
        this.context=context;
        this.ids = parameters.getIds();
        this.names = parameters.getNames();
        this.ingredients = parameters.getIngredients();
        mClickListener = listener;
        this.steps = parameters.getSteps();
        this.servings = parameters.getServings();
        notifyDataSetChanged();
    }
}
