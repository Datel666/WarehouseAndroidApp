package com.example.warehouseproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.warehouseproject.Code.historyitem;
import com.example.warehouseproject.R;
import java.util.ArrayList;
import java.util.List;

/**
 * historyItemAdapter class
 *
 * Данный класс описывает методы для получения экземпляра класса View из указанных составляющих
 * для последующего использования данного View в качестве предмета структуры ExpandableListView
 */
public class HistoryItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<historyitem> items;

    public HistoryItemAdapter(Context c, ArrayList<historyitem> _items ) {
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
            grid = inflater.inflate(R.layout.historyitemsgrid, null);
            TextView operation = (TextView) grid.findViewById(R.id.operationContainer);
            TextView vendor = (TextView) grid.findViewById(R.id.vendorhistoryContainer);
            TextView name = (TextView) grid.findViewById(R.id.historynameContainer);
            TextView type = (TextView) grid.findViewById(R.id.historytypeContainer) ;
            TextView count = (TextView) grid.findViewById(R.id.historyCountContainer);

            operation.setText(items.get(position).operation);
            vendor.setText(items.get(position).vendor);
            type.setText(items.get(position).type);
            name.setText(items.get(position).name);
            count.setText(items.get(position).count);

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
