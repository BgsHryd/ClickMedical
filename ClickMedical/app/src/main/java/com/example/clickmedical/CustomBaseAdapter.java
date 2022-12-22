package com.example.clickmedical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomBaseAdapter extends BaseAdapter {
    Context context;
    String ListRS[];
    int ListImage[];
    LayoutInflater inflater;

    public CustomBaseAdapter(Context ctx, String [] RSList, int [] images){
        this.context = ctx;
        this.ListRS = RSList;
        this.ListImage = images;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return ListRS.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_rslist_view, null);
        TextView text = (TextView) convertView.findViewById(R.id.textView);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageID);
        text.setText(ListRS[position]);
        image.setImageResource(ListImage[position]);
        return convertView;
    }
}
