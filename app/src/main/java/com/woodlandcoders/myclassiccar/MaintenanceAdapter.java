package com.woodlandcoders.myclassiccar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.MaintenanceViewHolder> {

    private ArrayList mlist;
    private Context mcontext;
    private MaintenanceOnRVItemClickListener mMaintenanceOnRVItemClickListener;

    public MaintenanceAdapter( Context context, ArrayList list, MaintenanceOnRVItemClickListener maintenanceOnRVItemClickListener) {
        this.mlist = list;
        this.mcontext = context;
        this.mMaintenanceOnRVItemClickListener = maintenanceOnRVItemClickListener;
    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }


    @NonNull
    @Override
    public MaintenanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater maintenanceInflater = LayoutInflater.from(mcontext);
        View view = maintenanceInflater.inflate(R.layout.activity_maintenance_detail, parent, false);
        MaintenanceViewHolder holder = new MaintenanceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaintenanceViewHolder maintenanceHolder, int position) {
        // Creating a container.
        MaintenanceContainer listItem = (MaintenanceContainer) mlist.get(position);

        if(position%2 != 0){
            maintenanceHolder.itemView.setBackgroundResource(R.color.StandardFieldAlternate);
        }

        // Pull data out of the list.
        String id = Integer.toString(listItem.getID());
        String date = listItem.getDate();
        String mileage = listItem.getMiles();
        String repair = listItem.getRepair();

        // Put data out of TextViews
        maintenanceHolder.tIDPH.setText(id);
        maintenanceHolder.tDate.setText(date);
        maintenanceHolder.tMiles.setText(mileage);
        maintenanceHolder.tRepair.setText(repair);
    }

    // Setting references to the UI elements.
    public class MaintenanceViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView tIDPH, tDate, tMiles, tRepair;

        public MaintenanceViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            tIDPH = itemView.findViewById(R.id.idph);
            tDate = itemView.findViewById(R.id.maintDateTextView);
            tMiles = itemView.findViewById(R.id.maintMileageTextView);
            tRepair = itemView.findViewById(R.id.repairTextView);
        }


        @Override
        public void onClick(View v) {
            // This is a function call to the function located in the OilChangeActivity class
            // passing in the proper ID for the intent defined "there". The global reference
            // stated above is to the establishes this connection.
            mMaintenanceOnRVItemClickListener.onCellClick(Integer.parseInt(tIDPH.getText().toString()));
        }
    }

    public interface MaintenanceOnRVItemClickListener {
        void onCellClick(int cellContentID);
    }
}
