package com.alsam.mdbook_01;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter1 extends RecyclerView.Adapter<CardAdapter1.CardViewHolder> {

    private List<CardItem> dataList;
    private static ListPatientActivity.CardClickListener cardClickListener = null;

    public CardAdapter1(List<CardItem> dataList, ListPatientActivity.CardClickListener cardClickListener) {
        this.dataList = dataList;
        this.cardClickListener = cardClickListener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView titleTextView;
        final TextView descriptionTextView;
        final TextView selectedDateTextView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            selectedDateTextView = itemView.findViewById(R.id.selectedDateTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            cardClickListener.onClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new CardViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem currentItem = dataList.get(position);

        holder.titleTextView.setText(currentItem.getTitle());
        holder.descriptionTextView.setText(currentItem.getDescription());
        holder.selectedDateTextView.setText(currentItem.getSelectedDate());
    }

    @Override
    public int getItemCount() {
        if(dataList!=null)
        {
            return dataList.size();
        }else{

            return 0;
        }

    }

    public void setDataList(List<CardItem> newList) {
        dataList = newList;
        notifyDataSetChanged();
    }
}
