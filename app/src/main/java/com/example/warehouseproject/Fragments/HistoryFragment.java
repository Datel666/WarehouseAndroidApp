package com.example.warehouseproject.Fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import com.example.warehouseproject.Adapters.historyItemAdapter;

import com.example.warehouseproject.R;
import com.example.warehouseproject.customForms.ExpandableHeightGridView;
import com.example.warehouseproject.utilityClasses.DBHelper;
import com.example.warehouseproject.utilityClasses.HistoryPaginator;

import com.example.warehouseproject.Code.historyitem;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    Context act;
    View view;

    Button nextBtn;
    Button prevBtn;
    ExpandableHeightGridView ehgrid;
    EditText searchF;

    private int totalpages;
    private int currentpage;

    // Экземпляры классов
    private HistoryPaginator paginator;
    private SQLiteDatabase database;
    private DBHelper helper;
    private List<historyitem> queryResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_history,container,false);

        initializeViews();
        initializeValues();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentpage++;
                toggleButtons();
                bindData(currentpage);
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentpage--;
                toggleButtons();
                bindData(currentpage);
            }
        });
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
        helper = new DBHelper(view.getContext());
        database = helper.getWritableDatabase();
        queryResults = loadinfo();
        paginator = new HistoryPaginator((ArrayList<historyitem>) queryResults);
        totalpages = paginator.getTotalPages();
        currentpage = 0;
        toggleButtons();
        bindData(currentpage);
        ehgrid.setExpanded(true);
    }

    private List<historyitem> loadinfo(){

        if(!database.isOpen()){
            database = helper.getWritableDatabase();
        }
        List<historyitem> res = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_SUPPLY + " a JOIN ( SELECT itemid, "+ DBHelper.KEY_ITEMNAME + " as itemname ," + DBHelper.KEY_ITEMTYPE+ " " +
                "as itemtype FROM " + DBHelper.TABLE_WAREHOUSE + ") b  on a.itemid = b.itemid",null);

        if (cursor.moveToFirst()) {
            int operationIndex = cursor.getColumnIndex(DBHelper.KEY_SUPPLYTYPE);
            int vendorIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMVENDOR);
            int typeIndex = cursor.getColumnIndex("itemtype");
            int nameIndex = cursor.getColumnIndex("itemname");
            int countIndex = cursor.getColumnIndex(DBHelper.KEY_COUNT2);

            do {

                res.add(new historyitem(cursor.getString(operationIndex),cursor.getString(vendorIndex),cursor.getString(typeIndex),cursor.getString(nameIndex),cursor.getString(countIndex)));
            }
            while (cursor.moveToNext());
        } else {
        }
        return res;
    }

    /**
     * Получение коллекции товаров для текущей страницы
     * @param page
     */
    private void bindData(int page) {
        historyItemAdapter adapter = new historyItemAdapter(view.getContext(), paginator.getCurrentGalaxys(page));
        ehgrid.setAdapter(adapter);
    }

    private void toggleButtons() {
        //SINGLE PAGE DATA
        if (totalpages <= 0) {
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(false);
        }
        //LAST PAGE
        else if (currentpage == totalpages) {
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(true);
        }
        //FIRST PAGE
        else if (currentpage == 0) {
            prevBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        }
        //SOMEWHERE IN BETWEEN
        else if (currentpage >= 1 && currentpage < totalpages) {
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(true);
        }
    }
}
