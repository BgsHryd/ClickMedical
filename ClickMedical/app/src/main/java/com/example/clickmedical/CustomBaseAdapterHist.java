package com.example.clickmedical;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CustomBaseAdapterHist extends ArrayAdapter {
    Context context;
    ArrayList<History> HistList;
    int[] ListImage;
    LayoutInflater inflater;

    public CustomBaseAdapterHist(Context ctx, ArrayList<History> HistList, int[] images){
        super(ctx, R.layout.activity_rslist_view, HistList);
        this.context = ctx;
        this.HistList = HistList;
        this.ListImage = images;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return HistList.size();
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
        History hist = HistList.get(position);
        convertView = inflater.inflate(R.layout.activity_rslist_view, null);
        TextView textInfo = (TextView) convertView.findViewById(R.id.infoObject);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageID);
        textInfo.setText(concatAllAttrib(position));
        image.setImageResource(ListImage[position]);

//        Glide.with(context).load(ref).into(image);

        return convertView;
    }
    public String concatAllAttrib(int position){
        String result = "";
        result+= "Rumah Sakit: " + HistList.get(position).getNamaRS() + "\n";
        result+= "Nama Kamar : " + HistList.get(position).getNamaKamar() + "\n";
        result+= "Tanggal    : " + HistList.get(position).getDate();
        return result;
    }
    public void setFilteredList(ArrayList<History> filteredList){
        this.HistList = filteredList;
        notifyDataSetChanged();
    }
}
