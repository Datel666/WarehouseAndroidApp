package com.example.warehouseproject.Code;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.warehouseproject.Code.item;
import com.example.warehouseproject.R;
import com.example.warehouseproject.utilityClasses.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class chosenItemfromlist extends AppCompatActivity {

    private Intent intent;
    private String action;
    private String[] itemtypes;
    private DBHelper helper;
    private SQLiteDatabase database;
    private item Item;

    private EditText itemname;
    private EditText itemdescription;
    private EditText itemcount;
    private EditText operationcount;
    private EditText itemtype;

    private Button performoperation;
    private Button changeinformation;
    private Button decline;
    private Button apply;
    private Button deleteitem;

    private ImageView itemImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_itemfromlist);

        initialiseViews();
        initializeValues();
        fillforms(Item);
    }

    private void initializeValues() {

        helper = new DBHelper(this);

        database = helper.getWritableDatabase();
        itemtypes = getResources().getStringArray(R.array.itemTypes);
        intent = getIntent();
        action = intent.getAction();
        Item = getitemInformation(Integer.parseInt(intent.getStringExtra("itemid")));

    }

    private void initialiseViews() {
        itemname = (EditText) findViewById(R.id.itemnameEdit);
        itemdescription = (EditText) findViewById(R.id.descriptionEdit);
        itemcount = (EditText) findViewById(R.id.itemcountEdit);
        operationcount = (EditText) findViewById(R.id.inputCountEditText);
        itemtype = (EditText) findViewById(R.id.itemtypeEdit);

        performoperation = (Button) findViewById(R.id.performOperationBtn);
        changeinformation = (Button) findViewById(R.id.changeinformationBtn);
        decline = (Button) findViewById(R.id.declineBtn);
        apply = (Button) findViewById(R.id.applyBtn);
        deleteitem = (Button) findViewById(R.id.deleteitemBtn);

        itemImage = (ImageView) findViewById(R.id.itemPhoto);


    }

    private void fillforms(item info) {
        itemname.setText(info.name);
        itemdescription.setText(info.description);
        itemcount.setText(info.count);
        itemtype.setText(info.type);

    }

    private item getitemInformation(int itemid) {
        Cursor cursor;
        item ItemInfo;
        if (!database.isOpen()) {
            database = helper.getWritableDatabase();
        }
        String queryString = "SELECT * FROM " + DBHelper.TABLE_WAREHOUSE + " WHERE " + DBHelper.KEY_ID + "=" + String.valueOf(itemid);
        cursor = database.rawQuery(queryString,null);

        cursor.moveToFirst();
        int itemidIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
        int itemtypeIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMTYPE);
        int itemnameIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMNAME);
        int itemcountIndex = cursor.getColumnIndex(DBHelper.KEY_COUNT);
        int itemdescriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);

        ItemInfo = new item(cursor.getInt(itemidIndex), cursor.getString(itemnameIndex), cursor.getString(itemtypeIndex), cursor.getString(itemcountIndex), cursor.getString(itemdescriptionIndex));
        return ItemInfo;
    }


}