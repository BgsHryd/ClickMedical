package com.example.clickmedical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomBaseAdapterKamar extends BaseAdapter {
    Context context;
    ArrayList<Kamar> listKamar;
    int listImages[];
    LayoutInflater inflater;

    public CustomBaseAdapterKamar(Context ctx, ArrayList<Kamar> listKamar, int[] images){
        this.context = ctx;
        this.listKamar = listKamar;
        this.listImages = images;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return listKamar.size();
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
        convertView = inflater.inflate(R.layout.activity_kamarlist_view, null);
        TextView info = (TextView) convertView.findViewById(R.id.infoObject);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageID);
        info.setText(concatAttrib(position));
        image.setImageResource(listImages[position]);
        return convertView;
    }

    public String concatAttrib(int position){
        Kamar target = listKamar.get(position);
        String result = "";
        result ="Nama: " + target.getNama() + "\n"+
                "Harga: " + giveDotToHarga(target.getHarga()) + "\n"+
                "Tersedia: " + target.getJumlah() + " Kamar\n";

        return result;
    }
    public String giveDotToHarga(int harga){
        String strHarga = Integer.toString(harga);
        int len = strHarga.length();
        int count_three = 0; String result = "";

        for (int i = len-1; i >= 0; i--){
            result = strHarga.charAt(i) + result;
            count_three++;
            if (count_three == 3 && i != 0){
                result = "." + result;
                count_three = 0;
            }
        }
        return result;
    }
}
