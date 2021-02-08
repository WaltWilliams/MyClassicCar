package com.woodlandcoders.myclassiccar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewPartsActivityAdapterRV extends RecyclerView.Adapter<com.woodlandcoders.myclassiccar.ViewPartsActivityAdapterRV.ViewPartsViewHolder> {

    private Context mContext;
    private ArrayList mlist;
    private com.woodlandcoders.myclassiccar.ViewPartsActivityAdapterRV.ViewPartsOnRVItemClickListener mViewPartsOnRVItemClickListener;

    public ViewPartsActivityAdapterRV(Context context, ArrayList list, com.woodlandcoders.myclassiccar.ViewPartsActivityAdapterRV.ViewPartsOnRVItemClickListener viewPartsOnRVItemClickListener) {
        this.mContext = context;
        this.mlist = list;
        this.mViewPartsOnRVItemClickListener = viewPartsOnRVItemClickListener;
    }

    public int getItemCount() {
        return mlist.size();
    }


    @NonNull
    @Override
    public com.woodlandcoders.myclassiccar.ViewPartsActivityAdapterRV.ViewPartsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater vpInflater = LayoutInflater.from(mContext);
        View view = vpInflater.inflate(R.layout.activity_view_parts_detail, parent, false);
        com.woodlandcoders.myclassiccar.ViewPartsActivityAdapterRV.ViewPartsViewHolder holder = new com.woodlandcoders.myclassiccar.ViewPartsActivityAdapterRV.ViewPartsViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull com.woodlandcoders.myclassiccar.ViewPartsActivityAdapterRV.ViewPartsViewHolder viewPartsViewHolder, int position) {
        ViewPartsContainer  vpListItem = (ViewPartsContainer) mlist.get(position);

        if(position%2 != 0){
            viewPartsViewHolder.itemView.setBackgroundResource(R.color.StandardFieldAlternate);
        }

        byte[] image = vpListItem.getImage();
        Bitmap bm = BitmapFactory.decodeByteArray(image, 0, image.length);

        String id = Integer.toString(vpListItem.getId());
        String name = vpListItem.getPartName();
        String num = vpListItem.getPartNumber();

        viewPartsViewHolder.image.setImageBitmap(bm);
        viewPartsViewHolder.idInCell.setText(id);
        viewPartsViewHolder.partName.setText(name);
        viewPartsViewHolder.partNumber.setText(num);
    }


    public class ViewPartsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView partName, partNumber, idInCell;
        ImageView image;

        public ViewPartsViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            image = itemView.findViewById(R.id.viewPartsImageView);
            idInCell = itemView.findViewById(R.id.idTextView);
            partName = itemView.findViewById(R.id.viewPartsNameTextView);
            partNumber = itemView.findViewById(R.id.viewPartsNumberTextView);
        }

        @Override
        public void onClick(View v) {
            // This is a function call to the function located in the ViewPartsActivityRV class
            // passing in the proper ID for the intent defined "there". The global reference
            // stated above is to the establishes this connection.
            mViewPartsOnRVItemClickListener.onCellClick(Integer.parseInt(idInCell.getText().toString()));
        }
    }


    public interface ViewPartsOnRVItemClickListener {
        void onCellClick(int cellContentID);
    }

}
