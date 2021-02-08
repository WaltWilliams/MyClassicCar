package com.woodlandcoders.myclassiccar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;


public class OilChangeAdapter extends RecyclerView.Adapter<OilChangeAdapter.OilViewHolder>  {

    private ArrayList mlist;
    private Context mcontext;
    private OilOnRVItemClickListener mOilOnRVItemClickListener;


    public OilChangeAdapter(Context mcontext, ArrayList mlist, OilOnRVItemClickListener oilOnRVItemClickListener) {
        this.mlist = mlist;
        this.mcontext = mcontext;
        this.mOilOnRVItemClickListener = oilOnRVItemClickListener;
    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }


    @NonNull
    @Override
    public OilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mcontext);
        View view = mInflater.inflate(R.layout.activity_oil_change_detail, parent, false);
        OilViewHolder holder = new OilViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OilViewHolder oilViewHolder, int position) {
        OilChangeContainer listItem = (OilChangeContainer) mlist.get(position);

        if(position%2 != 0){
            oilViewHolder.itemView.setBackgroundResource(R.color.StandardFieldAlternate);
        }

        String id = Integer.toString(listItem.getId());
        String date = listItem.getDate();
        String mileage = listItem.getMileage();
        String due = listItem.getDue();
        String viscosity = listItem.getViscosity();
        String oBrand = listItem.getOBrand();
        String fBrand = listItem.getFBrand();

        oilViewHolder.tIDPH.setText(id);
        oilViewHolder.tDate.setText(date);
        oilViewHolder.tMileage.setText(mileage);
        oilViewHolder.tDue.setText(due);
        oilViewHolder.tViscosity.setText(viscosity);
        oilViewHolder.tOBrand.setText(oBrand);
        oilViewHolder.tFBrand.setText(fBrand);
    }



    // Embedded class for the oil RecyclerView.
    public class OilViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tIDPH, tDate, tMileage, tDue, tViscosity, tOBrand, tFBrand;

        public OilViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            tIDPH = itemView.findViewById(R.id.idph);
            tDate = itemView.findViewById(R.id.oilDateTextView);
            tMileage = itemView.findViewById(R.id.oilMileageTextView);
            tDue = itemView.findViewById(R.id.oilDueTextView);
            tOBrand = itemView.findViewById(R.id.oilBrandTextView);
            tViscosity = itemView.findViewById(R.id.oilVisTextView);
            tFBrand = itemView.findViewById(R.id.oilFilterTextView);
        }

        @Override
        public void onClick(View v) {
            // This is a function call to the function located in the OilChangeActivity class
            // passing in the proper ID for the intent defined "there". The global reference
            // stated above is to the establishes this connection.
            mOilOnRVItemClickListener.onCellClick(tIDPH.getText().toString());
        }
    }



    public interface OilOnRVItemClickListener {
        void onCellClick(String cellContentID);
    }
}
