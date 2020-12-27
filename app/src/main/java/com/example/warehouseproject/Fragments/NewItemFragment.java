package com.example.warehouseproject.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.warehouseproject.utilityClasses.DBHelper;
import com.example.warehouseproject.utilityClasses.QRHelper;
import com.example.warehouseproject.R;

import com.example.warehouseproject.utilityClasses.QueriesProcessor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * NewItemFragment class
 * <p>
 * Описывает функционал для fragment_newitem layout
 */
public class NewItemFragment extends Fragment {

    //region variables
    SQLiteDatabase database;
    DBHelper helper;
    QRHelper qrhelper;
    QueriesProcessor qprocessor;

    Cursor c;
    View view;
    Context act;
    EditText itemname;
    Spinner itemtype;
    EditText itemcount;

    EditText itemvendor;
    EditText itemdescription;
    Button newitemBtn;
    Button choosephotoBtn;
    String currentItemtype = "Процессор";
    String[] filters;

    ImageView itemphoto;
    Bitmap itemphotobytes;
    ByteArrayOutputStream baos;
    //endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_newitem, container, false);

        initializeValues();
        initializeViews();

        itemdescription.setText(filters[0]);

        // Слушатель события изменения текта в поле описания товара
        itemdescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!filters[0].equals("")) {
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
                    if (s.toString().contains("~")) {
                        itemdescription.removeTextChangedListener(this);
                        itemdescription.setText(filters[0]);
                        itemdescription.addTextChangedListener(this);
                        return;
                    }
                }
            }
        });

        // Слушатель события изменения типа товара посредством spinner
        itemtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String[] choose = getResources().getStringArray(R.array.itemTypes);
                currentItemtype = choose[position];
                if (currentItemtype.equals("Процессор")) {
                    filters = getResources().getStringArray(R.array.processorfilters);
                    itemphotobytes = BitmapFactory.decodeResource(getResources(), R.drawable.processors);
                    itemphoto.setImageBitmap(itemphotobytes);
                } else if (currentItemtype.equals("Материнская плата")) {
                    filters = getResources().getStringArray(R.array.motherboardfilters);
                    itemphotobytes = BitmapFactory.decodeResource(getResources(), R.drawable.motherboards);
                    itemphoto.setImageBitmap(itemphotobytes);
                } else if (currentItemtype.equals("Видеокарта")) {
                    filters = getResources().getStringArray(R.array.videocardfilters);
                    itemphotobytes = BitmapFactory.decodeResource(getResources(), R.drawable.videocards);
                    itemphoto.setImageBitmap(itemphotobytes);
                } else if (currentItemtype.equals("Оперативная память")) {
                    filters = getResources().getStringArray(R.array.ramfilters);
                    itemphotobytes = BitmapFactory.decodeResource(getResources(), R.drawable.ram);
                    itemphoto.setImageBitmap(itemphotobytes);
                } else if (currentItemtype.equals("Блок питания")) {
                    filters = getResources().getStringArray(R.array.powersupplyfilters);
                    itemphotobytes = BitmapFactory.decodeResource(getResources(), R.drawable.powersupply);
                    itemphoto.setImageBitmap(itemphotobytes);
                } else if (currentItemtype.equals("Корпус")) {
                    filters = getResources().getStringArray(R.array.bodiesfilters);
                    itemphotobytes = BitmapFactory.decodeResource(getResources(), R.drawable.bodies);
                    itemphoto.setImageBitmap(itemphotobytes);
                } else if (currentItemtype.equals("SSD накопитель")) {
                    filters = getResources().getStringArray(R.array.ssdfilters);
                    itemphotobytes = BitmapFactory.decodeResource(getResources(), R.drawable.ssds);
                    itemphoto.setImageBitmap(itemphotobytes);
                } else if (currentItemtype.equals("Жесткий диск")) {
                    filters = getResources().getStringArray(R.array.hddfilters);
                    itemphotobytes = BitmapFactory.decodeResource(getResources(), R.drawable.hdds);
                    itemphoto.setImageBitmap(itemphotobytes);
                } else {
                    filters[0] = "";
                    itemphotobytes = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
                    itemphoto.setImageBitmap(itemphotobytes);
                }
                itemdescription.setText("");
                itemdescription.append(filters[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Слушатель события нажатия на кнопку "новый товар"
        newitemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                if(itemname.getText().toString().length()>4 && !itemcount.getText().toString().equals("") && itemvendor.getText().toString().length()>4) {
                    try {
                        if(!qprocessor.itemExist(database,itemname.getText().toString(),currentItemtype)) {
                            database.beginTransaction();

                            contentValues.put(DBHelper.KEY_ITEMTYPE, currentItemtype);
                            contentValues.put(DBHelper.KEY_QR, printQR(Long.toString(getProfilesCount())));
                            contentValues.put(DBHelper.KEY_ITEMNAME, itemname.getText().toString());
                            contentValues.put(DBHelper.KEY_COUNT, itemcount.getText().toString());
                            contentValues.put(DBHelper.KEY_DESCRIPTION, itemdescription.getText().toString());
                            contentValues.put(DBHelper.KEY_ITEMPHOTO, imagetobyte(itemphotobytes));
                            database.insert(DBHelper.TABLE_WAREHOUSE, null, contentValues);

                            ContentValues supplyvalues = new ContentValues();
                            supplyvalues.put(DBHelper.KEY_SUPPLYTYPE, "+");
                            supplyvalues.put(DBHelper.KEY_ITEMVENDOR, itemvendor.getText().toString());
                            supplyvalues.put(DBHelper.KEY_COUNT2, itemcount.getText().toString());
                            supplyvalues.put(DBHelper.KEY_DATE, System.currentTimeMillis());
                            supplyvalues.put("itemid", getProfilesCount());
                            database.insert(DBHelper.TABLE_SUPPLY, null, supplyvalues);

                            database.setTransactionSuccessful();
                        }
                        else{
                            Toast toast = Toast.makeText(act.getApplicationContext(), "Товар с заданным наименованием уже числится в базе товаров", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            itemname.setText("");
                        }

                    } catch (Exception ex) {
                        Toast toast = Toast.makeText(act.getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } finally {
                        if(database.inTransaction()) {
                            database.endTransaction();

                        Toast toast = Toast.makeText(act, "Товар успешно добавлен", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        clearforms();
                        }
                    }
                }
                else{

                    Toast toast = Toast.makeText(getContext(),"Заполните все необходимые поля",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        });

        choosephotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });


        return view;
    }

    /**
     * Инициализация форм
     */
    private void initializeViews() {
        itemname = (EditText) view.findViewById(R.id.itemName);
        itemtype = (Spinner) view.findViewById(R.id.itemType);
        itemcount = (EditText) view.findViewById(R.id.itemCount);

        itemvendor = (EditText) view.findViewById(R.id.itemVendor);
        itemdescription = (EditText) view.findViewById(R.id.itemDescription);

        newitemBtn = (Button) view.findViewById(R.id.addItemBtn);
        choosephotoBtn = (Button) view.findViewById(R.id.chooseItemPhoto);

        itemphoto = (ImageView) view.findViewById(R.id.newitemPhoto);
    }

    /**
     * Инициализация значений переменных
     */
    private void initializeValues() {
        helper = new DBHelper(act);
        database = helper.getWritableDatabase();
        baos = new ByteArrayOutputStream();
        qprocessor = new QueriesProcessor();
        itemphotobytes = BitmapFactory.decodeResource(getResources(), R.drawable.processors);
        filters = getResources().getStringArray(R.array.processorfilters);
    }

    public static int count(String str, String target) {
        return (str.length() - str.replace(target, "").length()) / target.length();
    }

    /**
     * Очистка форм приложения
     */
    public void clearforms() {
        itemname.setText("");
        itemvendor.setText("");
        itemcount.setText("");
        itemdescription.setText("Производитель:");
    }

    /**
     * Получение последнего идентификатора в таблице товаров
     *
     * @return последний идентификатор
     */
    public long getProfilesCount() {
        long count;
        c = database.rawQuery("SELECT COALESCE(MAX(itemid), 1) AS pls FROM itemtable", null);
        //c = database.rawQuery("SELECT last_insert_rowid() AS pls",null);
        c.moveToFirst();
        count = c.getInt(c.getColumnIndex("pls"));
        return count;
    }

    /**
     * Генерация QR-кода для указанного идентификатора товара
     *
     * @param id идентификатор товара
     * @return QR-код представленный в виде строки base64
     */
    public byte[] printQR(String id) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(id, BarcodeFormat.QR_CODE, 256, 256);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return imagetobyte(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Выбор изображения для товара из галереи снимков устройства
     */
    public void pickImage() {
        if (ActivityCompat.checkSelfPermission(act.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
        }
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");

        startActivityForResult(photoPickerIntent, 111);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        act = context;
    }

    /**
     * Обработчик результата выбора изображения для товара из галерии снимков устройства
     *
     * @param requestCode код запроса
     * @param resultCode  результирующий код
     * @param data        данные полученные в рамках активности
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 111:
                    Uri selectedImage = data.getData();
                    try {
                        itemphotobytes = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        itemphoto.setImageBitmap(itemphotobytes);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
    }

    public byte[] imagetobyte (Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return imageBytes;
    }
}

