package com.example.itemdisplayapplication;

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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context mContext;
    private List<Upload> mUploads;

    public ItemAdapter(Context context, List<Upload> uploads)
    {
        mContext = context;
        mUploads = uploads;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewDescription;
        public TextView textViewQuantity;
        public TextView textViewPrice;
        public ImageView displayImageViewItem;


        public ItemViewHolder(View itemView)
        {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            displayImageViewItem = itemView.findViewById(R.id.display_item_image);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.single_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewTitle.setText(uploadCurrent.getmTitle());
        holder.textViewDescription.setText(uploadCurrent.getmDescription());
        holder.textViewQuantity.setText(uploadCurrent.getmQuanitity());
        holder.textViewPrice.setText(uploadCurrent.getmPrice());
        Picasso.get()
                .load(uploadCurrent.getmImageUrl())
                .placeholder(R.drawable.ic_image)
                .fit()
                .centerInside()
                .into(holder.displayImageViewItem);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }
}
