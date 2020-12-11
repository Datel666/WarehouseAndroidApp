package com.example.warehouseproject.Fragments;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.warehouseproject.utilityClasses.DBHelper;
import com.example.warehouseproject.utilityClasses.QRHelper;
import com.example.warehouseproject.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class NewItemFragment extends Fragment {


    SQLiteDatabase database;
    DBHelper helper;
    QRHelper qrhelper;

    Cursor c;
    View view;
    Context act;
    EditText itemname;
    Spinner itemtype;
    EditText itemcount;
    EditText itemcreator;
    EditText itemvendor;
    EditText itemdescription;
    EditText date;
    Button newitemBtn;
    String temptext;
    ImageView imgv;
    String currentItemtype = "Процессор";
    String[] filters;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_newitem, container, false);


        helper = new DBHelper(act);
        database = helper.getWritableDatabase();

        filters = getResources().getStringArray(R.array.processorfilters);


        itemname = (EditText) view.findViewById(R.id.itemName);
        itemtype = (Spinner) view.findViewById(R.id.itemType);
        itemcount = (EditText) view.findViewById(R.id.itemCount);
        itemcreator = (EditText) view.findViewById(R.id.itemCreator);
        itemvendor = (EditText) view.findViewById(R.id.itemVendor);
        itemdescription = (EditText) view.findViewById(R.id.itemDescription);

        newitemBtn = (Button) view.findViewById(R.id.addItemBtn);
        itemdescription.setText(filters[0]);
        itemdescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!filters[0].equals("")){
                if (s.toString().contains(filters[0]) && count(s.toString(), ";") == 1 && !s.toString().contains(filters[1])) {
                    itemdescription.removeTextChangedListener(this);
                    s.append(filters[1]);
                    itemdescription.addTextChangedListener(this);
                    return;
                }
                if (s.toString().contains(filters[1]) && count(s.toString(), ";") == 2 && !s.toString().contains(filters[2])) {
                    itemdescription.removeTextChangedListener(this);
                    s.append(filters[2]);
                    itemdescription.addTextChangedListener(this);
                    return;
                }
                if (s.toString().contains(filters[2]) && count(s.toString(), ";") == 3 && !s.toString().contains(filters[3])) {
                    itemdescription.removeTextChangedListener(this);
                    s.append(filters[3]);
                    itemdescription.addTextChangedListener(this);
                    return;
                }
                if (s.toString().contains(filters[3]) && count(s.toString(), ";") == 4 && !s.toString().contains(filters[4])) {
                    itemdescription.removeTextChangedListener(this);
                    s.append(filters[4]);
                    itemdescription.addTextChangedListener(this);
                    return;
                }
                if(s.toString().contains("~")){
                    itemdescription.removeTextChangedListener(this);
                    itemdescription.setText(filters[0]);
                    itemdescription.addTextChangedListener(this);
                    return;
                }
                }


            }
        });



        itemtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String[] choose = getResources().getStringArray(R.array.itemTypes);
                currentItemtype = choose[position];
                if(currentItemtype.equals("Процессор")){
                    filters = getResources().getStringArray(R.array.processorfilters);
                }
                else if (currentItemtype.equals("Материнская плата")){
                    filters = getResources().getStringArray(R.array.motherboardfilters);
                }
                else if (currentItemtype.equals( "Видеокарта")){
                    filters = getResources().getStringArray(R.array.videocardfilters);
                }
                else if (currentItemtype.equals( "Оперативная память")){
                    filters = getResources().getStringArray(R.array.ramfilters);
                }
                else if (currentItemtype.equals( "Блок питания")){
                    filters = getResources().getStringArray(R.array.powersupplyfilters);
                }
                else if (currentItemtype.equals( "Корпус")){
                    filters = getResources().getStringArray(R.array.bodiesfilters);
                }
                else if (currentItemtype.equals( "SSD накопитель")){
                    filters = getResources().getStringArray(R.array.ssdfilters);
                }
                else if (currentItemtype.equals( "Жесткий диск")){
                    filters = getResources().getStringArray(R.array.hddfilters);
                }
                else{
                    filters[0] = "";
                }
                itemdescription.setText("");
                itemdescription.append(filters[0]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        newitemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_QR, printQR(Long.toString(getProfilesCount())));


                contentValues.put(DBHelper.KEY_ITEMTYPE, currentItemtype);
                contentValues.put(DBHelper.KEY_ITEMNAME, itemname.getText().toString());
                contentValues.put(DBHelper.KEY_COUNT, itemcount.getText().toString());
                contentValues.put(DBHelper.KEY_DESCRIPTION, itemdescription.getText().toString());
                database.insert(DBHelper.TABLE_WAREHOUSE, null, contentValues);

                ContentValues supplyvalues = new ContentValues();
                supplyvalues.put(DBHelper.KEY_SUPPLYTYPE, "+");
                supplyvalues.put(DBHelper.KEY_ITEMVENDOR, itemvendor.getText().toString());
                supplyvalues.put(DBHelper.KEY_COUNT2, itemcount.getText().toString());
                supplyvalues.put(DBHelper.KEY_DATE, System.currentTimeMillis());
                supplyvalues.put("itemid", getProfilesCount());
                database.insert(DBHelper.TABLE_SUPPLY, null, supplyvalues);

                Toast toast = Toast.makeText(act, "Товар успешно добавлен", Toast.LENGTH_SHORT);
                clearforms();

            }
        });

        return view;
    }

    public static int count(String str, String target) {
        return (str.length() - str.replace(target, "").length()) / target.length();
    }

    public void clearforms() {
        itemname.setText("");
        itemcreator.setText("");
        itemvendor.setText("");
        itemcount.setText("");
        itemdescription.setText("");
    }

    public long getProfilesCount() {
        long count;
        c = database.rawQuery("SELECT COALESCE(MAX(itemid), 1) AS pls FROM itemtable", null);

        c.moveToFirst();
        count = c.getInt(c.getColumnIndex("pls"));
        return count;
    }

    public String printQR(String id) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(id, BarcodeFormat.QR_CODE, 200, 200);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return qrhelper.encodeTobase64(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        act = context;
    }
}
