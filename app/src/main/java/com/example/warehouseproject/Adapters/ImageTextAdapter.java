package com.example.warehouseproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.warehouseproject.R;

/**
 * ExpandableListAdapter class
 *
 * Данный класс описывает методы для получения экземпляра класса View из указанных составляющих
 * для последующего использования данного View в качестве предмета ListView
 */
public class ImageTextAdapter extends BaseAdapter {
    private Context mContext;

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
            grid = inflater.inflate(R.layout.cellgrid, null);
            ImageView imageView = (ImageView)grid.findViewById(R.id.gridImage);
            imageView.setAdjustViewBounds(true);
            imageView.setImageResource(mThumbIds[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}

