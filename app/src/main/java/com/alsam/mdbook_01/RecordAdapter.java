package com.alsam.mdbook_01;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewholder> {


    private ArrayList<Record> mrecordList;
    private Record currentRecord;
    private Activity mActivity;
    private onItemClickListener mListener;

    public interface onItemClickListener {
        void onItemClick(int position);
        void viewmapClick(int postion);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    /**
     * A static class which is for a particular view, which contains the contents of record.
     */
    public static class RecordViewholder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDate;
        public TextView mComment;
        public ImageButton mAddComment;
        public ImageButton mLocation;
        public  TextView comment;


        /**
         * creates a RecordViewHolder object
         * @param itemView
         * @param listener
         */
        public RecordViewholder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.recordImage);
            mTitle = itemView.findViewById(R.id.recordTitle);
            mDate = itemView.findViewById(R.id.recordDate);
            mComment = itemView.findViewById(R.id.recordComments);
            mAddComment = itemView.findViewById(R.id.recordAddComment);
            mLocation = itemView.findViewById(R.id.recordLocation);

            comment = itemView.findViewById(R.id.comment);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                           listener.onItemClick(position);
                        }
                    }
                }
            });

            mLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.viewmapClick(position);
                        }
                    }
                }
            });

        }
    }

    /**
     * creates a RecordAdapter object
     * @param recordList: needs list of all records by the user currently signed
     * @param activity: needs the current activity to implement RecordAdapter
     */
    public RecordAdapter(ArrayList<Record> recordList, Activity activity,onItemClickListener listener){
        mrecordList = recordList;
        mActivity = activity;
        this.mListener = listener;
    }


    /**
     * when a RecordViewHolder is created, it adds to a layout
     * @param viewGroup
     * @param i
     * @return RecordViewHolder
     */
    @NonNull
    @Override
    public RecordViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v  = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.record_item,viewGroup,false);
        RecordViewholder rvh = new RecordViewholder(v,mListener);
        return rvh;
    }

    /**
     * Sets the content of a record to objects on the RecordViewHolder
     * @param recordViewholder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecordViewholder recordViewholder, int position) {
        currentRecord = mrecordList.get(position);
        //recordViewholder.mImageView.setImageResource(currentRecord.getPhotos().get(0).getPhotoid());
        recordViewholder.mTitle.setText(currentRecord.getTitle());
        recordViewholder.mDate.setText(currentRecord.getDate().toString());
        recordViewholder.mComment.setText(currentRecord.getDescription());
        try {
            recordViewholder.comment.setText(currentRecord.getComment());
        } catch (Exception e) {
            e.printStackTrace();
        }


        Glide.with(mActivity)
                .load(currentRecord.getPhotos()) // image url
                .placeholder(R.drawable.placeholder) // any placeholder to load at start
                .error(R.drawable.imagenotfound)  // any image in case of error
                .override(200, 200) // resizing
                .centerCrop()
                .into(recordViewholder.mImageView);
    }


    /**
     * @return size of list
     */
    @Override
    public int getItemCount() {
        return mrecordList.size();
    }

}
