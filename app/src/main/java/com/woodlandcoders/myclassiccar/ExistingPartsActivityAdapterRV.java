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

public class ExistingPartsActivityAdapterRV extends RecyclerView.Adapter<ExistingPartsActivityAdapterRV.ExistingPartViewHolder> {

    private ArrayList mlist;
    private Context mcontext;
    OnRVexpItemClickListener mOnRVexpItemClickListener;

    public ExistingPartsActivityAdapterRV(Context context, ArrayList list, OnRVexpItemClickListener onRVexpItemClickListener) {
        this.mcontext = context;
        this.mlist = list;
        this.mOnRVexpItemClickListener = onRVexpItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    @NonNull
    @Override
    public ExistingPartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflate = LayoutInflater.from(mcontext);
        View view = mInflate.inflate(R.layout.activity_existing_part_detail,parent, false);
        ExistingPartViewHolder holder = new ExistingPartViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExistingPartViewHolder existingPartViewHolder, int position) {
        ExistingPartsContainer  exListItem = (ExistingPartsContainer) mlist.get(position);

        if(position%2 != 0){
            existingPartViewHolder.itemView.setBackgroundResource(R.color.StandardFieldAlternate);
        }

        byte[] image = exListItem.getImage();
        Bitmap bm = BitmapFactory.decodeByteArray(image, 0, image.length);

        String id = Integer.toString(exListItem.getId());
        String name = exListItem.getPartName();
        String num = exListItem.getPartNumber();
        String mfg = exListItem.getMfg();

        existingPartViewHolder.image.setImageBitmap(bm);
        existingPartViewHolder.idInCell.setText(id);
        existingPartViewHolder.partName.setText(name);
        existingPartViewHolder.partNumber.setText(num);
        existingPartViewHolder.mfg.setText(mfg);
    }


    // An embedded class.
    public class ExistingPartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView partName, partNumber, idInCell, mfg;
        ImageView image;

        public ExistingPartViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            image = itemView.findViewById(R.id.existingPartsImageView);
            idInCell = itemView.findViewById(R.id.idTextView);
            partName = itemView.findViewById(R.id.existingPartsNameTextView);
            partNumber = itemView.findViewById(R.id.existingPartsNumberTextView);
            mfg = itemView.findViewById(R.id.existingPartsMfgTextView);

        }

        @Override
        public void onClick(View v) {
            mOnRVexpItemClickListener.onCellClick(Integer.parseInt(idInCell.getText().toString()));
        }
    }

    public interface OnRVexpItemClickListener {
        void onCellClick(int partID);
    }
}
