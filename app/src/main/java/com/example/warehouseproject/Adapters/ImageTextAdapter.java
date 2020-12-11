package com.example.warehouseproject.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.warehouseproject.R;

import java.util.List;

public class ImageTextAdapter extends BaseAdapter {
    private Context mContext;
    /*
    private final String[] names = {"Процессоры","Материнские платы","Видеокарты","Оперативная память","Блоки питания","Корпуса",
    "Комплектующие для сервера","Охлаждение компьютера","SSD накопители","Жесткие диски","Аксессуары для накопителей",
    "Устройства расширения"};

     */


    // references to our images
    public List<Bitmap> images;
    public List<String> names;
    private int[] mThumbIds = {R.drawable.processors,R.drawable.motherboards,R.drawable.videocards,R.drawable.ram,
    R.drawable.powersupply,R.drawable.bodies,R.drawable.serverthings,R.drawable.cooling,R.drawable.ssds,R.drawable.hdds,R.drawable.miscfordrives,R.drawable.expansiondevices};


    public ImageTextAdapter(Context c ) {
        mContext = c;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mThumbIds.length;
    }



    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.cellgrid, null);
            //TextView textView = (TextView) grid.findViewById(R.id.gridText);
            ImageView imageView = (ImageView)grid.findViewById(R.id.gridImage);
            imageView.setAdjustViewBounds(true);
           // textView.setText(names[position]);
            imageView.setImageResource(mThumbIds[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}

