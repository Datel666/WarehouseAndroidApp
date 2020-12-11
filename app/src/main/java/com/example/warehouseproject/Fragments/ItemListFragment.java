package com.example.warehouseproject.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.warehouseproject.Adapters.ImageTextAdapter;
import com.example.warehouseproject.R;
import com.example.warehouseproject.customForms.ExpandableHeightGridView;

import java.util.zip.Inflater;

public class ItemListFragment extends Fragment {
    View view;
    ExpandableHeightGridView gridView;
    Context act;
    Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_itemlist, container, false);

        gridView = (ExpandableHeightGridView) view.findViewById(R.id.itemsGridView);

        ImageTextAdapter adapter = new ImageTextAdapter(act);
        gridView.setAdapter(adapter);
        gridView.setExpanded(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent("com.example.warehouseproject.Code.processorsActivity");
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent("com.example.warehouseproject.Code.motherboardsActivity");
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent("com.example.warehouseproject.Code.videocardsActivity");
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent("com.example.warehouseproject.Code.ramActivity");
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent("com.example.warehouseproject.Code.powersupplyActivity");
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent("com.example.warehouseproject.Code.bodiesActivity");
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent("com.example.warehouseproject.Code.serverthingsActivity");
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent("com.example.warehouseproject.Code.coolingActivity");
                        startActivity(intent);
                        break;
                    case 8:
                        intent = new Intent("com.example.warehouseproject.Code.ssdsActivity");
                        startActivity(intent);
                        break;
                    case 9:
                        intent = new Intent("com.example.warehouseproject.Code.hddsActivity");
                        startActivity(intent);
                        break;
                    case 10:
                        intent = new Intent("com.example.warehouseproject.Code.miscfordrivesActivity");
                        startActivity(intent);
                        break;
                    case 11:
                        intent = new Intent("com.example.warehouseproject.Code.expansiondevicesActivity");
                        startActivity(intent);
                        break;
                }
            }
        });

        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        act = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
