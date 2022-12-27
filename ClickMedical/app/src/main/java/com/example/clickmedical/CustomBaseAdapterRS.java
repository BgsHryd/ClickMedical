package com.example.clickmedical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomBaseAdapterRS extends BaseAdapter {
    Context context;
    ArrayList<RumahSakit> ListRS;
    int ListImage[];
    LayoutInflater inflater;

    public CustomBaseAdapterRS(Context ctx, ArrayList<RumahSakit> RSList, int [] images){
        this.context = ctx;
        this.ListRS = RSList;
        this.ListImage = images;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return ListRS.size();
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
        TextView textInfo = (TextView) convertView.findViewById(R.id.namaRS);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageID);
        textInfo.setText(concatAllAttrib(position));
        image.setImageResource(ListImage[position]);
        return convertView;
    }
    public String concatAllAttrib(int position){
        String result = "";
        result+= "Nama      : " + ListRS.get(position).getNama() + "\n";
        result+= "No.telp   : " + ListRS.get(position).getPhone() + "\n";
        result+= "Alamat    : " + ListRS.get(position).getAlamat() + "\n";
        result+= "website   : " + ListRS.get(position).getWebsite() + "\n";

        return result;
    }
}
