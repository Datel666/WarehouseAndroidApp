package com.example.warehouseproject.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.warehouseproject.Code.Item;
import com.example.warehouseproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ItemAdapter class
 *
 * Данный класс описывает методы для получения экземпляра класса View из указанных составляющих
 *  * для последующего использования данного View в качестве предмета структуры ExpandableListView
 */
public class ItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<Item> Items;

    public ItemAdapter(Context c, ArrayList<Item> _items ) {
        mContext = c;
        Items =_items;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return Items.size();
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
            grid = inflater.inflate(R.layout.itemsgrid, null);
            TextView name = (TextView) grid.findViewById(R.id.itemnameContainer);
            TextView type = (TextView) grid.findViewById(R.id.itemtypeContainer) ;
            TextView count = (TextView) grid.findViewById(R.id.itemcountContainer);
            name.setText(Items.get(position).name);
            type.setText(Items.get(position).type);
            count.setText(Items.get(position).count);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}


