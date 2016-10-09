package com.example.ziyuhuang.picshot;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziyuhuang on 10/7/16.
 * customized grid view adapter
 */

public class GridViewAdapter extends ArrayAdapter<GridItem> {

    private MainActivity mContext;
    private int layoutResourceId;
    private ArrayList<GridItem> mGridData = new ArrayList<>();

//    DB_Controller db  = new DB_Controller(this, "", null, 1);

    public GridViewAdapter(MainActivity context, int resource, ArrayList<GridItem> mGridData) {
        super(context, resource, mGridData);
        this.layoutResourceId = resource;
        this.mContext = context;
        this.mGridData = mGridData;
    }

    public void setGridData(ArrayList<GridItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.saveButton = (Button) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final GridItem item = mGridData.get(position);
//        holder.titleTextView.setText(Html.fromHtml(item.getTitle()));

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.displayImage(item.getImage());
            }
        });

        holder.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.save(item.getImage());
            }
        });

        Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
        return row;
    }

    static class ViewHolder {
        Button saveButton;
        ImageView imageView;
    }
}
