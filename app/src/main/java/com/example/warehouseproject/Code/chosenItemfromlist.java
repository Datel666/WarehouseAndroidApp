package com.example.warehouseproject.Code;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.warehouseproject.Code.item;
import com.example.warehouseproject.R;
import com.example.warehouseproject.utilityClasses.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class chosenItemfromlist extends AppCompatActivity {

    //region variables
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
    private EditText itemvendor;

    private Button performoperation;
    private Button changeinformation;
    private Button decline;
    private Button apply;
    private Button deleteitem;

    private Spinner operationtype;

    private ImageView itemImage;
//endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_itemfromlist);

        initialiseViews();
        initializeValues();
        fillforms(Item);
    }

    //region utility
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
        itemvendor = (EditText) findViewById(R.id.vendorEdit);

        performoperation = (Button) findViewById(R.id.performOperationBtn);
        changeinformation = (Button) findViewById(R.id.changeinformationBtn);
        decline = (Button) findViewById(R.id.declineBtn);
        apply = (Button) findViewById(R.id.applyBtn);
        deleteitem = (Button) findViewById(R.id.deleteitemBtn);

        itemImage = (ImageView) findViewById(R.id.itemPhoto);
        operationtype = (Spinner) findViewById(R.id.operationChooser);
    }

    private void fillforms(item info) {
        itemname.setText(info.name);
        itemdescription.setText(info.description);
        itemcount.setText(info.count);
        itemtype.setText(info.type);
        itemImage.setImageBitmap(bytetoimage(info.photo));
    }
    private Bitmap bytetoimage(byte[] bytearr){
        Bitmap bmp = BitmapFactory.decodeByteArray(bytearr, 0, bytearr.length);
        return bmp;
    }

    private void updateforms(item info) {
        Item = info;
        fillforms(Item);
    }

    private void setformsenabled(boolean enabled) {
        decline.setEnabled(enabled);
        apply.setEnabled(enabled);
        itemname.setEnabled(enabled);
        itemdescription.setEnabled(enabled);
        changeinformation.setEnabled(!enabled);
    }

    //endregion

    //region queriesLogic
    private item getitemInformation(int itemid) {
        Cursor cursor;
        item ItemInfo;
        if (!database.isOpen()) {
            database = helper.getWritableDatabase();
        }
        String queryString = "SELECT * FROM " + DBHelper.TABLE_WAREHOUSE + " WHERE " + DBHelper.KEY_ID + "=" + String.valueOf(itemid);
        cursor = database.rawQuery(queryString, null);

        cursor.moveToFirst();
        int itemidIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
        int itemtypeIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMTYPE);
        int itemnameIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMNAME);
        int itemcountIndex = cursor.getColumnIndex(DBHelper.KEY_COUNT);
        int itemphotoIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMPHOTO);
        int itemdescriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);

        ItemInfo = new item(cursor.getInt(itemidIndex), cursor.getString(itemnameIndex), cursor.getString(itemtypeIndex), cursor.getString(itemcountIndex), cursor.getString(itemdescriptionIndex),cursor.getBlob(itemphotoIndex));
        return ItemInfo;
    }

    private void updateitemCount(int itemid) {
        String queryString = "UPDATE " + DBHelper.TABLE_WAREHOUSE + " SET " + DBHelper.KEY_COUNT + "=" + String.valueOf(Integer.parseInt(itemcount.getText().toString()) + Integer.parseInt(operationcount.getText().toString())) +
                " WHERE " + DBHelper.KEY_ID + " = " + String.valueOf(itemid);
        database.rawQuery(queryString, null);
    }

    private void updateitemInfo(int itemid) {
        String queryString = "UPDATE " + DBHelper.TABLE_WAREHOUSE + " SET " + DBHelper.KEY_ITEMNAME + "=" + itemname.getText().toString() + "," + DBHelper.KEY_DESCRIPTION + "=" + itemdescription.getText().toString() +
                " WHERE " + DBHelper.KEY_ID + " = " + String.valueOf(itemid);
        database.rawQuery(queryString, null);
    }


    private void updateSupplyTable(int itemid) {
        String operation = "";
        String size = operationtype.getSelectedItem().toString();

        switch (size) {
            case "Импорт товара":
                operation = "+";
                break;
            case "Экспорт товара":
                operation = "-";
                break;
            default:
                operation = "";
        }

        ContentValues supplyvalues = new ContentValues();
        supplyvalues.put(DBHelper.KEY_SUPPLYTYPE, operation);
        supplyvalues.put(DBHelper.KEY_ITEMVENDOR, itemvendor.getText().toString());
        supplyvalues.put(DBHelper.KEY_COUNT2, operationcount.getText().toString());
        supplyvalues.put(DBHelper.KEY_DATE, System.currentTimeMillis());
        supplyvalues.put("itemid", itemid);
        database.insert(DBHelper.TABLE_SUPPLY, null, supplyvalues);
    }

    private void deleteItem(int itemid){
        database.delete(DBHelper.TABLE_WAREHOUSE,DBHelper.KEY_ID + "=?",new String[]{String.valueOf(itemid)});
    }

    //endregion

    //region BtnsRegion
    public void performOperationClick(View view) {

        if (!database.isOpen()) {
            database = helper.getReadableDatabase();
        }
        if (itemvendor.getText().toString().length() != 0 && operationcount.getText().toString().length() != 0) {
            try {
                database.beginTransaction();
                updateitemCount(Item.id);
                updateSupplyTable(Item.id);
                database.setTransactionSuccessful();
            } catch (Exception ex) {
                Toast toast = Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } finally {
                database.endTransaction();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Данные не указаны или указаны не верно", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        Item = getitemInformation(Item.id);
        updateforms(Item);
    }



    public void changeinformationClick(View view) {
        changeinformation.setEnabled(false);
        decline.setEnabled(true);
        apply.setEnabled(true);
        itemname.setEnabled(true);
        itemdescription.setEnabled(true);
    }

    public void declineClick(View view) {
        setformsenabled(false);
        fillforms(Item);
    }

    public void applyClick(View view) {
        setformsenabled(false);

        if (!database.isOpen()) {
            database = helper.getWritableDatabase();
        }
        try {
            database.beginTransaction();
            updateitemInfo(Item.id);
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } finally {
            database.endTransaction();
        }

        Item = getitemInformation(Item.id);
        updateforms(Item);
    }

    public void deleteClick(View view) {
        if(!database.isOpen()){
            database = helper.getWritableDatabase();
        }
        try{
            database.beginTransaction();
            deleteItem(Item.id);
            database.setTransactionSuccessful();
        }
        catch(Exception ex){
            Toast toast = Toast.makeText(getApplicationContext(),ex.getMessage().toString(),Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
        finally{
            database.endTransaction();
            this.finish();
        }
    }
    //endregion

}