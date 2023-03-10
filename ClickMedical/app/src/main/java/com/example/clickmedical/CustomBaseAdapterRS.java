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

public class CustomBaseAdapterRS extends ArrayAdapter {
    Context context;
    ArrayList<RumahSakit> ListRS;
    int[] ListImage;
    LayoutInflater inflater;

    public CustomBaseAdapterRS(Context ctx, ArrayList<RumahSakit> RSList, int[] images){
        super(ctx, R.layout.activity_rslist_view, RSList);
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
        RumahSakit rs = ListRS.get(position);
        System.out.println("ImagePath ==> " + rs.getImagePath());
        StorageReference ref = FirebaseStorage.getInstance().getReference(rs.getImagePath());
        convertView = inflater.inflate(R.layout.activity_rslist_view, null);
        TextView textInfo = (TextView) convertView.findViewById(R.id.infoObject);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageID);
        textInfo.setText(concatAllAttrib(position));
        image.setImageResource(ListImage[position]);

//        Glide.with(context).load(ref).into(image);

        return convertView;
    }
    public void setFilteredList(ArrayList<RumahSakit> filteredList){
        this.ListRS = filteredList;
        notifyDataSetChanged();
    }
    public void loadImageRS(Context context, ImageView imgView, int position){
        String imagePath;
        RumahSakit rs = ListRS.get(position);
        imagePath = rs.getImagePath();
        StorageReference ref = FirebaseStorage.getInstance().getReference(imagePath);
        Glide.with(context).load(ref).into(imgView);
    }
    public String concatAllAttrib(int position){
        String result = "";
        result+= "Nama      : " + ListRS.get(position).getNama() + "\n";
        result+= "No.telp   : " + ListRS.get(position).getPhone() + "\n";
        result+= "Alamat    : " + ListRS.get(position).getAlamat();

        return result;
    }
}
