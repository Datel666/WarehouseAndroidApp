package com.example.warehouseproject.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.warehouseproject.Code.item;
import com.example.warehouseproject.R;

import java.util.ArrayList;
import java.util.List;

public class historyItemAdapter extends BaseAdapter {

    private Context mContext;


    private List<String> items;

    public historyItemAdapter(Context c,  ArrayList<String> _items ) {
        mContext = c;

        items=_items;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
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
            grid = inflater.inflate(R.layout., null);
            TextView name = (TextView) grid.findViewById(R.id.itemnameContainer);
            TextView type = (TextView) grid.findViewById(R.id.itemtypeContainer) ;
            TextView count = (TextView) grid.findViewById(R.id.itemcountContainer);
            //TextView description = (TextView) grid.findViewById(R.id.descriptionContainer);
            //ImageView imageView = (ImageView) grid.findViewById(R.id.imageContainer);
            //imageView.setAdjustViewBounds(true);
            name.setText(items.get(position).name);
            type.setText(items.get(position).type);
            count.setText(items.get(position).count);
            //description.setText(items.get(position).description);
            //imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
