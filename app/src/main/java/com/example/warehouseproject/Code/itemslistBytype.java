package com.example.warehouseproject.Code;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.warehouseproject.Adapters.ExpandableListAdapter;
import com.example.warehouseproject.Adapters.itemAdapter;
import com.example.warehouseproject.R;
import com.example.warehouseproject.customForms.ExpandableHeightGridView;
import com.example.warehouseproject.utilityClasses.DBHelper;
import com.example.warehouseproject.utilityClasses.Paginator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class itemslistBytype extends AppCompatActivity {

    // Формы
    private ExpandableListAdapter listAdapter;
    private ExpandableHeightGridView ehgrid;
    private ExpandableListView settings;
    private Button nextBtn;
    private Button prevBtn;
    private Button showfiltersBtn;
    private EditText searchF;


    // Переменные
    private int totalpages;
    private int currentpage;
    private String itemtype;
    private String action;

    // Структуры
    private List<item> queryResults;
    private List<Integer> queryQRS;
    private List<String> listDataHeader;
    private HashMap<String, List<CheckBox>> listDataChild;
    private String[] selectionBuilder;
    private String[] itemtypes;
    private String[] filtersettings;

    // Экземпляры классов
    private Paginator paginator;
    private SQLiteDatabase database;
    private DBHelper helper;
    private Intent intent;

    /**
     * Создание формы
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemslist_bytype);

        // Инициализация форм
        initializeViews();
        // Инициализация переменных, заполнение форм
        initializeValues();
        // Первичное заполнение форм
        updateformsDefault();

        if (ehgrid.getCount() == 0) {
            Toast toast = Toast.makeText(this, "В базе данных нет записей по комплектующим с типом - " + itemtype.toString(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        //Установка слушателей событий
        if (!filtersettings[0].equals("")) {
            prepareListData();
            listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
            settings.setAdapter(listAdapter);
            settings.setVisibility(View.INVISIBLE);

            settings.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    CheckBox checktemp = v.findViewById(R.id.lblCheckboxItem);
                    checktemp.setChecked(!checktemp.isChecked());
                    listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setChecked(checktemp.isChecked());

                    if (!database.isOpen()) {
                        database = helper.getWritableDatabase();
                    }
                    updateformsAccordingfilters();

                    if (ehgrid.getCount() == 0) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Предметов по заданному запросу не найдено", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    return false;
                }

            });

        }
        else{
            showfiltersBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast = Toast.makeText(getApplicationContext(),"Фильтров для данного типа товаров не найдено",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            });
        }

        searchF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
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
                    Toast toast = Toast.makeText(getApplicationContext(), "Предметов по заданному запросу не найдено", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                searchF.addTextChangedListener(this);
            }
        });

        ehgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int chosenID = queryResults.get(currentpage * paginator.ITEMS_PER_PAGE + position).id;
                Intent chosenitemIntent = new Intent("com.example.warehouseproject.Code.chosenItemfromlistActivity");
                chosenitemIntent.putExtra("itemid",String.valueOf(chosenID));
                startActivity(chosenitemIntent);

            }
        });
    }

    /**
     * Инициализация форм
     */
    private void initializeViews() {
        nextBtn = (Button) findViewById(R.id.nextbtn);
        prevBtn = (Button) findViewById(R.id.prevbtn);
        showfiltersBtn = (Button) findViewById(R.id.showFiltersBtn);
        settings = findViewById(R.id.settingsList);
        ehgrid = (ExpandableHeightGridView) findViewById(R.id.ehgridview);
        searchF = (EditText) findViewById(R.id.searchField);
    }

    /**
     * Инициализация значений
     */
    private void initializeValues() {

        helper = new DBHelper(this);
        database = helper.getWritableDatabase();
        itemtypes = getResources().getStringArray(R.array.itemTypes);
        intent = getIntent();
        action = intent.getAction();
        itemtype = switchAction(action, itemtypes);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<CheckBox>>();
        selectionBuilder  = new String[2];
        queryResults = new ArrayList<item>();
        queryQRS = new ArrayList<Integer>();
    }

    /**
     * Получение коллекции товаров для текущей страницы
     * @param page
     */
    private void bindData(int page) {
        itemAdapter adapter = new itemAdapter(this, paginator.getCurrentGalaxys(page));
        ehgrid.setAdapter(adapter);
    }

    /**
     * Генерация фильтров поиска товаров
     */
    private void prepareListData() {

        if (!database.isOpen()) {
            database = helper.getWritableDatabase();
        }

        List<List<String>> filters = new ArrayList<>();
        List<String> filters0 = new ArrayList<>();
        List<String> filters1 = new ArrayList<>();
        List<String> filters2 = new ArrayList<>();
        List<String> filters3 = new ArrayList<>();
        List<String> filters4 = new ArrayList<>();

        Cursor cursor = database.rawQuery("Select description from itemtable where itemtype = ?", new String[]{itemtype});
        String tempstring = "";
        String tempcyclestring = "";

        if (cursor.moveToFirst()) {
            int itemdescriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
            do {
                tempstring = cursor.getString(itemdescriptionIndex);
                if (tempstring.contains(filtersettings[0].trim())) {
                    tempcyclestring = cursor.getString(itemdescriptionIndex).substring(tempstring.indexOf(filtersettings[0])).split(";")[0].replace(filtersettings[0], "");
                    if (!filters0.contains(tempcyclestring)) {
                        filters0.add(tempcyclestring);
                    }
                }
                if (tempstring.contains(filtersettings[1].trim())) {
                    tempcyclestring = cursor.getString(itemdescriptionIndex).substring(tempstring.indexOf(filtersettings[1].trim())).split(";")[0].replace(filtersettings[1], "");
                    if (!filters1.contains(tempcyclestring)) {
                        filters1.add(tempcyclestring);
                    }
                }
                if (tempstring.contains(filtersettings[2].trim())) {
                    tempcyclestring = cursor.getString(itemdescriptionIndex).substring(tempstring.indexOf(filtersettings[2].trim())).split(";")[0].replace(filtersettings[2], "");
                    if (!filters2.contains(tempcyclestring)) {
                        filters2.add(tempcyclestring);
                    }
                }
                if (tempstring.contains(filtersettings[3].trim())) {
                    tempcyclestring = cursor.getString(itemdescriptionIndex).substring(tempstring.indexOf(filtersettings[3].trim())).split(";")[0].replace(filtersettings[3], "");
                    if (!filters3.contains(tempcyclestring)) {
                        filters3.add(tempcyclestring);
                    }
                }
                if (tempstring.contains(filtersettings[4].trim())) {
                    tempcyclestring = cursor.getString(itemdescriptionIndex).substring(tempstring.indexOf(filtersettings[4].trim())).split(";")[0].replace(filtersettings[4], "");
                    if (!filters4.contains(tempcyclestring)) {
                        filters4.add(tempcyclestring);
                    }
                }
            }
            while (cursor.moveToNext());
        } else {
        }
        filters.add(filters0);
        filters.add(filters1);
        filters.add(filters2);
        filters.add(filters3);
        filters.add(filters4);

        // Adding header data
        listDataHeader.add(filtersettings[0]);
        listDataHeader.add(filtersettings[1]);
        listDataHeader.add(filtersettings[2]);
        listDataHeader.add(filtersettings[3]);
        listDataHeader.add(filtersettings[4]);

        CheckBox tempcheck;
        for (int i = 0; i < filters.size(); i++) {
            List<CheckBox> tempchecklist = new ArrayList<>();
            for (int j = 0; j < filters.get(i).size(); j++) {
                tempcheck = new CheckBox(this);
                tempcheck.setText(filters.get(i).get(j));
                tempcheck.setChecked(false);
                tempchecklist.add(tempcheck);
            }
            listDataChild.put(listDataHeader.get(i), tempchecklist);
        }
    }



    /**
     * Получение списка товаров определённого типа без использования фильтров
     * @param itemtype тип товара
     * @param db база данных
     * @return  лист товаров определённого типа
     */
    public List<item> rawqueries(String itemtype, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("Select * from itemtable where itemtype = ? order by itemname", new String[]{itemtype});
        List<item> res = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int itemidIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int itemtypeIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMTYPE);
            int itemnameIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMNAME);
            int itemcountIndex = cursor.getColumnIndex(DBHelper.KEY_COUNT);
            int itemdescriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);

            do {

                res.add(new item(cursor.getInt(itemidIndex), cursor.getString(itemnameIndex), cursor.getString(itemtypeIndex), cursor.getString(itemcountIndex), cursor.getString(itemdescriptionIndex)));
            }
            while (cursor.moveToNext());
        } else {
        }
        return res;
    }

    /**
     * Генерация запросов в соответствии с выбранными фильтрами
     * @param itemtype тип товара
     * @param searcher поле поиска
     * @param listheader коллекция групп фильтров поиска
     * @param listchild коллекция членов групп фильтров поиска
     * @return массив с телом запросов
     */
    public String[] preparefilters(String itemtype,EditText searcher,List<String> listheader,HashMap<String,List<CheckBox>> listchild) {
        String[] selection = new String[2];
        selection[0] = DBHelper.KEY_ITEMTYPE + " =" + "'" + itemtype + "'";
        String filterBuilder = " AND instr(itemname," + "'" + searcher.getText().toString() + "'" + ") > 0";
        String tempstr = "";
        for (int i = 0; i < listheader.size(); i++) {

            for (int j = 0; j < listchild.get(listheader.get(i)).size(); j++) {
                if (listchild.get(listheader.get(i)).get(j).isChecked()) {
                    tempstr = listchild.get(listheader.get(i)).get(j).getText().toString();
                    filterBuilder += " AND instr(description," + "'" + tempstr + "'" + ") > 0";
                }
            }
        }
        selection[1] = filterBuilder;

        return selection;

    }

    /**
     * Получение списка товаров определенного типа с использованием фильтров
     * @param db база данных
     * @param selection массив с телом запроса
     * @return лист товаров определённого типа в соответствии с фильтрами
     */
    public List<item> filteredqueries(SQLiteDatabase db,String[] selection) {
        Cursor cursor = db.query(DBHelper.TABLE_WAREHOUSE, null, selection[0] + selection[1], null, null, null, "itemname");
        List<item> res = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int itemidIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int itemtypeIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMTYPE);
            int itemnameIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMNAME);
            int itemcountIndex = cursor.getColumnIndex(DBHelper.KEY_COUNT);
            int itemdescriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);

            do {

                res.add(new item(cursor.getInt(itemidIndex), cursor.getString(itemnameIndex), cursor.getString(itemtypeIndex), cursor.getString(itemcountIndex), cursor.getString(itemdescriptionIndex)));
            }
            while (cursor.moveToNext());
        } else {
        }
        return res;
    }

    /**
     * Получение коллекции фильтров в соответствии с типом товара
     * @param action
     * @param itemtypes
     * @return
     */
    public String switchAction(String action, String[] itemtypes) {
        if (action.equals("com.example.warehouseproject.Code.processorsActivity")) {
            filtersettings = getResources().getStringArray(R.array.processorfilters);
            return itemtypes[0];
        } else if (action.equals("com.example.warehouseproject.Code.motherboardsActivity")) {
            filtersettings = getResources().getStringArray(R.array.motherboardfilters);
            return itemtypes[1];
        } else if (action.equals("com.example.warehouseproject.Code.videocardsActivity")) {
            filtersettings = getResources().getStringArray(R.array.videocardfilters);
            return itemtypes[2];
        } else if (action.equals("com.example.warehouseproject.Code.ramActivity")) {
            filtersettings = getResources().getStringArray(R.array.ramfilters);
            return itemtypes[3];
        } else if (action.equals("com.example.warehouseproject.Code.powersupplyActivity")) {
            filtersettings = getResources().getStringArray(R.array.powersupplyfilters);
            return itemtypes[4];
        } else if (action.equals("com.example.warehouseproject.Code.bodiesActivity")) {
            filtersettings = getResources().getStringArray(R.array.bodiesfilters);
            return itemtypes[5];
        } else if (action.equals("com.example.warehouseproject.Code.serverthingsActivity")) {
            filtersettings = new String[]{""};
            return itemtypes[6];
        } else if (action.equals("com.example.warehouseproject.Code.coolingActivity")) {
            filtersettings = new String[]{""};
            return itemtypes[7];
        } else if (action.equals("com.example.warehouseproject.Code.ssdsActivity")) {
            filtersettings = getResources().getStringArray(R.array.ssdfilters);
            return itemtypes[8];
        } else if (action.equals("com.example.warehouseproject.Code.hddsActivity")) {
            filtersettings = getResources().getStringArray(R.array.hddfilters);
            return itemtypes[9];
        } else if (action.equals("com.example.warehouseproject.Code.miscfordrivesActivity")) {
            filtersettings = new String[]{""};
            return itemtypes[10];
        } else if (action.equals("com.example.warehouseproject.Code.expansiondevicesActivity")) {
            filtersettings = new String[]{""};
            return itemtypes[11];
        } else {
            return "";
        }

    }

    /**
     * Изменение форм в соответствии с фильтрами
     */
    public void updateformsAccordingfilters() {

        selectionBuilder =  preparefilters(itemtype,searchF,listDataHeader,listDataChild);
        queryResults =  filteredqueries(database,selectionBuilder);
        paginator = new Paginator((ArrayList) queryResults);
        totalpages = paginator.getTotalPages();
        currentpage = 0;
        toggleButtons();
        bindData(currentpage);
        ehgrid.setExpanded(true);
    }

    /**
     * Изменение форм используя значения "по умолчанию"
     */
    public void updateformsDefault(){
        queryResults =  rawqueries(itemtype,database);
        paginator = new Paginator((ArrayList) queryResults);
        totalpages = paginator.getTotalPages();
        currentpage = 0;
        toggleButtons();
        bindData(currentpage);
        ehgrid.setExpanded(true);
    }


    //region buttonSection
    /**
     * Изменение состояний кнопок, используемых для изменения текущей страницы списка товаров
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

    /**
     * Изменение состояний форм фильтров и списка товаров
     * @param view
     */
    public void showFiltersonClick(View view) {
        settings.setEnabled(!settings.isEnabled());
        switch (settings.getVisibility()) {
            case View.VISIBLE:
                settings.setVisibility(View.INVISIBLE);
                settings.setFocusable(false);
                settings.setItemsCanFocus(false);
                settings.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                settings.setEnabled(false);

                ehgrid.setFocusable(true);
                ehgrid.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                ehgrid.setEnabled(true);
                ehgrid.setVisibility(View.VISIBLE);
                break;
            case View.INVISIBLE:
                ehgrid.setFocusable(false);
                ehgrid.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                ehgrid.setEnabled(false);
                ehgrid.setVisibility(View.INVISIBLE);

                settings.setFocusable(true);
                settings.setItemsCanFocus(true);
                settings.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                settings.setEnabled(true);
                settings.setVisibility(View.VISIBLE);
                break;
        }
    }




    /**
     * Событие нажатия кнопки "предыдущая страница" списка товаров
     * @param view
     */
    public void prevbtnClick(View view) {
        currentpage--;
        toggleButtons();
        bindData(currentpage);
    }

    /**
     * Событие нажатия кнопки "следующая страница" списка товаров
     * @param view
     */
    public void nextbtnClick(View view) {
        currentpage++;
        toggleButtons();
        bindData(currentpage);
    }
    //endregion


}