package com.woodlandcoders.myclassiccar;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by walt on 2/13/18.
 */

public class SinglePartActivityAdapter extends CursorAdapter {

    DBManagerSchema schema;

    public SinglePartActivityAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_single_part_detail, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Alternating the cell color
        if(cursor.getPosition() % 2 == 1) {
            view.setBackgroundResource(R.color.StandardFieldAlternate);
        }

        TextView singlePartMfgTextView = view.findViewById(R.id.singlePartSrcTextView);
        TextView singlePartNumTextView = view.findViewById(R.id.singlePartNumTextView);

        singlePartMfgTextView.setText(cursor.getString(cursor.getColumnIndex(schema.COL_SOURCE)));
        singlePartNumTextView.setText(cursor.getString(cursor.getColumnIndex(schema.COL_PART_NUMBER)));
    }
}
