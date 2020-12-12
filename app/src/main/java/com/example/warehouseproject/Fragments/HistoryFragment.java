package com.example.warehouseproject.Fragments;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.warehouseproject.Code.item;
import com.example.warehouseproject.R;
import com.example.warehouseproject.customForms.ExpandableHeightGridView;
import com.example.warehouseproject.utilityClasses.DBHelper;
import com.example.warehouseproject.utilityClasses.Paginator;

import java.util.List;

public class HistoryFragment extends Fragment {

    Context act;
    View view;

    Button nextBtn;
    Button prevBtn;
    ExpandableHeightGridView ehgrid;
    EditText searchF;

    // Экземпляры классов
    private Paginator paginator;
    private SQLiteDatabase database;
    private DBHelper helper;
    private List<String> queryResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_history,container,false);

        initializeViews();

        return view;
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        act = context;

    }

    private void initializeViews() {
        nextBtn = (Button) view.findViewById(R.id.nextHistoryBtn);
        prevBtn = (Button) view.findViewById(R.id.prevhistoryBtn);
        ehgrid = (ExpandableHeightGridView) view.findViewById(R.id.historyGridView);
        searchF = (EditText) view.findViewById(R.id.historySeachField);
    }

    private void initializeValues(){

    }
}
