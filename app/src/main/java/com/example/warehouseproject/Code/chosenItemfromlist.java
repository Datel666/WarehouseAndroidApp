package com.example.warehouseproject.Code;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.warehouseproject.R;
import com.example.warehouseproject.utilityClasses.DBHelper;
import com.example.warehouseproject.utilityClasses.QRHelper;
import com.example.warehouseproject.utilityClasses.QueriesProcessor;

import java.util.logging.Logger;

/**
 * chosenItemfromlist class
 *
 * Класс содержит функционал для chosenItemfromlist layout
 */
public class chosenItemfromlist extends AppCompatActivity {

    //region variables
    private Intent intent;
    private String action;
    private String[] itemtypes;
    private DBHelper helper;
    private QueriesProcessor qprocessor;
    private SQLiteDatabase database;
    private QRHelper qhelper;
    private com.example.warehouseproject.Code.Item Item;

    private EditText itemname;
    private EditText itemdescription;
    private EditText itemcount;
    private EditText operationcount;
    private EditText itemtype;
    private EditText itemvendor;


    private Button changeinformation;
    private Button decline;
    private Button apply;

    private Spinner operationtype;
    private ImageView itemImage;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_itemfromlist);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initialiseViews();
        initializeValues();
        fillforms(Item);
        operationtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String size = operationtype.getSelectedItem().toString();
                switch (size) {
                    case "Импорт товара":
                        itemvendor.setEnabled(true);
                        break;
                    case "Экспорт товара":
                        itemvendor.setEnabled(false);
                        itemvendor.setText("");
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //region utility

    /**
     * Инициализация значений переменных
     */
    private void initializeValues() {
        helper = new DBHelper(this);
        qprocessor = new QueriesProcessor();
        database = helper.getWritableDatabase();
        itemtypes = getResources().getStringArray(R.array.itemTypes);
        intent = getIntent();
        action = intent.getAction();
        Item = qprocessor.getitemInformation(Integer.parseInt(intent.getStringExtra("itemid")), database);
    }

    /**
     * Иницализация форм
     */
    private void initialiseViews() {
        itemname = (EditText) findViewById(R.id.itemnameEdit);
        itemdescription = (EditText) findViewById(R.id.descriptionEdit);
        itemcount = (EditText) findViewById(R.id.itemcountEdit);
        operationcount = (EditText) findViewById(R.id.inputCountEditText);
        itemtype = (EditText) findViewById(R.id.itemtypeEdit);
        itemvendor = (EditText) findViewById(R.id.vendorEdit);


        changeinformation = (Button) findViewById(R.id.changeinformationBtn);
        decline = (Button) findViewById(R.id.declineBtn);
        apply = (Button) findViewById(R.id.applyBtn);

        itemImage = (ImageView) findViewById(R.id.itemPhoto);
        operationtype = (Spinner) findViewById(R.id.operationChooser);
    }

    /**
     * Первичное заполнение форм информацией о текущем товаре
     * @param info информация о товаре
     */
    private void fillforms(com.example.warehouseproject.Code.Item info) {
        itemname.setText(info.name);
        itemdescription.setText(info.description);
        itemcount.setText(info.count);
        itemtype.setText(info.type);
        itemImage.setImageBitmap(bytetoimage(info.photo));
    }

    /**
     * Обновление информации о товаре, находящейся в формах приложения
     * @param info информация о товаре
     */
    private void updateforms(com.example.warehouseproject.Code.Item info) {
        Item = info;
        fillforms(Item);
    }

    /**
     * Переключение режима форм между в активный режим и наоборот
     * @param enabled значение свойства активности
     */
    private void setformsenabled(boolean enabled) {
        decline.setEnabled(enabled);
        apply.setEnabled(enabled);
        itemname.setEnabled(enabled);
        itemdescription.setEnabled(enabled);
        changeinformation.setEnabled(!enabled);
    }
    //endregion

    //region easyqueriesLogic

    /**
     *  Логика запроса по обновлению значения количества товара в базе данных товаров
     * @param itemid идентификатор товара
     */
    private void updateitemCount(int itemid,String operation) {

        if(operation.equals("+")) {
            if (itemvendor.getText().toString().length() != 0 && operationcount.getText().toString().length() != 0) {
                String queryString = "UPDATE " + DBHelper.TABLE_WAREHOUSE + " SET " + DBHelper.KEY_COUNT + "="
                        + String.valueOf(Integer.parseInt(itemcount.getText().toString())
                        + Integer.parseInt(operationcount.getText().toString())) +
                        " WHERE " + DBHelper.KEY_ID + " = " + String.valueOf(itemid);
                database.rawQuery(queryString, null);
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(),"Заполните все необходимые поля", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        }
        else if (operation.equals("-")){
            if (operationcount.getText().toString().length() != 0) {
                String queryString = "UPDATE " + DBHelper.TABLE_WAREHOUSE + " SET " + DBHelper.KEY_COUNT + "="
                        + String.valueOf(Integer.parseInt(itemcount.getText().toString())
                        + Integer.parseInt(operationcount.getText().toString())) +
                        " WHERE " + DBHelper.KEY_ID + " = " + String.valueOf(itemid);
                database.rawQuery(queryString, null);
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(),"Заполните все необходимые поля", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        }
    }

    /**
     * Логика запроса по обновлению значений названия и описания товара в базе данных товаров
     * @param itemid
     */
    private void updateitemInfo(int itemid) {
        if(itemname.getText().toString().length()>4) {
            String queryString = "UPDATE " + DBHelper.TABLE_WAREHOUSE + " SET "
                    + DBHelper.KEY_ITEMNAME + "=" + itemname.getText().toString()
                    + "," + DBHelper.KEY_DESCRIPTION + "="
                    + itemdescription.getText().toString() +
                    " WHERE " + DBHelper.KEY_ID + " = " + String.valueOf(itemid);
            database.rawQuery(queryString, null);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),"Заполните все необходимые поля",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    /**
     * Логика создания новой записи импорта или экспорта товара в базе данных поставок
     * @param itemid идентификатор товара
     */
    private void updateSupplyTable(int itemid,String operation) {

        if(operation.equals("+")) {
            if (itemvendor.getText().toString().length() != 0 && operationcount.getText().toString().length() != 0) {
                ContentValues supplyvalues = new ContentValues();
                supplyvalues.put(DBHelper.KEY_SUPPLYTYPE, operation);
                supplyvalues.put(DBHelper.KEY_ITEMVENDOR, itemvendor.getText().toString());
                supplyvalues.put(DBHelper.KEY_COUNT2, operationcount.getText().toString());
                supplyvalues.put(DBHelper.KEY_DATE, System.currentTimeMillis());
                supplyvalues.put("itemid", itemid);
                database.insert(DBHelper.TABLE_SUPPLY, null, supplyvalues);
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(),"Заполните все необходимые поля", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        }
        else if (operation.equals("-")){
            if (operationcount.getText().toString().length() != 0) {
                ContentValues supplyvalues = new ContentValues();
                supplyvalues.put(DBHelper.KEY_SUPPLYTYPE, operation);
                supplyvalues.put(DBHelper.KEY_ITEMVENDOR, itemvendor.getText().toString());
                supplyvalues.put(DBHelper.KEY_COUNT2, operationcount.getText().toString());
                supplyvalues.put(DBHelper.KEY_DATE, System.currentTimeMillis());
                supplyvalues.put("itemid", itemid);
                database.insert(DBHelper.TABLE_SUPPLY, null, supplyvalues);
            }
            }
            else{
            Toast toast = Toast.makeText(getApplicationContext(),"Заполните все необходимые поля", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    /**
     * Логика удаления товара из базы данных товаров
     * @param itemid идентификатор товара
     */
    private void deleteItem(int itemid) {
        database.delete(DBHelper.TABLE_WAREHOUSE, DBHelper.KEY_ID + "=?", new String[]{String.valueOf(itemid)});
    }

    //endregion

    //region BtnsRegion

    /**
     * Обработщик события нажатия на кнопку выполнить операцию импорта или экспорта
     * @param view ссылка на кнопку
     */
    public void performOperationClick(View view) {

        if (!database.isOpen()) {
            database = helper.getReadableDatabase();
        }

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

            try {
                database.beginTransaction();
                updateitemCount(Item.id,operation);
                updateSupplyTable(Item.id,operation);
                database.setTransactionSuccessful();
            } catch (Exception ex) {
                Toast toast = Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } finally {
                database.endTransaction();
            }



        Item = qprocessor.getitemInformation(Item.id, database);
        updateforms(Item);
    }

    /**
     * Обработчик события нажатия на кнопку изменить информацию о товаре
     * @param view ссылка на кнопку
     */
    public void changeinformationClick(View view) {
        changeinformation.setEnabled(false);
        decline.setEnabled(true);
        apply.setEnabled(true);
        itemname.setEnabled(true);
        itemdescription.setEnabled(true);
    }

    /**
     * Обработчик события нажатия на кнопку отменить изменения имени и описания товара
     * @param view ссылка на кнопку
     */
    public void declineClick(View view) {
        setformsenabled(false);
        fillforms(Item);
    }

    /**
     * Обработчик события нажатия на кнопку подтвердить изменения имени и описания товара
     * @param view ссылка на кнопку
     */
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

        Item = qprocessor.getitemInformation(Item.id, database);
        updateforms(Item);
    }

    /**
     * Обработчик события нажатия на кнопку удалить товар из базы данных
     * @param view ссылка на кнопку
     */
    public void deleteClick(View view) {
        if (!database.isOpen()) {
            database = helper.getWritableDatabase();
        }
        try {
            database.beginTransaction();
            deleteItem(Item.id);
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } finally {
            database.endTransaction();
            this.finish();
        }
    }
    //endregion

    public Bitmap bytetoimage(byte[] bytearr){
        Bitmap bmp = BitmapFactory.decodeByteArray(bytearr, 0, bytearr.length);
        return bmp;
    }

}