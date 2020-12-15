package com.example.warehouseproject.Fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.warehouseproject.Adapters.ItemAdapter;
import com.example.warehouseproject.Code.Item;
import com.example.warehouseproject.R;
import com.example.warehouseproject.customForms.ExpandableHeightGridView;
import com.example.warehouseproject.utilityClasses.DBHelper;
import com.example.warehouseproject.utilityClasses.Paginator;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    Context con;
    View view;
    ImageButton qrscanBtn;
    Button nextBtn;
    Button prevBtn;
    EditText searchF;
    ExpandableHeightGridView ehgrid;

    private Paginator paginator;
    private SQLiteDatabase database;
    private DBHelper helper;
    private Intent intent;

    private int totalpages;
    private int currentpage;
    private List<Item> queryResults;
    private List<Integer> queryQRS;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        initializeViews();
        initializeValues();

        updateformsDefault();

        qrscanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanClick();
            }
        });

        ehgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int chosenID = queryResults.get(currentpage * paginator.ITEMS_PER_PAGE + position).id;
                Intent chosenitemIntent = new Intent("com.example.warehouseproject.Code.chosenItemfromlistActivity");
                chosenitemIntent.putExtra("itemid", String.valueOf(chosenID));
                startActivity(chosenitemIntent);

            }
        });

        searchF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchF.removeTextChangedListener(this);
                if (!database.isOpen()) {
                    database = helper.getWritableDatabase();
                }

                updateformsAccordingfilters();
                if (ehgrid.getCount() == 0) {
                    Toast toast = Toast.makeText(con, "Предметов по заданному запросу не найдено", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                searchF.addTextChangedListener(this);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        con = context;
    }

    private void initializeViews() {
        qrscanBtn = (ImageButton) view.findViewById(R.id.scanqrBtn);
        nextBtn = (Button) view.findViewById(R.id.searchnextBtn);
        prevBtn = (Button) view.findViewById(R.id.searchprevBtn);
        searchF = (EditText) view.findViewById(R.id.searchsearchEdit);
        ehgrid = (ExpandableHeightGridView) view.findViewById(R.id.searchehGrid);
    }

    private void initializeValues() {

        helper = new DBHelper(con);
        database = helper.getWritableDatabase();

        queryResults = new ArrayList<Item>();
        queryQRS = new ArrayList<Integer>();
    }

    public List<Item> rawquery(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("Select * from itemtable order by itemname", new String[]{});
        List<Item> res = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int itemidIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int itemtypeIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMTYPE);
            int itemnameIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMNAME);
            int itemcountIndex = cursor.getColumnIndex(DBHelper.KEY_COUNT);
            int itemdescriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
            int itemphotoIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMPHOTO);

            do {

                res.add(new Item(cursor.getInt(itemidIndex), cursor.getString(itemnameIndex), cursor.getString(itemtypeIndex), cursor.getString(itemcountIndex), cursor.getString(itemdescriptionIndex), cursor.getBlob(itemphotoIndex)));
            }
            while (cursor.moveToNext());
        } else {
        }
        return res;
    }

    public List<Item> filteredqueries(SQLiteDatabase db) {

        Cursor cursor = db.rawQuery("Select * from itemtable WHERE instr(count," + "'" + searchF.getText().toString() + "'" + ") > 0 OR  instr(itemname," + "'" + searchF.getText().toString() + "'" + ") > 0 OR " +
                "instr(description," + "'" + searchF.getText().toString() + "'" + ") > 0 OR instr(itemtype," + "'" + searchF.getText().toString() + "'" + ") > 0  order by itemname", new String[]{});
        List<Item> res = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int itemidIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int itemtypeIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMTYPE);
            int itemnameIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMNAME);
            int itemcountIndex = cursor.getColumnIndex(DBHelper.KEY_COUNT);
            int itemdescriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
            int itemphotoIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMPHOTO);

            do {

                res.add(new Item(cursor.getInt(itemidIndex), cursor.getString(itemnameIndex), cursor.getString(itemtypeIndex), cursor.getString(itemcountIndex), cursor.getString(itemdescriptionIndex), cursor.getBlob(itemphotoIndex)));
            }
            while (cursor.moveToNext());
        } else {
        }
        return res;
    }

    /**
     * Изменение форм используя значения "по умолчанию"
     */
    public void updateformsDefault() {
        queryResults = rawquery(database);
        paginator = new Paginator((ArrayList) queryResults);
        totalpages = paginator.getTotalPages();
        currentpage = 0;
        toggleButtons();
        bindData(currentpage);
        ehgrid.setExpanded(true);
    }

    /**
     * Изменение форм в соответствии с фильтрами
     */
    public void updateformsAccordingfilters() {

        queryResults = filteredqueries(database);
        paginator = new Paginator((ArrayList) queryResults);
        totalpages = paginator.getTotalPages();
        currentpage = 0;
        toggleButtons();
        bindData(currentpage);
        ehgrid.setExpanded(true);
    }

    /**
     * Получение коллекции товаров для текущей страницы
     *
     * @param page
     */
    private void bindData(int page) {
        ItemAdapter adapter = new ItemAdapter(con, paginator.getCurrentGalaxys(page));
        ehgrid.setAdapter(adapter);
    }

    /**
     * Изменение состояний кнопок, используемых для изменения текущей страницы истории
     */
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

    public void scanClick() {
        Intent intent = new IntentIntegrator(SearchFragment.this.getActivity()).createScanIntent();
        intent.setAction(Intents.Scan.ACTION);
        intent.putExtra("SCAN_MODE","QR_CODE_MODE");

        intent.putExtra("RESULT_DISPLAY_DURATION_MS",0L);
        startActivityForResult(intent,0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        database.close();
        if(requestCode==0)
        {
            String id = data.getStringExtra("SCAN_RESULT");
            if(isNumeric(id)) {
                Intent chosenitemIntent = new Intent("com.example.warehouseproject.Code.chosenItemfromlistActivity");
                chosenitemIntent.putExtra("itemid", id);
                startActivity(chosenitemIntent);
            }
        } else {
            Toast toast = Toast.makeText(con, "Сканирование не удалось", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
