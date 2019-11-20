package com.superdroid.order;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.CustomViewHolder> {
    private ArrayList<StoreData> mList = null;
    private Activity context = null;


    public StoreAdapter(Activity context, ArrayList<StoreData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView type;
        protected TextView name;
        protected TextView address;
        protected ImageView image;

        public CustomViewHolder(View view) {
            super(view);
            this.type = (TextView) view.findViewById(R.id.textView_list_type);
            this.name = (TextView) view.findViewById(R.id.textView_list_name);
            this.address = (TextView) view.findViewById(R.id.textView_list_address);
            this.image = (ImageView) view.findViewById(R.id.imageView);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.store_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.type.setText(mList.get(position).getMember_type());
        viewholder.name.setText(mList.get(position).getMember_name());
        viewholder.address.setText(mList.get(position).getMember_address());
        if(mList.get(position).getMember_image()!=null)
            viewholder.image.setImageBitmap(mList.get(position).getMember_image());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
