package com.prince.bakeryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.MyOwnViewHandler> {
    Context context;
    List<String> shortDescriptions;
    final private StepsListAdapter.ListItemClickListener mClickListener;
    public interface ListItemClickListener{
        void onListItemClick(int listItemIndex);

    }

    @NonNull
    @Override
    public StepsListAdapter.MyOwnViewHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.step_plate,parent,false);


        return new StepsListAdapter.MyOwnViewHandler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOwnViewHandler holder, int position) {
        holder.serialNumber.setText(String.valueOf(position+1));
        holder.stepDescription.setText(shortDescriptions.get(position));
    }











    @Override
    public int getItemCount() {
        return shortDescriptions.size();
    }

    public class MyOwnViewHandler extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView stepDescription;
        public final TextView serialNumber;


        public MyOwnViewHandler(@NonNull View itemView) {
            super(itemView);
            stepDescription = itemView.findViewById(R.id.step_small_description);
            serialNumber = itemView.findViewById(R.id.serial_number);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickedItemIndex = getAdapterPosition();
            mClickListener.onListItemClick(clickedItemIndex);
        }
    }
    StepsListAdapter(Context context, List<String> shortDescriptions,ListItemClickListener listItemClickListener){
        this.context=context;
        this.shortDescriptions = shortDescriptions;
        mClickListener = listItemClickListener;
        notifyDataSetChanged();
    }
}
